package com.jpto.redefine

import com.jediterm.terminal.model.StyleState
import com.jediterm.terminal.model.TerminalTextBuffer
import com.jediterm.terminal.model.hyperlinks.TextProcessing;
import net.sf.jremoterun.utilities.JrrClassUtils;
import java.util.logging.Logger;
import groovy.transform.CompileStatic;


@Deprecated
@CompileStatic
class JptoTerminalTextBuffer extends TerminalTextBuffer{

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    JptoTerminalTextBuffer(int width, int height, StyleState styleState, int historyLinesCount, TextProcessing textProcessing) {
        super(width, height, styleState, historyLinesCount, textProcessing)
    }
}
