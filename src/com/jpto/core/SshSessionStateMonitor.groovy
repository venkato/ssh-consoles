package com.jpto.core

import com.jpto.core.concreator.JptoSshJediTermWidget
import com.jpto.settings.SshSettings
import net.infonode.docking.View
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.nonjdk.dateutils.DurationConstants
import net.sf.jremoterun.utilities.nonjdk.idwutils.MyDockingWindowTitleProvider
import net.sf.jremoterun.utilities.nonjdk.swing.JrrSwingUtilsParent
import net.sf.jremoterun.utilities.nonjdk.timer.simple1.PeriodicTaskSimple

import javax.swing.SwingUtilities;
import java.util.logging.Logger;
import groovy.transform.CompileStatic;


@CompileStatic
class SshSessionStateMonitor extends PeriodicTaskSimple {

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    public static volatile SshSessionStateMonitor sessionStateMonitor;
    private static Object dummyObject = new Object();

    static void createMonitor2() {
        if (sessionStateMonitor == null) {
            createMonitor()
        } else {
            log.info "sessionStateMonitor already created : ${sessionStateMonitor.class.name}"
        }
    }

    static void createMonitor() {
        assert sessionStateMonitor == null
        sessionStateMonitor = new SshSessionStateMonitor()
        sessionStateMonitor.start()
    }

    WeakHashMap<JptoSshJediTermWidget, Object> sshSessions = new WeakHashMap()

//    boolean stopThread = false

//    Thread monitorThread  = new Thread(this,SshSessionStateMonitor.simpleName);

//    volatile long sleepTime = 5_000L

    SshSessionStateMonitor() {
        super(DurationConstants.oneSec.timeInMsLong * 5, SshSessionStateMonitor.getSimpleName())
        startUpDelay = sleepTime
    }

    void addSession(JptoSshJediTermWidget session) {
        sshSessions.put(session, dummyObject)
    }

//    @Override
//    void run() {
//        while(!stopThread){
//            Thread.sleep(sleepTime)
//            checkSessions()
//        }
//
//    }

    @Override
    void doJob() {
        new ArrayList<JptoSshJediTermWidget>(sshSessions.keySet()).each {
            checkSession(it)
        }
    }


    void checkSession(JptoSshJediTermWidget sshWidget) {
        if (sshWidget.jSchShellTtyConnector.initDone) {
            if (!sshWidget.jSchShellTtyConnector.sessionDead) {
                if (sshWidget.jSchShellTtyConnector.isConnected()) {
                    if (SshSettings.sessionInactiveMonitor != null) {
                        long now = System.currentTimeMillis()
                        long diff = now - sshWidget.jSchShellTtyConnector.lastWrittenDate
                        if (diff > SshSettings.sessionInactiveMonitor.monitorTime) {
                            sshWidget.jSchShellTtyConnector.lastWrittenDate = now
                            SshSettings.sessionInactiveMonitor.onSessionInactive(sshWidget)
                        }
                    }
                } else {
                    log.info("session is dead ${sshWidget.jSchShellTtyConnector.host}")
                    View parentWindow = JrrSwingUtilsParent.findParentWindow(sshWidget, View)
                    if (parentWindow != null) {
                        SwingUtilities.invokeLater {
                            MyDockingWindowTitleProvider titleProvider = new MyDockingWindowTitleProvider(parentWindow.getTitle() + ' dead');
                            parentWindow.getWindowProperties().setTitleProvider(titleProvider)
                        }
                    } else {
                        log.info "can't find parent window for ${sshWidget.jSchShellTtyConnector.host}"
                    }
                    sshWidget.jSchShellTtyConnector.sessionDead = true;
                }
            }
        }

    }


}
