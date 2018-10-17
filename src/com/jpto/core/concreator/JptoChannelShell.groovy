package com.jpto.core.concreator

import com.jcraft.jsch.JSchException
import com.jcraft.jsch.JrrSchSessionLog
import com.jcraft.jsch.Session
import groovy.transform.CompileStatic;
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.nonjdk.sshsup.JrrJschIO
import net.sf.jremoterun.utilities.nonjdk.sshsup.JrrJschSession
import net.sf.jremoterun.utilities.nonjdk.sshsup.channels.JrrChannelShell
import net.sf.jremoterun.utilities.nonjdk.sshsup.channels.JschChannelType;

import java.util.logging.Logger;

@CompileStatic
class JptoChannelShell extends JrrChannelShell{
    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    public int openShellTimeout =  com.jpto.settings.SshSettings.openShellChannelTimeoutInMs;

    private int connectTimeout3

    static void initCustomShell(){
        JschChannelType.shell.clazz =JptoChannelShell;
    }

    //@Override
    void setExitListener(Runnable exitListener) {
        super.setExitListener(exitListener)
    }

    //@Override
    Runnable getExitListener() {
        return super.getExitListener()
    }

    //@Override
    JrrJschIO getJrrJschIO() {
        return super.getJrrJschIO()
    }

    //@Override
    void setJrrJschIO(JrrJschIO jrrJschIO) {
        super.setJrrJschIO(jrrJschIO)
    }

    @Override
    void connect(int connectTimeout) throws JSchException {
        if(openShellTimeout>=0){
            connectTimeout = openShellTimeout;
        }
        connectTimeout3 = connectTimeout;
        super.connect(connectTimeout)
    }

    @Override
    protected void sendChannelOpen() throws Exception {
        JrrSchSessionLog sessionLog;
        Session session1 = getSession()
        if (session1 instanceof JrrJschSession) {
            JrrJschSession session3 = (JrrJschSession) session1;
            sessionLog = session3.jrrSchSessionLog
            sessionLog.logMsg("connecting to shell channel with timeout ${connectTimeout3} ms")
        }
        long start = System.currentTimeMillis();
        try {

            super.sendChannelOpen()

            long duration = System.currentTimeMillis() -start;
            duration = (long)(duration/1000);
            log.info "shell channel opened within  ${duration} s"
            sessionLog.logMsg("sendChannelOpen done fine withing ${duration} s")
        }catch(Exception e){
            if(sessionLog!=null) {
                sessionLog.logMsg("sendChannelOpen failed : ${e}")
            }
            throw e
        }

    }


    @Override
    void start() throws JSchException {
        JrrSchSessionLog sessionLog;
        Session session1 = getSession()
        if (session1 instanceof JrrJschSession) {
            JrrJschSession session3 = (JrrJschSession) session1;
            sessionLog = session3.jrrSchSessionLog
            sessionLog.logMsg("starting shell")
        }
        try {
            super.start()
            sessionLog.logMsg("started shell channel")
        }catch(Exception e){
            if(sessionLog!=null) {
                sessionLog.logMsg("start shell channel failed : ${e}")
            }
            throw e
        }

    }

    @Override
    protected void sendRequests() throws Exception {
        JrrSchSessionLog sessionLog;
        Session session1 = getSession()
        if (session1 instanceof JrrJschSession) {
            JrrJschSession session3 = (JrrJschSession) session1;
            sessionLog = session3.jrrSchSessionLog
            sessionLog.logMsg("starting sendRequests")
        }
        try {

        super.sendRequests()
            sessionLog.logMsg("sendRequests finished fine")
        }catch(Exception e){
            if(sessionLog!=null) {
                sessionLog.logMsg("sendRequests failed : ${e}")
            }
            throw e
        }

    }
}
