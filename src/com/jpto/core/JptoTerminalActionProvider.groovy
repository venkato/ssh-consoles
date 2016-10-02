package com.jpto.core

import com.jediterm.terminal.TtyConnector
import com.jediterm.terminal.model.TerminalLine
import com.jediterm.terminal.model.TerminalTextBuffer
import com.jediterm.terminal.ui.JediTermWidget
import com.jediterm.terminal.ui.TerminalAction
import com.jediterm.terminal.ui.TerminalActionProvider
import com.jediterm.terminal.ui.TerminalPanel
import com.jpto.core.concreator.JptoJSchShellTtyConnector
import com.jpto.core.concreator.JptoSshJediTermWidget
import com.jpto.core.concreator.SshConSet
import com.jpto.settings.SshSettings
import groovy.transform.CompileStatic
import io.github.dheid.fontchooser.FontDialog
import net.infonode.docking.DockingWindow
import net.infonode.docking.TabWindow
import net.infonode.docking.View
import net.sf.jremoterun.utilities.JrrUtilities
import net.sf.jremoterun.utilities.nonjdk.idwutils.IdwActions
import net.sf.jremoterun.utilities.nonjdk.idwutils.IdwMoveToNewTab
import net.sf.jremoterun.utilities.nonjdk.idwutils.IdwPopupMenuFactory
import net.sf.jremoterun.utilities.nonjdk.idwutils.IdwShortcuts
import net.sf.jremoterun.utilities.nonjdk.idwutils.IdwUtils
import net.sf.jremoterun.utilities.nonjdk.mucom.OpenFolderInMuCommander
import net.sf.jremoterun.utilities.nonjdk.sshsup.SshConSet2
import net.sf.jremoterun.utilities.nonjdk.swing.JrrSwingUtilsParent
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

import javax.swing.JButton
import javax.swing.JComboBox
import javax.swing.JDialog
import javax.swing.JOptionPane
import javax.swing.JPanel
import javax.swing.SwingUtilities
import javax.swing.WindowConstants
import java.awt.BorderLayout
import java.awt.Component
import java.awt.Container
import java.awt.FlowLayout
import java.awt.Font
import java.awt.Frame
import java.awt.Window
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent

@CompileStatic
public class JptoTerminalActionProvider implements TerminalActionProvider {
    private static final Logger log = LogManager.getLogger();

    public TerminalActionProvider terminalActionProviderNext;

    public JptoTerminalActionProvider() {

    }


    void duplicatePtyConsole(JptoTerminalPanel component, JptoJediPtyTermWidget jedi) {
        JptoTtyConnector ttyConnector = jedi.jSchShellTtyConnector;
        TabWindow parentTabWindow = IdwUtils.getOppositeSplitTab(IdwUtils.getDockerWindowWithPopmenu(component));
        DockingWindow dockingWindow = JrrSwingUtilsParent.findParentWindow(component, DockingWindow)
        View createTerm = SshSettings.customFunctions.createPtyTerminal(dockingWindow.getTitle(), ttyConnector.dir, ttyConnector.cmd);
        parentTabWindow.addTab(createTerm);
    }

    void duplicateSshConsole(JptoTerminalPanel component, JptoSshJediTermWidget jedi) {
        JptoJSchShellTtyConnector ttyConnector = jedi.jSchShellTtyConnector;
        TabWindow parentTabWindow = IdwUtils.getOppositeSplitTab(IdwUtils.getDockerWindowWithPopmenu(component));
        SshConSet sshConSetNew = ttyConnector.host.clone2()
        log.info "duplicated : ${sshConSetNew}"
        View createTerm = JptoAddHostPanel.createTerminal(sshConSetNew);
        parentTabWindow.addTab(createTerm);
    }

