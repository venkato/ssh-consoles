package com.jpto.empty

import com.jpto.core.JptoSshConsoles
import com.jpto.settings.SshSettings
import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.classpath.MavenDefaultSettings
import net.sf.jremoterun.utilities.groovystarter.GroovyMethodRunnerParams
import net.sf.jremoterun.utilities.nonjdk.InitGeneral
import org.apache.log4j.Logger

@CompileStatic
public class SshEmptyLauncher implements Runnable{
    private static final Logger logger = Logger.getLogger(SshEmptyLauncher);
    private static final Logger log = logger;

    public static void main(String[] args) throws Exception {
        File userHome = System.getProperty('user.home') as File
        MavenDefaultSettings.mavenDefaultSettings.jrrDownloadDir = new File("${userHome}/jrr/jrrdownload");
        launch()
    }

    static void launch() {
        addGrToPath()
        InitGeneral.init1();
        SshSettings.customFunctions = new CustomFunctionsEmpty();
        JptoSshConsoles.launchCore();
    }

    static void addGrToPath(){
        String pathId ='Path'
        String pathSeparator = System.getProperty('path.separator')
        String pathBefore = SshSettings.envs.get(pathId)
        if(pathBefore==null){
            log.info "failed find env var : ${pathId}"
        }else {

            File grRunner = new File(GroovyMethodRunnerParams.instance.grHome, 'firstdownload')
            assert grRunner.exists()
            grRunner = grRunner.canonicalFile

            String newPath = "${pathBefore}${pathSeparator}${grRunner.absolutePath}"
            SshSettings.envs.put(pathId, newPath)
        }

    }


    @Override
    void run() {
        launch()
    }
}
