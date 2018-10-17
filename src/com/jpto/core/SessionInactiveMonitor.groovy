package com.jpto.core

import com.jpto.core.concreator.JptoSshJediTermWidget
import groovy.transform.CompileStatic;
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.nonjdk.dateutils.DurationConstants;

import java.util.logging.Logger;

@CompileStatic
class SessionInactiveMonitor {
    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    public long monitorTime = 4* DurationConstants.oneHour.timeInMsLong
    public String writeTestMsg = 'pwd\n'

    void onSessionInactive(JptoSshJediTermWidget jptoSshJediTermWidget) {
        //jptoSshJediTermWidget.jSchShellTtyConnector.las
        jptoSshJediTermWidget.enterTextAndCHeckVisible(writeTestMsg)
//        jptoSshJediTermWidget.jSchShellTtyConnector.write(writeTestMsg)
    }
}
