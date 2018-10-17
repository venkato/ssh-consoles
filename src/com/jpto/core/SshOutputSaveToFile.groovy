package com.jpto.core

import groovy.transform.CompileStatic
import net.infonode.docking.View;
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.nonjdk.swing.JrrSwingUtils
import net.sf.jremoterun.utilities.nonjdk.swing.JrrSwingUtilsParent
import net.sf.jremoterun.utilities.nonjdk.swing.swingfind.Accepter
import net.sf.jremoterun.utilities.nonjdk.swing.swingfind.SwingComponentFinder
import org.apache.commons.io.FileUtils

import java.awt.Component
import java.text.DecimalFormat
import java.text.SimpleDateFormat;
import java.util.logging.Logger;

@CompileStatic
class SshOutputSaveToFile {
    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    public int maxFilesInFolder=50
    public int minLength=300
    public DecimalFormat decimalFormat = new DecimalFormat('00')

    void saveAllSessionPerionOneMonth(File toFolder){
        toFolder.mkdir()
        assert toFolder.exists()
        String folderName = new SimpleDateFormat('dd').format(new Date())
        File toFolder2 = new File(toFolder, folderName)
        FileUtils.deleteQuietly(toFolder2)
        toFolder2.mkdir()
        assert toFolder2.exists()
        saveAllSessions(toFolder2)
    }

    void saveAllSessions(File toFolder){
        if(!toFolder.exists()){
            throw new FileNotFoundException(toFolder.getAbsolutePath())
        }
        Accepter accepter = new Accepter(){

            @Override
            boolean accept(Component component) {
                if (component instanceof JptoTerminalPanel) {
                    JptoTerminalPanel panel = (JptoTerminalPanel) component;
                    saveOneSession(panel,toFolder)
                }
                return false
            }
        }
        SwingComponentFinder.findComponent(accepter)
    }



    void saveOneSession(JptoTerminalPanel panel,File toFolder){
        View view = JrrSwingUtilsParent.findParentComponent(panel, View)
        String title = view.getTitle()
        String screenWithHistory = panel.getScreenWithHistory()
        if(title==null){
            throw new NullPointerException('title is null')
        }
        if(isNeedSave(screenWithHistory)){
            File file = findFile(title, toFolder)
            file.text = screenWithHistory
        }else{
            log.info "no need to save ${title} : ${screenWithHistory}"
        }
    }

    File findFile(String title,File folder){

        if(!folder.exists()){
            throw new FileNotFoundException(folder.getAbsolutePath())
        }
        int i=0;
        while (true){
            String nn = title.replace('/','_')+'_'+decimalFormat.format(i)+'.txt'
            File f =new File(folder,nn)
            if(!f.exists()){
                return f
            }
            i++

            if(i>maxFilesInFolder){
                throw new Exception("too many files for ${title} ${folder}")
            }
        }
        throw new IllegalStateException()
    }


    boolean isNeedSave(String text){
        if(text==null){
            return false
        }
        if(text.length()<minLength){
            return false
        }
        return true
    }


}
