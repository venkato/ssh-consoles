package com.jpto.core

import com.jediterm.terminal.Terminal
import com.jediterm.terminal.TerminalDataStream
import com.jediterm.terminal.TerminalOutputStream
import com.jediterm.terminal.TerminalStarter
// import com.jediterm.terminal.TtyChannel
import com.jediterm.terminal.TtyConnector
import com.jediterm.terminal.emulator.JediEmulator;
import net.sf.jremoterun.utilities.JrrClassUtils;
import java.util.logging.Logger;
import groovy.transform.CompileStatic;


@CompileStatic
class JptoTerminalStarter extends TerminalStarter{

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    public JptoJediEmulator jptoJediEmulator

    public  TtyConnector ttyConnector;

    JptoTerminalStarter(Terminal terminal, TtyConnector ttyConnector, TerminalDataStream dataStream) {
        super(terminal, ttyConnector,dataStream)
        this.ttyConnector = ttyConnector;
    }

    @Override
    protected JediEmulator createEmulator(TerminalDataStream dataStream, Terminal terminal) {
        jptoJediEmulator = new JptoJediEmulator(dataStream, terminal)
        return jptoJediEmulator
    }

    @Override
    String toString() {
        return "JptoTerminalStarter : ${ttyConnector}"
    }
}