    void addNewHost(JptoCommonJediTermWidget jedi) {
        JPanel panel = new JPanel(new FlowLayout())
        List<String> hosts3 = JptoAddHostPanel.getHosts3()
        JComboBox<String> hosts4 = new JComboBox<>(hosts3.toArray(new String[0]));
        panel.add(hosts4)
        JButton button = new JButton("Add host");
        panel.add(button)
        JDialog dialog = new JDialog(JrrSwingUtilsParent.findParentWindow(jedi, Window), "Add host");
        button.addActionListener {
            try {
                TabWindow parentTabWindow = IdwUtils.getOppositeSplitTab(IdwUtils.getDockerWindowWithPopmenu(jedi));
                SshConSet sshConSet = new SshConSet()
                sshConSet.host = (String) hosts4.getSelectedItem();
                View createTerm = JptoAddHostPanel.createTerminal(sshConSet);
                parentTabWindow.addTab(createTerm);
                dialog.dispose()
            } catch (Throwable e) {
                JrrUtilities.showException("Failed add host", e)
            }
        }

        hosts4.addKeyListener(new KeyAdapter() {
            @Override
            void keyReleased(KeyEvent e) {
                int code = e.getKeyCode();
                switch (code) {
                    case KeyEvent.VK_ESCAPE:
                        dialog.dispose()
                        break
                }
            }
        })

        dialog.getContentPane().add(panel, BorderLayout.CENTER)
        dialog.pack();
        dialog.setVisible(true)
        hosts4.requestFocusInWindow()
        hosts4.requestFocus()
    }

    @Override
    public List<TerminalAction> getActions() {
        ArrayList<TerminalAction> actionsAuu3 = new ArrayList<>();
        getActions1(actionsAuu3)
        getActions2(actionsAuu3)
        getActions12(actionsAuu3)
        getActions9(actionsAuu3)
        getActions3(actionsAuu3)
        getActions4(actionsAuu3)
        getActions5(actionsAuu3)
        getActionsOpenInMuCommander(actionsAuu3)
        getChangeFont(actionsAuu3)
        return actionsAuu3

    }

    void getActions1(ArrayList<TerminalAction> actions) {
        actions.add(new JptoTerminalAction(SshKeyStokesShortCuts.duplicate, { Component component ->
            log.info "duplicated called ${component.class.name}"
            if (component instanceof JptoTerminalPanel) {
                Container parent = component.getParent().getParent()
                JptoTerminalPanel term = (JptoTerminalPanel) component;
                if (parent instanceof JptoSshJediTermWidget) {
                    JptoSshJediTermWidget sshWi = (JptoSshJediTermWidget) parent;
                    duplicateSshConsole(term, sshWi)
                    return true;
                }
                if (parent instanceof JptoJediPtyTermWidget) {
                    JptoJediPtyTermWidget pty = (JptoJediPtyTermWidget) parent;
                    duplicatePtyConsole(term, pty)
                    return true;
                }
            }
            return false;
        }));
    }

    void getActions2(ArrayList<TerminalAction> actions) {

        actions.add(new JptoTerminalAction(SshKeyStokesShortCuts.createNewHost, { Component component ->
            log.info "duplicated called ${component.class.name}"
            if (component instanceof JptoTerminalPanel) {
                Container parent = component.getParent().getParent()
                JptoTerminalPanel term = (JptoTerminalPanel) component;
                if (parent instanceof JptoCommonJediTermWidget) {
                    JptoCommonJediTermWidget sshWi = (JptoCommonJediTermWidget) parent;
                    addNewHost(sshWi)
                    return true;
                }
                JOptionPane.showMessageDialog(parent, "Unsupported");
                return true;
            }
            return false;
        }));

    }

    void getActions12(ArrayList<TerminalAction> actions) {
        JptoTerminalAction ta = new JptoTerminalAction(IdwShortcuts.switchSplitLayout,
                { Component input ->
                    return IdwActions.switchSpliptWondowLayout(input)
                })
        actions.add(ta);

    }

    void getActions9(ArrayList<TerminalAction> actions) {
        actions.add(new JptoTerminalAction(IdwShortcuts.focusOppositeTab, { Component input ->
            return IdwActions.splitSwitch(input);
        }));

    }

    void getActions3(ArrayList<TerminalAction> actions) {


        actions.add(new JptoTerminalAction(IdwShortcuts.prevoiusTab,
                { Component input ->
                    return IdwActions.switchToPrevoius(input, false);
                }));



        actions.add(new JptoTerminalAction(IdwShortcuts.nextTab
                , { Component input ->
            return IdwActions.switchToNext(input, false)
        }));
        actions.add(new JptoTerminalAction(IdwShortcuts.prevoiusUpperTab,
                { Component input ->
                    return IdwActions.switchToPrevoius(input, true);
                }));



        actions.add(new JptoTerminalAction(IdwShortcuts.nextUpperTab
                , { Component input ->
            return IdwActions.switchToNext(input, true)
        }));


        actions.add(new JptoTerminalAction(IdwShortcuts.swapTabs,
                { Component input ->
                    return IdwActions.swapTab(IdwUtils.getDockerWindowWithPopmenu(input));
                }));

    }

