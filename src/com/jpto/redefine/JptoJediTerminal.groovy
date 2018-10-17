package com.jpto.redefine

import com.jcraft.jsch.Session
import com.jediterm.terminal.TerminalDisplay
import com.jediterm.terminal.TerminalOutputStream
import com.jediterm.terminal.TerminalStarter
import com.jediterm.terminal.TtyConnector
import com.jediterm.terminal.model.JediTerminal
import com.jediterm.terminal.model.StyleState
import com.jediterm.terminal.model.TerminalTextBuffer
import com.jpto.core.JptoTerminalPanel
import com.jpto.core.JptoTerminalStarter
import com.jpto.core.concreator.JptoJSchShellTtyConnector
import groovy.transform.CompileStatic;
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.nonjdk.sshsup.JrrJschSession;

import java.util.logging.Logger;

@CompileStatic
class JptoJediTerminal extends JediTerminal{
    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();
    public static JptoNewStringListener jptoNewStringListener;
//    public JptoTerminalStarter jptoTerminalStarter;
    public JptoTerminalPanel jptoTerminalPanel;

    JptoJediTerminal(TerminalDisplay display, TerminalTextBuffer buf, StyleState initialStyleState) {
        super(display, buf, initialStyleState)
        this.jptoTerminalPanel = display as JptoTerminalPanel;
    }

    @Override
    void nextLine() {
        super.nextLine()
    }

    @Override
    void newLine() {
        super.newLine()
    }

    @Override
    void writeCharacters(String string) {
        if(jptoNewStringListener!=null){
            jptoNewStringListener.newValue(this,string)
        }
        super.writeCharacters(string)
    }

    // for ssh : subtype is JptoJSchShellTtyConnector
    TtyConnector getConnectionInfo(){
        JptoTerminalStarter starter = jptoTerminalPanel.jptoTerminalStarter as JptoTerminalStarter;
        TtyConnector ttyConnector = starter.ttyConnector
        return ttyConnector;
    }

//    @Override
//    void setTerminalOutput(TerminalOutputStream terminalOutput) {
//        this.jptoTerminalStarter = terminalOutput as JptoTerminalStarter
//        super.setTerminalOutput(terminalOutput)
//    }


}
