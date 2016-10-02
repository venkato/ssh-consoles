package com.jpto.sample

import com.jpto.core.JptoSshConsoles
import com.jpto.settings.SshSettings
import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.classpath.MavenDefaultSettings
import org.apache.log4j.Logger
;

@CompileStatic
public class SshSampleLauncher {
    private static final Logger logger = Logger.getLogger(SshSampleLauncher);
    private static final Logger log = logger;

    public static void main(String[] args) throws Exception {
        File userHome = System.getProperty('user.home') as File
        MavenDefaultSettings.mavenDefaultSettings.jrrDownloadDir = new File("${userHome}/jrr/jrrdownload");
        launch()
    }

    static void launch() {
        net.sf.jremoterun.utilities.nonjdk.InitGeneral.init1();
        SshSettings.customFunctions = new CustomFunctionsSample();
        JptoSshConsoles.launchCore();
    }
}
