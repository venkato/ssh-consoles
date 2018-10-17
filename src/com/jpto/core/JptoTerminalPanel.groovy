package com.jpto.core

import com.jediterm.terminal.HyperlinkStyle
import com.jediterm.terminal.TerminalCopyPasteHandler
import com.jediterm.terminal.TerminalStarter
import com.jediterm.terminal.TtyConnector
import com.jediterm.terminal.model.LinesBuffer
import com.jediterm.terminal.model.StyleState
import com.jediterm.terminal.model.TerminalLine
import com.jediterm.terminal.model.TerminalTextBuffer
import com.jediterm.terminal.ui.JediTermWidget
import com.jediterm.terminal.ui.TerminalPanel
import com.jediterm.terminal.ui.settings.SettingsProvider
import com.jpto.core.concreator.TerminalPanelUtils
import com.jpto.settings.JptoJediSettings
import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.NewValueListener
import org.apache.commons.lang3.StringUtils


import javax.swing.JOptionPane
import javax.swing.SwingUtilities
import java.awt.Dimension
import java.awt.Font
import java.awt.Point
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.StringSelection
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.im.InputMethodRequests
import java.util.function.Supplier
import java.util.logging.Logger

@CompileStatic
public class JptoTerminalPanel extends TerminalPanel {

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();


    public StyleState styleState;
    public boolean inverted = false;
    public Font customFont
    public JptoTerminalCopyPasteHandler jptoTerminalCopyPasteHandler;
    public JptoInputMethodRequests inputMethodRequests2 = new JptoInputMethodRequests(this); ;
    public TerminalStarter jptoTerminalStarter;
    public TerminalTextBuffer jptoTerminalTextBuffer;
    public JptoMethodInfo jptoMethodInfo;

    public JptoTerminalPanel(SettingsProvider settingsProvider, TerminalTextBuffer terminalTextBuffer,
                             StyleState styleState) {
        super(settingsProvider, terminalTextBuffer, styleState);
        this.styleState = styleState;
        this.jptoTerminalTextBuffer = terminalTextBuffer;
        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(final MouseEvent e) {
                mouseClicked2(e)
            }
        });

    }

    @Override
    void setTerminalStarter(TerminalStarter terminalStarter) {
        super.setTerminalStarter(terminalStarter)
        jptoTerminalStarter = terminalStarter;
    }

    @Override
    protected JptoTerminalCopyPasteHandler createCopyPasteHandler() {
        jptoTerminalCopyPasteHandler = new JptoTerminalCopyPasteHandler();
        return jptoTerminalCopyPasteHandler;
    }

    @Override
    public Font createFont() {
        if (customFont != null) {
            return customFont
        }
        return super.createFont()
    }

    void setCustomFont2(Font font) {
        customFont = font
        initFont()
    }

    void mouseClicked2(final MouseEvent e) {
        requestFocusInWindow();
        HyperlinkStyle hyperlink = findHyperlink2(e.getPoint());
    }

    protected HyperlinkStyle findHyperlink2(Point p) {
        return findHyperlink(p)
//        Point p2 = panelToCharCoords(p);
//        HyperlinkStyle hs = JrrClassUtils.invokeJavaMethod(this, "findHyperlink", p) as HyperlinkStyle
//        log.info "${p2} from ${p} ${hs}"
//        return hs;
    }


    void invertStyle3() {
        inverted = !inverted;
        styleState.setDefaultStyle(inverted ? JptoJediSettings.blackStyle : JptoJediSettings.whiteStyle);
    }

//    @Override
//    protected void pasteFromClipboard(Supplier<String> clipboardStringSupplier) {
//        log.info("paste pressed");
//        String selection = clipboardStringSupplier.get();
//        if (selection != null) {
//            try {
//                pasteFromClipboard2(selection, clipboardStringSupplier)
//            } catch (Exception e) {
//                log.info(e);
//            }
//        }
//    }







    protected void pasteFromClipboard2(String selection, Supplier<String> clipboardStringSupplier) {
        selection = selection.replace("\r\n", "\n");
        selection = selection.replace('\n', '\r');
        selection = selection.trim();
        if (selection.contains("\r")) {
            log.info("try to paste : " + selection);
            int dialogButton = JOptionPane.showConfirmDialog(null, "Paste multi line?", "Warning",
                    JOptionPane.YES_NO_OPTION);
            if (dialogButton != JOptionPane.YES_OPTION) {
                log.info("reject insert multi line");
                return;
            }
        }
//        TerminalStarter myTerminalStarter2 = (TerminalStarter) JrrClassUtils.getFieldValue(this,
//                "myTerminalStarter");
        jptoTerminalStarter.sendString(selection);
    }

//	private String getClipboardString() {
//		try {
//			return getClipboardContent();
//		} catch (Exception e) {
//			log.info(e);
//		}
//		return null;
//	}


    TerminalLine getLastLine() {
        return getLineFromEnd(1)
    }

    /**
     * 1 means last line
     */
    TerminalLine getLineFromEnd(int lineFromEnd) {
        return TerminalPanelUtils.getLineFromEnd(this, lineFromEnd)
    }

    @Override
    protected Point panelToCharCoords(Point p) {
        Point res = super.panelToCharCoords(p)
//        log.info "${res}"
        return res
    }
