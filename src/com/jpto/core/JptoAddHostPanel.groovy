package com.jpto.core

import com.jediterm.terminal.ui.TerminalSession
import com.jpto.core.concreator.JptoAddHostPanel2
import com.jpto.core.concreator.JptoJSchShellTtyConnector
import com.jpto.core.concreator.JptoSshJediTermWidget
import com.jpto.core.concreator.SshConSet
import com.jpto.settings.SshSettings
import groovy.transform.CompileStatic
import net.infonode.docking.TabWindow
import net.infonode.docking.View
import net.sf.jremoterun.utilities.nonjdk.idwutils.IdwActions
import net.sf.jremoterun.utilities.nonjdk.idwutils.ViewAndPanel
import net.sf.jremoterun.utilities.nonjdk.sshsup.SshConnectionDetailsI
import net.sf.jremoterun.utilities.nonjdk.swing.JrrSwingUtils
import org.apache.log4j.Logger

import javax.swing.DefaultComboBoxModel
import javax.swing.JButton
import javax.swing.JComboBox
import javax.swing.JPanel
import javax.swing.JTextField
import javax.swing.KeyStroke
import java.awt.FlowLayout
import java.awt.event.InputEvent
import java.awt.event.KeyEvent

@CompileStatic
public class JptoAddHostPanel {
    private static final Logger logger = Logger.getLogger(JptoAddHostPanel);
    private static final Logger log = logger;

    public static KeyStroke detailedEditorShortCut = KeyStroke.getKeyStroke(KeyEvent.VK_SPACE,
            InputEvent.CTRL_DOWN_MASK);

    /**
     * Used by 'Add host'
     */
    public static TabWindow tabWindow = new TabWindow();
    public static JComboBox<String> hosts = new JComboBox<String>();


    static List<String> getHosts3() {
        List<String> res = []
        int count = hosts.getItemCount()
        for (int i = 0; i < count; i++) {
            res.add hosts.getItemAt(i)
        }
        return res;
    }


    static void addHost(String host) {
        List<String> res = getHosts3()
        res.add(host)
        res = res.unique()
        DefaultComboBoxModel<String> model = hosts.getModel() as DefaultComboBoxModel<String>
        model.removeAllElements()
        res.each { model.addElement(it) }
    }


    /**
     * @return panel with FlowLayout
     */
    static ViewAndPanel getAddHostPanelView2() {
        JPanel panel = JptoAddHostPanel.getAddHostPanel();
        ViewAndPanel viewAndPanel = new ViewAndPanel("Add host",panel)
        JrrSwingUtils.tranferFocus(viewAndPanel.view, panel)
        return viewAndPanel
    }

    @Deprecated
    static View getAddHostPanelView() {
        return getAddHostPanelView2().view
    }

    public static JPanel getAddHostPanel() {
        JTextField textField = new JTextField(SshSettings.addHostNameTempalate);
        textField.setColumns(20)
        JPanel panel = new JPanel(new FlowLayout());
        panel.add(textField);

        JButton addHost = new JButton("add host");
        panel.add(addHost);
        addHost.addActionListener {
            View createTerminalVar;
            try {
//                SshConSet sshConSet = new SshConSet()
//                sshConSet.host = textField.getText()
                SshConnectionDetailsI sshConSet = SshSettings.customFunctions.createSshConnectionFromSidePanel(textField.getText())
                createTerminalVar = createTerminal(sshConSet);
                tabWindow.addTab(createTerminalVar);
            } catch (Exception e1) {
                log.warn(null, e1);
                net.sf.jremoterun.utilities.JrrUtilitiesShowE.showException("can't connect to " + textField.getText(), e1);
            }
        };
        panel.add(hosts)
        JButton addHost2 = new JButton("add host from selected");
        panel.add(addHost2);
        addHost2.addActionListener {
            View createTerminalVar;
            try {
                SshConSet sshConSet = new SshConSet();
                sshConSet.host = hosts.getSelectedItem() as String
//                sshConSet.password = SshConSet2.defaultPassword
                createTerminalVar = createTerminal(sshConSet);
                tabWindow.addTab(createTerminalVar);
            } catch (Exception e1) {
                log.warn(null, e1);
                net.sf.jremoterun.utilities.JrrUtilitiesShowE.showException("can't connect to " + textField.getText(), e1);
            }
        };

        SshSettings.customFunctions.toString();
        // SshSettings.customFunctions.addCustomButtons(panel);
        IdwActions.regiteterKeyStokes(panel)
        JrrSwingUtils.tranferFocus(panel, textField)
        return panel;
    }


//    static JptoSshJediTermWidget createTerminalImpl(SshConSet host, boolean throwEx) throws Exception {
//        host.host = StringUtils.strip(host.host, "\t\r\n ");
//        return createTerminalImpl((SshConSet2I)host,throwEx)
//    }

    static JptoSshJediTermWidget createTerminalImpl(SshConnectionDetailsI host, boolean throwEx) throws Exception {
        JptoJSchShellTtyConnector jSchShellTtyConnector = SshSettings.customFunctions.buildJSchShellTtyConnector(host);
        return createTerminalImpl2(jSchShellTtyConnector, throwEx)
    }

    static JptoSshJediTermWidget createTerminalImpl2(JptoJSchShellTtyConnector jSchShellTtyConnector, boolean throwEx) throws Exception {
        JptoSshJediTermWidget jediTermWidget = createTerminalImpl3(jSchShellTtyConnector)
        if (!jSchShellTtyConnector.initDone) {
            log.info("Logon failed, for details change ssh log level : " + SshSettings.getName() + ".logLevel");
            if (throwEx) {
                throw new Exception("Ssh connection failed to initialized");
            }
        }
        SshSessionStateMonitor.sessionStateMonitor.addSession(jediTermWidget)
        return jediTermWidget;

    }


    static JptoJediPtyTermWidget createLocalTerminal(List<String> cmd, File dir) throws Exception {
//        assert JptoAddHostPanel.classLoader == ClassLoader.getSystemClassLoader()
        if (dir != null) {
            assert dir.exists()
            assert dir.isDirectory()
        }
        JptoTtyConnector jSchShellTtyConnector = new JptoTtyConnector(cmd, dir);
        JptoJediPtyTermWidget jediTermWidget = new JptoJediPtyTermWidget(SshSettings.defaultSettingsProvider,
                jSchShellTtyConnector);
        JptoTerminalActionProvider myTerminalActionProvider = new JptoTerminalActionProvider();
        TerminalSession createTerminalSession = jediTermWidget.createTerminalSession(jSchShellTtyConnector);
        createTerminalSession.start();
        myTerminalActionProvider.setNextProvider(jediTermWidget.getNextProvider());
        jediTermWidget.setNextProvider(myTerminalActionProvider);
        return jediTermWidget;
    }

    public static View createTerminal(SshConnectionDetailsI host) throws Exception {
        return SshSettings.customFunctions.createTerminal(host);
    }

    static JptoSshJediTermWidget createTerminalImpl3(JptoJSchShellTtyConnector jSchShellTtyConnector) throws Exception {
        JptoSshJediTermWidget jediTermWidget = JptoAddHostPanel2.createTerminalImpl3(jSchShellTtyConnector)
        JptoTerminalActionProvider myTerminalActionProvider = new JptoTerminalActionProvider();
        myTerminalActionProvider.setNextProvider(jediTermWidget.getNextProvider());
        jediTermWidget.setNextProvider(myTerminalActionProvider);
        return jediTermWidget
    }

}
