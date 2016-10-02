package com.jpto.core

import com.jpto.core.concreator.JptoSshJediTermWidget
import net.infonode.docking.View
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.nonjdk.idwutils.MyDockingWindowTitleProvider
import net.sf.jremoterun.utilities.nonjdk.swing.JrrSwingUtilsParent;

import java.util.logging.Logger;
import groovy.transform.CompileStatic;


@CompileStatic
class SshSessionStateMonitor implements Runnable{

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    public static volatile SshSessionStateMonitor sessionStateMonitor;
    private static Object dummyObject = new Object();

    static void createMonitor2(){
        if(sessionStateMonitor==null){
            createMonitor()
        }else{
            log.info "sessionStateMonitor already created : ${sessionStateMonitor.class.name}"
        }
    }

    static void createMonitor(){
        assert sessionStateMonitor==null
        sessionStateMonitor = new SshSessionStateMonitor()
        sessionStateMonitor.monitorThread.start()
    }

    WeakHashMap<JptoSshJediTermWidget,Object> sshSessions  = new WeakHashMap()

    boolean stopThread = false

    Thread monitorThread  = new Thread(this,SshSessionStateMonitor.simpleName);

    volatile long sleepTime = 5_000L


    void addSession(JptoSshJediTermWidget session){
        sshSessions.put(session,dummyObject)
    }

    @Override
    void run() {
        while(!stopThread){
            Thread.sleep(sleepTime)
            checkSessions()
        }

    }

    void checkSessions(){
        sshSessions.keySet().each {
            checkSession(it)
        }
    }


    void checkSession(JptoSshJediTermWidget it){
        if(it.jSchShellTtyConnector.initDone) {
            if (!it.jSchShellTtyConnector.sessionDead) {
                if (!it.jSchShellTtyConnector.isConnected()) {
                    log.info("session is dead ${it.jSchShellTtyConnector.host}")
                    View parentWindow = JrrSwingUtilsParent.findParentWindow(it, View)
                    if (parentWindow != null) {
                        MyDockingWindowTitleProvider titleProvider = new MyDockingWindowTitleProvider(parentWindow.getTitle() + ' dead');
                        parentWindow.getWindowProperties().setTitleProvider(titleProvider)
                    } else {
                        log.info "can't find parent window for ${it.jSchShellTtyConnector.host}"
                    }
                    it.jSchShellTtyConnector.sessionDead = true;
                }
            }
        }

    }


}
