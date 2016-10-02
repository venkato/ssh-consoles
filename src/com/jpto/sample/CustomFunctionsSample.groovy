package com.jpto.sample

import com.jpto.core.AddHostRunner
import com.jpto.core.JptoAddHostPanel
import com.jpto.core.JptoCustomFunctions
import com.jpto.settings.SshSettings
import groovy.transform.CompileStatic
import net.infonode.docking.DockingWindow
import net.infonode.docking.SplitWindow
import net.infonode.docking.TabWindow
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.UrlToFileConverter
import net.sf.jremoterun.utilities.nonjdk.downloadutils.WinptyDownloader
import net.sf.jremoterun.utilities.nonjdk.idwutils.IdwUtils
import org.apache.commons.io.FileUtils
import org.apache.log4j.Logger
;

/**
 * This class describes to what host need connect. And layout
 */
@CompileStatic
public class CustomFunctionsSample extends JptoCustomFunctions {

    private static final Logger log = Logger.getLogger(CustomFunctionsSample);

    @Override
    public DockingWindow initHosts() throws Exception {
        // SshSettings.password = "";
        WinptyDownloader.downloadWinpty();
        SshSettings.enableAdoptToFixMsgs = true;
        TabWindow tabWindowUp = JptoAddHostPanel.tabWindow;
        TabWindow tabWindowButtom = new TabWindow();
        SplitWindow splitWindow = new SplitWindow(false, 0.5f, tabWindowUp, tabWindowButtom);
        tabWindowUp.addTab(JptoAddHostPanel.getAddHostPanelView());
        File userHome = System.getProperty('user.home') as File
        File addHost = new File("${userHome}/jrr/configs/addhost.groovy")
        if (true) {
            File f = UrlToFileConverter.c.convert JrrClassUtils.currentClassLoader.getResource('ssh_icon.png')
            File f2 = f.parentFile.parentFile
            File tempate = new File(f2,"addhost-template.groovy");
            assert tempate.exists()
            FileUtils.copyFile(tempate, addHost)
        }
        SplitWindow panel3 = new AddHostRunner(addHost).mainPanel3;
        IdwUtils.setTitle(panel3, 'Add host2')
        tabWindowUp.addTab(panel3);
        SshSettings.addHostNameTempalate = "b";
        tabWindowUp.addTab(createTerminal("b"));
        tabWindowButtom.addTab(createTerminal("b"));

        tabWindowUp.addTab(createPtyTerminal(null, [SshSettings.nativeShellCmd]));
        tabWindowButtom.addTab(createPtyTerminal(null, [SshSettings.nativeShellCmd]));
        return splitWindow;
    }
}