//	public void setSett(SettingsProvi)


    @Override
    public InputMethodRequests getInputMethodRequests() {
        return inputMethodRequests2
    }

    Dimension getCharSize() {
        return myCharSize
    }

    protected void pasteFromClipboardSuper(boolean useSystemSelectionClipboardIfAvailable) {
        super.pasteFromClipboard(useSystemSelectionClipboardIfAvailable);
    }

    //@Override
    protected void pasteFromClipboard(boolean useSystemSelectionClipboardIfAvailable) {
        String text = jptoTerminalCopyPasteHandler.getContents(useSystemSelectionClipboardIfAvailable);

        if (text == null) {
            return;
        }

        try {
            text = getContentCustom(text, useSystemSelectionClipboardIfAvailable)
            if (text != null) {
                jptoTerminalStarter.sendString(text);
            }
        } catch (Exception e) {
            log.info3('',e);
        }

    }

    protected String getContentCustom(String selection, boolean useSystemSelectionClipboardIfAvailable) {
        selection = selection.replace("\r\n", "\n");
        selection = selection.replace('\n', '\r');
        selection = selection.trim();
        if (selection.length() == 0) {
            return null;
        }
//        if(!useSystemSelectionClipboardIfAvailable){
//            return selection;
//        }
        if (selection.contains("\r")) {
            log.info("try to paste : " + selection.length());
            log.info("try to paste : " + selection);
            int dialogButton = JOptionPane.showConfirmDialog(null, "Paste multi line?", "Warning",
                    JOptionPane.YES_NO_OPTION);
            if (dialogButton != JOptionPane.YES_OPTION) {
                log.info("reject insert multi line");
                return null;
            }
        }
//        TerminalStarter myTerminalStarter2 = (TerminalStarter) JrrClassUtils.getFieldValue(this,
//                "myTerminalStarter");
        //sendStringSuper(selection);
        return selection;
    }


    //@Override
    protected HyperlinkStyle findHyperlink(Point p) {
        java.awt.Point p5 = panelToCharCoords(p);
        int x1 = p5.@x;
        int y1 = p5.@y;
        if (x1 >= 0 && x1 < jptoTerminalTextBuffer.getWidth() && y1 <= jptoTerminalTextBuffer.getHeight()) {
            com.jediterm.terminal.TextStyle testStyle = jptoTerminalTextBuffer.getStyleAt(x1, y1);
            if (testStyle instanceof com.jediterm.terminal.HyperlinkStyle) {
                return (com.jediterm.terminal.HyperlinkStyle) testStyle;
            }
        }
        return null;
    }

    //public static String lineSep23 = '\n'

    List<String> getVisibleText() {
        List<String> result = []
        String currentLine = '';
        //StringBuilder sb = new StringBuilder();
        int count = jptoTerminalTextBuffer.getScreenLinesCount();
        for (int i = 0; i < count; i++) {
            TerminalLine line = jptoTerminalTextBuffer.getLine(i)
            if (line.isNul()) {
                result.add("")
            } else {
                currentLine += line.getText();
                if (!line.isWrapped()) {
                    result.add(currentLine);
                    currentLine = '';
                }
            }
        }
        return result;
    }

    List<String> getTextNoWrap(LinesBuffer terminalTextBuffer) {
        List<String> result = []
        String currentLine = '';
        //StringBuilder sb = new StringBuilder();
        int count = terminalTextBuffer.getLineCount();
        for (int i = 0; i < count; i++) {
            TerminalLine line = terminalTextBuffer.getLine(i)
            if (line.isNul()) {
                result.add("")
            } else {
                currentLine += line.getText();
                if (!line.isWrapped()) {
                    result.add(currentLine);
                    currentLine = '';
                }
            }
        }
        return result;
    }

    String getLastLineNoWrap(){
        List<String> text1 = getVisibleText()
        text1= text1.findAll{it.length()>0};
        return text1.last()
    }




    String getScreenWithHistory() {
        TerminalTextBuffer textBuffer1 = getTerminalTextBuffer()
        List<String> rr = []
        LinesBuffer histBuffer = textBuffer1.getHistoryBuffer();
        if (histBuffer != null) {
            rr.addAll( getTextNoWrap(histBuffer))
        }
        rr.addAll(getVisibleText())
        return rr.join('\n')
    }

//    @Deprecated
//    String getScreenWithHistoryOld() {
//        TerminalTextBuffer textBuffer1 = getTerminalTextBuffer()
//        String history = null
//        LinesBuffer histBuffer = textBuffer1.getHistoryBuffer()
//        if (histBuffer != null) {
//            history = histBuffer.getLines()
//        }
//        String screenLines1 = textBuffer1.getScreenLines()
//        if (history != null && history.length() > 0) {
//            screenLines1 = history + '\n' + screenLines1
//        }
//        return screenLines1
//    }


    @Override
    JptoTerminalStarter getTerminalOutputStream() {
        return (JptoTerminalStarter) super.getTerminalOutputStream()
    }

    @Override
    String toString() {
        return "JptoTerminalPanel : ${getTerminalOutputStream()}"
    }

    JediTermWidget getJediTermWidget(){
        JediTermWidget jedi = (JediTermWidget) getParent().getParent();
        return jedi
    }

}
