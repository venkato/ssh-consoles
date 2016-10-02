package com.jpto.core.concreator

import com.jediterm.terminal.model.TerminalLine
import com.jediterm.terminal.model.TerminalTextBuffer
import com.jediterm.terminal.ui.TerminalPanel;
import net.sf.jremoterun.utilities.JrrClassUtils;
import java.util.logging.Logger;
import groovy.transform.CompileStatic;


@CompileStatic
class TerminalPanelUtils {

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    /**
     * 1 means last line
     */
    static TerminalLine getLineFromEnd(TerminalPanel terminalPanel, int lineFromEnd) {
        assert lineFromEnd > 0
        TerminalTextBuffer textBuffer = terminalPanel.getTerminalTextBuffer();
        int count = textBuffer.getScreenLinesCount();
        int height = textBuffer.getHeight()
        if (count == 0) {
            log.info "no lines on screen"
            return null;
        }
        if (count != height) {
            log.info "count != height : ${count} != ${height}"
            count = Math.min(count, height);
        }
        if (count <= 0) {
            log.info "count is starnge : ${count}"
            return null;
        }
        int linenn = count - lineFromEnd
        TerminalLine line = textBuffer.getLine(linenn)
        return line
    }

}
