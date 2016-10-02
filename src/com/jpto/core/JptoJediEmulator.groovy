package com.jpto.core;

import com.jediterm.terminal.Terminal;
import com.jediterm.terminal.TerminalDataStream;
import com.jediterm.terminal.TerminalOutputStream;
import com.jediterm.terminal.TextStyle;
import com.jediterm.terminal.emulator.JediEmulator;
import com.jpto.settings.SshSettings;
import groovy.transform.CompileStatic;
import net.sf.jremoterun.utilities.JrrClassUtils;

import java.io.IOException;
import java.util.logging.Logger;


@CompileStatic
public class JptoJediEmulator extends JediEmulator {

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();
    private boolean preCharUp = false;
    String unsup1 = "k";
    String unsup2 = "\\";
    public volatile boolean enableAdoptToFixMsgs =SshSettings.enableAdoptToFixMsgs;

    public JptoJediEmulator(TerminalDataStream dataStream,  Terminal terminal) {
        super(dataStream, terminal);
    }

    @Override
    public void processChar(char ch, Terminal terminal) throws IOException {
        if (enableAdoptToFixMsgs) {
            if (ch == '^'.charAt(0) && !preCharUp) {
                preCharUp = true;
            } else {
                if (ch == 'A'.charAt(0) && preCharUp) {
                    myTerminal.characterAttributes(TextStyle.EMPTY);
                    processCharJpto('|', terminal);
                } else {
                    if (preCharUp) {
                        processCharJpto('^', terminal);
                    }
                    if (ch == '\001'.charAt(0)) {
                        processCharJpto('|', terminal);
                    } else {
                        super.processChar(ch, terminal);
                    }
                }
                preCharUp = false;
            }
        } else {
            super.processChar(ch, terminal);
        }
    }

    void processCharJpto(String ch,Terminal terminal){
        super.processChar(ch.charAt(0),terminal)

    }



    @Override
    protected void unsupported(char... sequenceChars) {
        String s = new String(sequenceChars);
        if (s.equals(unsup1) || s.equals(unsup2)) {

        } else {
            super.unsupported(sequenceChars);
        }
    }
}
