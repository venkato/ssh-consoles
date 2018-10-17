package com.jpto.core

import com.jpto.core.JptoTerminalPanel
import com.jpto.core.concreator.JptoSshJediTermWidget
import groovy.transform.CompileStatic
import net.infonode.docking.View;
import net.sf.jremoterun.utilities.JrrClassUtils

import javax.swing.Icon
import java.awt.Component;
import java.util.logging.Logger;

@CompileStatic
class ViewJptoConnectionCreator extends View{
    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    JptoCommonJediTermWidget jediTermWidget;

    ViewJptoConnectionCreator(String title, Icon icon, JptoCommonJediTermWidget jediTermWidget) {
        super(title, icon, jediTermWidget)
        this.jediTermWidget = jediTermWidget
    }

    @Override
    void requestFocus() {
        JptoTerminalPanel terminalPanel = jediTermWidget.getJptoTerminalPanel();
        if (terminalPanel == null) {

        } else {
            terminalPanel.requestFocus();
        }
    }

    @Override
    boolean requestFocusInWindow() {
        JptoTerminalPanel terminalPanel = jediTermWidget.getJptoTerminalPanel();
        if (terminalPanel == null) {
            return false
        }
        return terminalPanel.requestFocusInWindow();
    }
}
