package com.jpto.idea

import com.intellij.terminal.JBTerminalSystemSettingsProviderBase
import com.jediterm.terminal.HyperlinkStyle;
import net.sf.jremoterun.utilities.JrrClassUtils
import org.jetbrains.plugins.terminal.JBTerminalSystemSettingsProvider

import java.lang.reflect.Field;
import java.util.logging.Logger;
import groovy.transform.CompileStatic;


@CompileStatic
class MyJBTerminalSystemSettingsProvider2 extends JBTerminalSystemSettingsProvider{

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    void copySettingsFrom(JBTerminalSystemSettingsProvider other){
        Object myColorScheme = JrrClassUtils.getFieldValue(this,"myColorScheme")
        JrrClassUtils.setFieldValue(this,"myColorScheme",myColorScheme)



        Object myListeners = JrrClassUtils.getFieldValue(this,"myListeners")
        JrrClassUtils.setFieldValue(this,"myListeners",myListeners)


//        Field myListenersField = com.intellij.terminal.JBTerminalSystemSettingsProviderBase.getField("myListeners")
//        myListenersField.setAccessible(true)
//        def otherValue = myListenersField.get(other)
//        myListenersField.set(this,otherValue)
    }

    @Override
    HyperlinkStyle.HighlightMode getHyperlinkHighlightingMode() {
        return HyperlinkStyle.HighlightMode.ALWAYS;
    }
}
