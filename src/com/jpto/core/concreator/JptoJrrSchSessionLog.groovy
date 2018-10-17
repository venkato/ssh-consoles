package com.jpto.core.concreator

import com.jediterm.terminal.Terminal
import groovy.transform.CompileStatic;
import net.sf.jremoterun.utilities.JrrClassUtils
import com.jcraft.jsch.JrrSchSessionLog;

import java.util.logging.Logger;

@CompileStatic
class JptoJrrSchSessionLog extends JrrSchSessionLog{
    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    public Terminal terminal;

    JptoJrrSchSessionLog(Terminal terminal) {
        this.terminal = terminal
        if(terminal==null){
            throw new NullPointerException('terminal is null')
        }
    }

    @Override
    void logMsg(String msg) {
        try {
            //log.info "writing to terminal : ${msg}"
            terminal.writeUnwrappedString(' ' +msg+' ');
        }catch(Throwable e){
            log.severe('failed write to terminal',e)
        }
    }
}
