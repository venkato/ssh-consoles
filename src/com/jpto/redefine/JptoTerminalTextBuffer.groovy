package com.jpto.redefine

import com.jediterm.terminal.model.CharBuffer
import com.jediterm.terminal.model.StyleState
import com.jediterm.terminal.model.TerminalTextBuffer
import com.jediterm.terminal.model.hyperlinks.TextProcessing;
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.NewValueListener
import org.apache.commons.lang.StringUtils
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;
import groovy.transform.CompileStatic;


@Deprecated
@CompileStatic
class JptoTerminalTextBuffer extends TerminalTextBuffer{

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();
//    public static NewValueListener


    JptoTerminalTextBuffer(int width, int height, StyleState styleState, int historyLinesCount, TextProcessing textProcessing) {
        super(width, height, styleState, historyLinesCount, textProcessing)
    }

    @Override
    void writeString(int x, int y, @NotNull CharBuffer str) {
        super.writeString(x, y, str)
    }
}
