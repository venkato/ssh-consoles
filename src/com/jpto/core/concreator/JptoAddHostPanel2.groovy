package com.jpto.core.concreator

import com.jediterm.terminal.ui.TerminalSession
import com.jpto.settings.SshSettings
import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.JrrClassUtils

import java.util.logging.Logger

@CompileStatic
class JptoAddHostPanel2 {

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();


    static JptoSshJediTermWidget createTerminalImpl3(JptoJSchShellTtyConnector jSchShellTtyConnector) throws Exception {
        JptoSshJediTermWidget jediTermWidget = new JptoSshJediTermWidget(SshSettings.defaultSettingsProvider,
                jSchShellTtyConnector);
        TerminalSession createTerminalSession = jediTermWidget.createTerminalSession(jSchShellTtyConnector);
        createTerminalSession.start();
        synchronized (jSchShellTtyConnector.initDonelock) {
            if (!jSchShellTtyConnector.initDone) {
                jSchShellTtyConnector.initDonelock.wait(SshSettings.logonTimeOutInMs);
            }
        }
        return jediTermWidget;
    }
}
