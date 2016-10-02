package com.jpto.core

import com.google.common.collect.Maps
import com.jediterm.pty.PtyMain
import com.jpto.settings.SshSettings
import com.pty4j.PtyProcess
import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.JrrClassUtils

import java.nio.charset.Charset
import java.util.logging.Logger

@CompileStatic
class JptoTtyConnector extends PtyMain.LoggingPtyProcessTtyConnector implements com.jediterm.terminal.TtyConnector {

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    static Charset utf8Charset = Charset.forName("UTF-8");

    List<String> cmd;
    File dir;


    JptoTtyConnector(List<String> cmd, File dir) {
        super(createProcess(cmd,dir), utf8Charset)
        this.cmd  = cmd;
        this.dir = dir;
    }

    static PtyProcess createProcess(List<String> cmd, File dir){
//        assert JptoTtyConnector.classLoader == ClassLoader.getSystemClassLoader()
        String[] cmd2 = cmd.toArray(new String[0])
        PtyProcess process = PtyProcess.exec(cmd2, SshSettings.envs, dir?.absolutePath);
        return process
    }

    @Override
    String toString() {
        return "JptoTtyConnector : ${cmd}"
    }
}
