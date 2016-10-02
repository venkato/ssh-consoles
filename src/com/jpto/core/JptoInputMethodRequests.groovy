package com.jpto.core

import net.sf.jremoterun.utilities.JrrClassUtils

import java.awt.Point
import java.awt.Rectangle
import java.awt.font.TextHitInfo
import java.awt.im.InputMethodRequests
import java.text.AttributedCharacterIterator;
import java.util.logging.Logger;
import groovy.transform.CompileStatic;


@CompileStatic
class JptoInputMethodRequests implements InputMethodRequests {

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    JptoTerminalPanel terminalPanel;

    JptoInputMethodRequests(JptoTerminalPanel terminalPanel) {
        this.terminalPanel = terminalPanel
    }

    Rectangle emprtyRect = new Rectangle(0,0,0,0)

    @Override
    public Rectangle getTextLocation(TextHitInfo offset) {
        if(!terminalPanel.isShowing()){
            log.info "terminalPanel with last line not showed : ${terminalPanel.getLastLine()}, return 00"
            return emprtyRect
        }
        Rectangle r = new Rectangle(terminalPanel.getTerminalCursor().getCoordX() * terminalPanel.getCharSize().@width, (terminalPanel.getTerminalCursor().getCoordY() + 1) * terminalPanel.getCharSize().@height,
                0, 0);
        Point p = terminalPanel.getLocationOnScreen();
        r.translate(p.@x, p.@y);
        return r;
    }

    @Override
    public TextHitInfo getLocationOffset(int x, int y) {
        return null;
    }

    @Override
    public int getInsertPositionOffset() {
        return 0;
    }

    @Override
    public AttributedCharacterIterator getCommittedText(int beginIndex, int endIndex, AttributedCharacterIterator.Attribute[] attributes) {
        return null;
    }

    @Override
    public int getCommittedTextLength() {
        return 0;
    }

    @Override
    public AttributedCharacterIterator cancelLatestCommittedText(AttributedCharacterIterator.Attribute[] attributes) {
        return null;
    }

    @Override
    public AttributedCharacterIterator getSelectedText(AttributedCharacterIterator.Attribute[] attributes) {
        return null;
    }


}