    void getActions4(ArrayList<TerminalAction> actions) {


        actions.add(new JptoTerminalAction(IdwShortcuts.moveToDialog
                , { Component input ->
            DockingWindow window = IdwUtils.getDockerWindowWithPopmenu(input);
            IdwMoveToNewTab.showMoveToMenu2(window)
            return true
        }));

        actions.add(new JptoTerminalAction(IdwShortcuts.maximaze
                , { Component input ->
            return IdwActions.maximazeIdw(input)
        }));
        actions.add(new JptoTerminalAction(IdwShortcuts.rename
                , { Component input ->
            DockingWindow window = IdwUtils.getDockerWindowWithPopmenu(input);
            IdwPopupMenuFactory.renameWindow(window);
            return true;
        }));

        actions.add(new JptoTerminalAction(IdwShortcuts.moveOppositeTab
                , { Component input ->
            return IdwActions.moveTab(IdwUtils.getDockerWindowWithPopmenu(input))
        }));

        actions.add(new JptoTerminalAction(SshKeyStokesShortCuts.changeColor,
                { Component input ->
                    if (input instanceof JptoTerminalPanel) {
                        JptoTerminalPanel tp = input as JptoTerminalPanel;
                        tp.invertStyle3();
                        return true;
                    } else {
                        JrrUtilities.showException("Not inverted", new Exception("not inverted"))
                        return false
                    }

                }));

        actions.add(new JptoTerminalAction(SshKeyStokesShortCuts.myPassword,
                { Component input ->
                    if (input == null) {
                        log.error("input is null");
                        return false;
                    }
                    JptoTerminalPanel component = (JptoTerminalPanel) input;
                    JediTermWidget jedi = (JediTermWidget) component.getParent().getParent();
                    TtyConnector ttyConnector = jedi.getTtyConnector();
                    if (SshConSet2.defaultPassword == null) {
                        throw new IllegalStateException("Password not set")
                    }
                    ttyConnector.write(SshConSet2.defaultPassword);
                    return true;
                }));

    }

    void getActions5(ArrayList<TerminalAction> actions) {

        if (SshSettings.enableAdoptToFixMsgs) {
            actions.add(new JptoTerminalAction(SshKeyStokesShortCuts.translateFix, { Component component ->
                log.info "translate ${component.class.name}"
                if (component instanceof JptoTerminalPanel) {
                    Container parent = component.getParent().getParent()
                    JptoTerminalPanel term = (JptoTerminalPanel) component;
                    if (parent instanceof JptoCommonJediTermWidget) {
                        JptoCommonJediTermWidget sshWi = (JptoCommonJediTermWidget) parent;
                        boolean valueBefore = sshWi.jptoTerminalStarter.jptoJediEmulator.enableAdoptToFixMsgs
                        valueBefore = (!valueBefore)
                        sshWi.jptoTerminalStarter.jptoJediEmulator.enableAdoptToFixMsgs = valueBefore
                        log.info "after translate fix : ${valueBefore}"
                        return true;
                    } else {
                        log.info "no jedi common"
                    }
                } else {
                    log.info "not jedi terminal"
                }
                return false;
            }));
        }

    }

    void getActionsOpenInMuCommander(ArrayList<TerminalAction> actions) {

        if (OpenFolderInMuCommander.openFolderInMuCommander != null) {
            actions.add(new JptoTerminalAction(SshKeyStokesShortCuts.openInMuCommander, { Component component ->

                log.info "duplicated called ${component.class.name}"
                if (component instanceof JptoTerminalPanel) {
                    Container parent = component.getParent().getParent()
                    JptoTerminalPanel term = (JptoTerminalPanel) component;
                    if (parent instanceof JptoSshJediTermWidget) {
                        JptoSshJediTermWidget sshWi = (JptoSshJediTermWidget) parent;
                        openInMu(term, sshWi)
                        return true;
                    }
                }
                return false;
            }));
        }

    }

