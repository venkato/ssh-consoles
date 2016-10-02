package com.jpto.settings

import com.jediterm.terminal.HyperlinkStyle
import com.jediterm.terminal.TerminalColor
import com.jediterm.terminal.TextStyle
import com.jediterm.terminal.emulator.ColorPalette
import com.jediterm.terminal.model.LinesBuffer
import com.jediterm.terminal.ui.UIUtil
import com.jediterm.terminal.ui.settings.SettingsProvider
import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.JrrClassUtils

import javax.swing.KeyStroke
import java.awt.Color
import java.awt.Font
import java.awt.event.InputEvent
import java.awt.event.KeyEvent
import java.util.logging.Logger

@CompileStatic
public class JptoJediSettings implements SettingsProvider {

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    public volatile boolean useInverseSelectionColorFlag = true;

    @Override
    public boolean useInverseSelectionColor() {
        return useInverseSelectionColorFlag;
    }

    public javax.swing.KeyStroke[] getPasteKeyStrokes() {
        javax.swing.KeyStroke[] ks = [
                KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK),
                KeyStroke.getKeyStroke(KeyEvent.VK_INSERT, InputEvent.SHIFT_DOWN_MASK)]
        return ks;
    }

    public static TextStyle hiperLinks =
            new TextStyle(TerminalColor.awt(Color.BLUE), TerminalColor.WHITE);

    @Override
    public TextStyle getHyperlinkColor() {
        return hiperLinks
    }


    @Override
    HyperlinkStyle.HighlightMode getHyperlinkHighlightingMode() {
        return HyperlinkStyle.HighlightMode.ALWAYS
    }

//    Object getHyperlinkHighlightingMode(){
//        return null;
//    }
    @Override
    public KeyStroke[] getPageUpKeyStrokes() {
        return createKsOne(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP, InputEvent.SHIFT_DOWN_MASK) );
    }

    @Override
    public KeyStroke[] getPageDownKeyStrokes() {
        return createKsOne (KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN, InputEvent.SHIFT_DOWN_MASK) );
    }


    static KeyStroke[] createKsOne(KeyStroke ks) {
        KeyStroke[] ksArray = [ks]
        return ksArray
    }


    @Override
    public KeyStroke[] getNewSessionKeyStrokes() {
        return createKs(KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.META_DOWN_MASK)
                , KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
    }

    @Override
    public KeyStroke[] getCloseSessionKeyStrokes() {
        return createKs(
                KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.META_DOWN_MASK)
                , KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
    }

    @Override
    public KeyStroke[] getCopyKeyStrokes() {
        return createKs(
                KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.META_DOWN_MASK)
                // CTRL + C is used for signal; use CTRL + SHIFT + C instead
                , KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
    }


    @Override
    public KeyStroke[] getClearBufferKeyStrokes() {
        return createKs(
                KeyStroke.getKeyStroke(KeyEvent.VK_K, InputEvent.META_DOWN_MASK)
                , KeyStroke.getKeyStroke(KeyEvent.VK_K, InputEvent.CTRL_DOWN_MASK));
    }

    @Override
    public KeyStroke[] getFindKeyStrokes() {
        return createKs(
                KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.META_DOWN_MASK)
                , KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK));
    }


    static KeyStroke[] createKs(KeyStroke ifMac, KeyStroke ifNotMac) {
        KeyStroke ks = UIUtil.isMac ? ifMac : ifNotMac
        KeyStroke[] ksArray = [ks]
        return ksArray
    }

    @Override
    public ColorPalette getTerminalColorPalette() {
        return UIUtil.isWindows ? ColorPalette.WINDOWS_PALETTE : ColorPalette.XTERM_PALETTE;
    }

    @Override
    public Font getTerminalFont() {
        return terminalFont;
    }

    public volatile Font terminalFont = getTerminalFont2();

    public Font getTerminalFont2() {
        String fontName;
        if (UIUtil.isWindows) {
            fontName = "Consolas";
        } else if (UIUtil.isMac) {
            fontName = "Menlo";
        } else {
            fontName = "Monospaced";
        }
        return Font.decode(fontName).deriveFont(getTerminalFontSize());
    }

    @Override
    public float getTerminalFontSize() {
        return 14;
    }

    @Override
    public float getLineSpace() {
        return 0;
    }

    @Override
    public TextStyle getDefaultStyle() {
        return defaultStyle;
    }


    public volatile TextStyle defaultStyle = whiteStyle;

    public static TextStyle blackStyle =
            new TextStyle(TerminalColor.WHITE, TerminalColor.rgb(24, 24, 24));

    public static TextStyle whiteStyle =
            new TextStyle(TerminalColor.BLACK, TerminalColor.WHITE);
    //


    @Override
    public TextStyle getSelectionColor() {
        return selectionColor;
    }

    public volatile TextStyle selectionColor = new TextStyle(TerminalColor.WHITE, TerminalColor.rgb(82, 109, 165));


    @Override
    public TextStyle getFoundPatternColor() {
        return foundPatternColor;
    }

    public volatile TextStyle foundPatternColor = new TextStyle(TerminalColor.BLACK, TerminalColor.rgb(255, 255, 0));


    @Override
    public boolean copyOnSelect() {
        return emulateX11CopyPaste();
    }

    @Override
    public boolean pasteOnMiddleMouseClick() {
        return emulateX11CopyPaste();
    }

    @Override
    public boolean emulateX11CopyPaste() {
        return true;
    }

    @Override
    public boolean useAntialiasing() {
        return true;
    }

    @Override
    public int maxRefreshRate() {
        return maxRefreshRate;
    }

    public volatile int maxRefreshRate = 50;


    @Override
    public boolean audibleBell() {
        return true;
    }

    @Override
    public boolean enableMouseReporting() {
        return true;
    }

    @Override
    public int caretBlinkingMs() {
        return caretBlinkingMs;
    }


    public volatile int caretBlinkingMs = 505;


    @Override
    public boolean scrollToBottomOnTyping() {
        return true;
    }

    @Override
    public boolean DECCompatibilityMode() {
        return true;
    }

    @Override
    public boolean forceActionOnMouseReporting() {
        return false;
    }

    @Override
    public int getBufferMaxLinesCount() {
        return LinesBuffer.DEFAULT_MAX_LINES_COUNT;
    }

    @Override
    public boolean altSendsEscape() {
        return false;
    }

    @Override
    public boolean ambiguousCharsAreDoubleWidth() {
        return false;
    }

}
