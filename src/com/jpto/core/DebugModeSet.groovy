package com.jpto.core

import com.jcraft.jsch.JSch
import com.jpto.settings.SshSettings
import groovy.transform.CompileStatic;
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.nonjdk.sshsup.JSchSettingsDefaults
import net.sf.jremoterun.utilities.nonjdk.sshsup.JrrJschSession
import net.sf.jremoterun.utilities.nonjdk.sshsup.JscpLogger

import java.util.logging.Logger;

@CompileStatic
class DebugModeSet {
    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    static void setDebugMode(){
        setCleverDebug()
        SshSettings.logLevel        = com.jcraft.jsch.Logger.DEBUG;
        JSch.setLogger(new JscpLogger(SshSettings.logLevel));
        JSchSettingsDefaults.logSessionSettingsDefault = true;
    }




    static void setCleverDebug(){
        net.sf.jremoterun.utilities.nonjdk.sshsup.auth.InitAuth.init();
        com.jpto.core.concreator.JptoChannelShell.initCustomShell()
        JSchSettingsDefaults.logSessionSettingsDefault = false;
    }

}