    void getChangeFont(ArrayList<TerminalAction> actions) {
        actions.add(new JptoTerminalAction(SshKeyStokesShortCuts.changeFont, { Component component ->
            log.info "select font called ${component.class.name}"
            if (component instanceof JptoTerminalPanel) {
                Container parent = component.getParent().getParent()
                JptoTerminalPanel term = (JptoTerminalPanel) component;
                FontDialog dialog = new FontDialog((Frame) null, "Set console font", true);
                dialog.setSelectedFont(term.createFont())
                dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                dialog.setVisible(true);
                if (dialog.isCancelSelected()) {
                    log.info "user pressed cancel"
                } else {
                    Font selectedFont = dialog.getSelectedFont()
                    log.info "selectedFont = ${selectedFont}"
                    term.setCustomFont2(selectedFont)
                    SwingUtilities.invokeLater {
                        term.updateUI()
                    }
                }
            }
            return false;

        }));

    }

    void openInMu(JptoTerminalPanel term, JptoSshJediTermWidget sshWi) {
        sendPwdCommand(term, sshWi)
    }

    public static long pwdSleep = 1000

    void sendPwdCommand(JptoTerminalPanel term, JptoSshJediTermWidget sshWi) {
        TtyConnector ttyConnector = sshWi.getTtyConnector();
        ttyConnector.write("pwd\n");
//        TerminalLine line = term.getLastLine()
//        assert line!=null
//        String text = line.getText()
        Runnable r = {
            Thread.sleep(pwdSleep)
            SwingUtilities.invokeLater {
                openPwd(term, sshWi)
            }
        }
        Thread thread = new Thread(r, "pwd opener")
        thread.start()
//        if(!text.endsWith('pwd')){
//            throw new Exception("not ends with pwd")
//        }
//        ttyConnector.write("\n");

    }

    void openPwd(JptoTerminalPanel term, JptoSshJediTermWidget sshWi) {
        String dir = getPwd(term)
        String host = sshWi.jSchShellTtyConnector.host.host
        log.info "host = ${host} , dir = ${dir}"
        OpenFolderInMuCommander.openFolderInMuCommander.openInMuCommander(host, dir)
    }

    String getPwd(JptoTerminalPanel term) {
        TerminalPanel.TerminalCursor cursor = term.getTerminalCursor()
        int cursor2 = cursor.getCoordY()
        log.info "cursor = ${cursor2}"
        TerminalTextBuffer textBuffer = term.getTerminalTextBuffer();
        if (cursor2 < 2) {
            throw new RuntimeException("bad cursor : " + cursor2)
        }
        TerminalLine n2 = textBuffer.getLine(cursor2 - 2)

        String n2t = n2.getText()
        log.info "n2t = ${n2t}"
        if (n2t.endsWith('pwd')) {
            TerminalLine n1 = textBuffer.getLine(cursor2 - 1)
            String n1t = n1.getText()
            log.info "n1t = ${n1t}"
            return n1t
        }
        TerminalLine n3 = textBuffer.getLine(cursor2 - 3)
        String n3t = n3.getText()
        log.info "n3t = ${n3t}"
        if (n3t.endsWith('pwd')) {
            return n2t
        }
        throw new Exception("not found ${n3t} , ${n2t} ")
    }

//        for (int i = 1; i <= 9; i++) {
//            final int newSelect = i - 1;
//            actions.add(new JptoTerminalAction("Select " + i,
//                    KeyStroke.getKeyStroke(KeyEvent.VK_0 + i, InputEvent.CTRL_DOWN_MASK),
//                    { Component input ->
//                        TabWindow parentTabWindow = getParentIdwWindowSpecial3(input, TabWindow);
//                        if (parentTabWindow == null) {
//                            log.info("can't find parent tab ${input}");
//                            return false;
//                        }
//                        TabbedPanel tabbedPanel = getTabbedPanel(parentTabWindow);
//                        if (newSelect > tabbedPanel.getTabCount() - 1) {
//                            log.info("up conner");
//                            return false;
//                        }
//                        parentTabWindow.setSelectedTab(newSelect);
//                        selectLastFocusedChild(parentTabWindow.getChildWindow(newSelect));
//                        return true;
//
//                    }));
//        }


    @Override
    public TerminalActionProvider getNextProvider() {
        return terminalActionProviderNext;
    }

    @Override
    public void setNextProvider(TerminalActionProvider paramTerminalActionProvider) {
        terminalActionProviderNext = paramTerminalActionProvider;

    }

}