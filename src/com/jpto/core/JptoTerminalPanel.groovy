package com.jpto.core

import com.jediterm.terminal.HyperlinkStyle
import com.jediterm.terminal.TerminalStarter
import com.jediterm.terminal.model.StyleState
import com.jediterm.terminal.model.TerminalLine
import com.jediterm.terminal.model.TerminalTextBuffer
import com.jediterm.terminal.ui.TerminalPanel
import com.jediterm.terminal.ui.settings.SettingsProvider
import com.jpto.core.concreator.TerminalPanelUtils
import com.jpto.settings.JptoJediSettings
import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.JrrClassUtils
import org.apache.commons.lang3.StringUtils
import org.apache.log4j.Logger

import javax.swing.JOptionPane
import java.awt.Dimension
import java.awt.Font
import java.awt.Point
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.StringSelection
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.im.InputMethodRequests
import java.util.function.Supplier

@CompileStatic
public class JptoTerminalPanel extends TerminalPanel {

    private static final Logger logger = Logger.getLogger(JptoTerminalPanel);
    private static final Logger log = logger;

    public StyleState styleState;
    public boolean inverted = false;
    public Font customFont

    public JptoTerminalPanel(SettingsProvider settingsProvider, TerminalTextBuffer terminalTextBuffer,
                             StyleState styleState) {
        super(settingsProvider, terminalTextBuffer, styleState);
        this.styleState = styleState;
        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(final MouseEvent e) {
                mouseClicked2(e)
            }
        });

    }

    @Override
    public Font createFont() {
        if(customFont!=null){
            return customFont
        }
        return super.createFont()
    }

    void setCustomFont2(Font font){
        customFont = font
        initFont()
    }

    void mouseClicked2(final MouseEvent e) {
        requestFocusInWindow();
        Runnable hyperlink = findHyperlink2(e.getPoint());
    }

    protected HyperlinkStyle findHyperlink2(Point p) {
        Point p2 = panelToCharCoords(p);
        HyperlinkStyle hs = JrrClassUtils.invokeJavaMethod(this, "findHyperlink", p) as HyperlinkStyle
        log.info "${p2} from ${p} ${hs}"
        return hs;
    }


    void invertStyle3() {
        inverted = !inverted;
        styleState.setDefaultStyle(inverted ? JptoJediSettings.blackStyle : JptoJediSettings.whiteStyle);
    }

    @Override
    protected void pasteFromClipboard(Supplier<String> clipboardStringSupplier) {
        log.info("paste pressed");
        String selection = clipboardStringSupplier.get();
        if (selection != null) {
            try {
                pasteFromClipboard2(selection, clipboardStringSupplier)
            } catch (Exception e) {
                log.info(e);
            }
        }
    }

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
        TerminalStarter myTerminalStarter2 = (TerminalStarter) JrrClassUtils.getFieldValue(this,
                "myTerminalStarter");
        myTerminalStarter2.sendString(selection);
    }

//	private String getClipboardString() {
//		try {
//			return getClipboardContent();
//		} catch (Exception e) {
//			log.info(e);
//		}
//		return null;
//	}

    @Override
    protected void setCopyContents(StringSelection selection2) {
        try {
            StringSelection stringSelection4 = cleanupString(selection2)
            if (stringSelection4!=null) {
                super.setCopyContents(stringSelection4);
            }
        } catch (Exception e) {
            log.warn(null, e);
        }
    }

    StringSelection cleanupString(StringSelection selection2) {
        String selection = (String) selection2.getTransferData(DataFlavor.stringFlavor);
        String selection3 = cleanupString(selection);
        if (selection3.isEmpty()) {
            log.info("try to copy empty string, skip");
            return null;
        }
        StringSelection stringSelection3 = new StringSelection(selection3);
        return stringSelection3;
    }

    String cleanupString(String selection) {
        selection = StringUtils.stripStart(selection, "\r\n\t ");
        selection = StringUtils.stripEnd(selection, "\r\n\t ");
        selection = selection.trim();
        return selection
    }


    protected void setCopySelectionContents(StringSelection selection2) {
        try {
            StringSelection stringSelection4 = cleanupString(selection2)
            if (stringSelection4!=null) {
                super.setCopySelectionContents(stringSelection4);
            }
        } catch (Exception e) {
            log.warn(null, e);
        }
    }

    TerminalLine getLastLine() {
        return getLineFromEnd(1)
    }

    /**
     * 1 means last line
     */
    TerminalLine getLineFromEnd(int lineFromEnd) {
        return TerminalPanelUtils.getLineFromEnd(this,lineFromEnd)
    }

    @Override
    protected Point panelToCharCoords(Point p) {
        Point res = super.panelToCharCoords(p)
//        log.info "${res}"
        return res
    }
//	public void setSett(SettingsProvi)

    JptoInputMethodRequests inputMethodRequests2 = new JptoInputMethodRequests(this); ;

    @Override
    public InputMethodRequests getInputMethodRequests() {
        return inputMethodRequests2
    }

    Dimension getCharSize() {
        return myCharSize
    }

    @Override
    JptoTerminalStarter getTerminalOutputStream() {
        return (JptoTerminalStarter) super.getTerminalOutputStream()
    }

    @Override
    String toString() {
        return "JptoTerminalPanel : ${getTerminalOutputStream()}"
    }
}
