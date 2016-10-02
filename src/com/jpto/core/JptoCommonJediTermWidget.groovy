package com.jpto.core

import com.google.common.base.Predicate
import com.google.common.collect.Lists
import com.jediterm.terminal.TerminalMode
import com.jediterm.terminal.TerminalStarter
import com.jediterm.terminal.TtyBasedArrayDataStream
import com.jediterm.terminal.TtyConnector
import com.jediterm.terminal.model.JediTerminal
import com.jediterm.terminal.model.TerminalTextBuffer
import com.jediterm.terminal.ui.JediTermWidget
import com.jediterm.terminal.ui.TerminalAction
import com.jediterm.terminal.ui.settings.SettingsProvider
import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.JrrClassUtils

import javax.swing.JPanel
import javax.swing.JScrollBar
import java.awt.event.KeyEvent
import java.lang.management.ManagementFactory
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.util.logging.Logger

@CompileStatic
class JptoCommonJediTermWidget extends JediTermWidget {

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    static JediLogger jediLogger = new JediLogger(TerminalTextBuffer.getName())
    static JediLogger jediLogger2 = new JediLogger(TerminalMode.getName())

    public JptoTerminalStarter jptoTerminalStarter;

    static {
        JrrClassUtils.setFieldValue(TerminalTextBuffer, "LOG", jediLogger)

        // why need this ?
        JrrClassUtils.setFieldValue(TerminalMode, "LOG", jediLogger2)
    }

    static Field searchComponntField
    static Method showFindComponentMethod

    JptoCommonJediTermWidget(SettingsProvider settingsProvider) {
        super(settingsProvider)
        if(searchComponntField==null){
//            JrrClassUtils.findField(Class,"useCaches")
//            JrrClassUtils.findField(ManagementFactory,"CLASS_LOADING_MXBEAN_NAME")
//            Class cl =JediTermWidget
            searchComponntField = JrrClassUtils.findField(JediTermWidget,"myFindComponent")
            showFindComponentMethod = JrrClassUtils.findMethodByCount(JediTermWidget,"showFindText",0)
        }
//        addHyperlinkFilter(new HyperLinkFilter1())

    }

    @Override
    protected TerminalStarter createTerminalStarter(JediTerminal terminal, TtyConnector connector) {
        jptoTerminalStarter = new JptoTerminalStarter(terminal, connector,new TtyBasedArrayDataStream(connector));
        return jptoTerminalStarter
    }

    public JptoTerminalPanel getJptoTerminalPanel() {
        return (JptoTerminalPanel) myTerminalPanel;
    }

    @Override
    void requestFocus() {
        if(myTerminalPanel!=null) {
            myTerminalPanel.requestFocus()
        }
    }

    @Override
    boolean requestFocusInWindow() {
        if(myTerminalPanel==null){
            return false
        }
        return myTerminalPanel.requestFocusInWindow()
    }
//    void setCustomLogger(TerminalTextBuffer terminalTextBuffer){   }

    JScrollBar getJScrollBar(){
        return myScrollBar;
    }

    @Override
    List<TerminalAction> getActions() {
        return Lists.newArrayList(new TerminalAction("Find", mySettingsProvider.getFindKeyStrokes(),
                new Predicate<KeyEvent>() {
                    @Override
                    public boolean apply(KeyEvent input) {
                        showFindText2();
                        return true;
                    }
                }).withMnemonicKey(KeyEvent.VK_F));
    }

    void showFindText2(){
        JPanel seachPanel= searchComponntField.get(this) as JPanel;
        log.info("cp ${seachPanel == null}")
        if(seachPanel ==null){
            showFindComponentMethod.invoke(this)
        }else{
            seachPanel.requestFocusInWindow()
            seachPanel.requestFocus()

        }
    }
}
