package com.jpto.core

import com.jediterm.terminal.DefaultTerminalCopyPasteHandler
import groovy.transform.CompileStatic;
import net.sf.jremoterun.utilities.JrrClassUtils
import org.apache.commons.lang3.StringUtils

import javax.swing.JOptionPane
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.StringSelection;
import java.util.logging.Logger;

@CompileStatic
class JptoTerminalCopyPasteHandler extends DefaultTerminalCopyPasteHandler {
    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    void setContentsSuper(String text, boolean useSystemSelectionClipboardIfAvailable) {
        super.setContents(text, useSystemSelectionClipboardIfAvailable);
    }

    @Override
    void setContents(String text, boolean useSystemSelectionClipboardIfAvailable) {
        try {
            String text4 = cleanupString4(text)
            if (text4 != null) {
                super.setContents(text4, useSystemSelectionClipboardIfAvailable)
            }
        } catch (Exception e) {
            log.warn(null, e);
        }
    }

//    @Override
//    public void setCopyContents(StringSelection selection2) {
//        try {
//            StringSelection stringSelection4 = cleanupString(selection2)
//            if (stringSelection4!=null) {
//                super.setCopyContents(stringSelection4);
//            }
//        } catch (Exception e) {
//            log.warn(null, e);
//        }
//    }

    String getContentsSuper(boolean useSystemSelectionClipboardIfAvailable) {
        return super.getContents(useSystemSelectionClipboardIfAvailable);
    }

    @Override
    String getContents(boolean useSystemSelectionClipboardIfAvailable) {
        String superR = super.getContents(useSystemSelectionClipboardIfAvailable);

        return superR;
    }


    String cleanupString4(String selection) {
        //String selection = (String) selection2.getTransferData(DataFlavor.stringFlavor);
        String selection3 = cleanupString(selection);
        if (selection3.isEmpty()) {
            log.debug("try to copy empty string, skip");
            return null;
        }
        //StringSelection stringSelection3 = new StringSelection(selection3);
        return selection3;
    }

    String cleanupString(String selection) {
        selection = StringUtils.stripStart(selection, "\r\n\t ");
        selection = StringUtils.stripEnd(selection, "\r\n\t ");
        selection = selection.trim();

        return selection
    }


//    protected void setCopySelectionContents(StringSelection selection2) {
//        try {
//            StringSelection stringSelection4 = cleanupString(selection2)
//            if (stringSelection4!=null) {
//                super.setCopySelectionContents(stringSelection4);
//            }
//        } catch (Exception e) {
//            log.warn(null, e);
//        }
//    }

}
