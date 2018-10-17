package com.jpto.sample

import com.jpto.core.JptoSshConsoles
import com.jpto.settings.SshSettings
import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.classpath.MavenDefaultSettings

import java.util.logging.Logger


@CompileStatic
public class SshSampleLauncher implements Runnable{
    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();


//    public static void main(String[] args) throws Exception {
//        File userHome = System.getProperty('user.home') as File
//        MavenDefaultSettings.mavenDefaultSettings.jrrDownloadDir = new File("${userHome}/jrr/jrrdownload");
//        launch()
//    }

    static void launch() {
        net.sf.jremoterun.utilities.java11.Java11ModuleSetDisable.doIfNeeded();
        net.sf.jremoterun.utilities.nonjdk.InitGeneral.init1();
        SshSettings.customFunctions = new CustomFunctionsSample();
        JptoSshConsoles.launchCore();

        Thread.sleep(Long.MAX_VALUE);
    }

    @Override
    void run() {
        launch()
    }
}
