package com.jpto.core

import com.jcraft.jsch.JSch
import com.jcraft.jsch.Session
import com.jediterm.terminal.ui.TerminalPanel
import com.jpto.core.concreator.JptoJSchShellTtyConnector
import com.jpto.core.concreator.JptoSshJediTermWidget
import com.jpto.core.concreator.SshConSet
import com.jpto.settings.SshSettings
import groovy.transform.CompileStatic
import net.infonode.docking.DockingWindow
import net.infonode.docking.View
import net.sf.jremoterun.utilities.JrrUtilitiesShowE
import net.sf.jremoterun.utilities.nonjdk.downloadutils.LessDownloader
import net.sf.jremoterun.utilities.nonjdk.sshsup.ConnectionState
import net.sf.jremoterun.utilities.nonjdk.sshsup.JrrJschSession
import net.sf.jremoterun.utilities.nonjdk.sshsup.JscpLogger
import net.sf.jremoterun.utilities.nonjdk.sshsup.SshConnectionDetailsI
import net.sf.jremoterun.utilities.nonjdk.swing.JrrSwingUtils
import org.apache.log4j.Logger

import javax.swing.JPanel
import javax.swing.SwingUtilities

@CompileStatic
public abstract class JptoCustomFunctions {

    private static final Logger logger = Logger.getLogger(JptoCustomFunctions);
    private static final Logger log = logger;

    JptoCustomFunctions() {
        setJschLogger()
    }

    void setJschLogger() {
        JSch.setLogger(new JscpLogger(SshSettings.logLevel));
    }


    public void addCustomButtons(JPanel panel) {

    }

    public void putCustomCommadnAfterLogin(JptoJSchShellTtyConnector myJSchShellTtyConnector) throws IOException {

    }

    public JptoJSchShellTtyConnector buildJSchShellTtyConnector(SshConnectionDetailsI host) {
        JptoJSchShellTtyConnector jSchShellTtyConnector = new JptoJSchShellTtyConnector(host);
        return jSchShellTtyConnector;
    }

    public View createTerminal(String host) throws Exception {
        SshConSet sshConSet = new SshConSet()
        sshConSet.host = host;
        return createTerminal(sshConSet)
    }

    public int dumpSessionParamsRotateCount = 9;
    public File dumpSessionParamsDir;
    public String dumpSshFileNameSuffix = '_jcraft.json'

    public View createTerminal(SshConnectionDetailsI host) throws Exception {
        //host.host = StringUtils.strip(host.host, "\t\r\n ");
        JptoJSchShellTtyConnector jSchShellTtyConnector = SshSettings.customFunctions.buildJSchShellTtyConnector(host);
        View terminal2 = createTerminal2(jSchShellTtyConnector)
        if (dumpSessionParamsDir != null && jSchShellTtyConnector.sessionCatched != null) {
            try {
                jSchShellTtyConnector.sessionCatched.dumpMainParamsToFile(dumpSessionParamsDir, dumpSshFileNameSuffix, dumpSessionParamsRotateCount)
            } catch (Exception e) {
                log.warn("${host.host} failed dump params for", e)
                JrrUtilitiesShowE.showException("${host.host} failed dump params for", e)
            }
        }
        JptoAddHostPanel.addHost(host.host)
        return terminal2
    }

    String deriveViewTitle(JptoJSchShellTtyConnector jSchShellTtyConnector) {
        String host = jSchShellTtyConnector.host.host
        final String humanName
        int dot = host.indexOf('.')
        if (dot == -1 || dot == 0) {
            humanName = host
        } else {
            String humanName2 = host.substring(0, dot)
            if (humanName2.isInteger()) {
                humanName = host
            } else {
                humanName = humanName2
            }
        }
        return humanName
    }

    View createTerminal2(JptoJSchShellTtyConnector jSchShellTtyConnector) throws Exception {
        JptoSshJediTermWidget jediTermWidget = JptoAddHostPanel.createTerminalImpl2(jSchShellTtyConnector, false);
        boolean authOk = false;
        Session session = jSchShellTtyConnector.getSession()
        if (session instanceof JrrJschSession) {
            JrrJschSession session3 = (JrrJschSession) session;
            authOk = (session3.connectionState == ConnectionState.AuthPassed)
        };

        if (authOk && jediTermWidget.jSchShellTtyConnector.initDone) {
            Thread.sleep(500);
            SwingUtilities.invokeLater {
                TerminalPanel terminalPanel = jediTermWidget.getTerminalPanel();
                terminalPanel.clearBuffer()
//                try {
//                    JrrClassUtils.invokeJavaMethod(terminalPanel, "clearBuffer");
//                } catch (Exception e) {
//                    log.warn(null, e);
//                    net.sf.jremoterun.utilities.JrrUtilitiesShowE.showException("e", e);
//                }
            }
        }
        View view = new ViewJptoConnectionCreator(deriveViewTitle(jSchShellTtyConnector), null, jediTermWidget);
//        jediTermWidget.addHyperlinkFilter(new HyperLinkFilter1())
        JrrSwingUtils.tranferFocus(view, jediTermWidget.terminalPanel)
        return view;
    }

    public View createPtyTerminal(File dir, List<String> cmd) throws Exception {
        createPtyTerminal(cmd[0], dir, cmd)
    }


    public View createPtyTerminalLogViewer(LogViewerI logViewerI) throws Exception {
        createPtyTerminalLogViewer(logViewerI.name(), logViewerI.file);
    }

    public View createPtyTerminalLogViewer(String viewName, File logFile) throws Exception {
        assert logFile.exists()
        if (LessDownloader.lessViewer == null) {
            throw new Exception("Define less viewer")
        }
        if (!LessDownloader.lessViewer.exists()) {
            throw new FileNotFoundException("${LessDownloader.lessViewer}")
        }
        return createPtyTerminal(viewName, logFile.parentFile, [LessDownloader.lessViewer.absolutePath, logFile.absolutePath.replace('/', '/')])
    }

    public View createCmdTerminal(String viewName, File dir) throws Exception {
        return createPtyTerminal(viewName, dir, [SshSettings.nativeShellCmd])
    }

    public View createPtyTerminal(String viewName, File dir, List<String> cmd) throws Exception {
//        assert JptoCustomFunctions.classLoader == ClassLoader.getSystemClassLoader()
        JptoJediPtyTermWidget jediTermWidget = JptoAddHostPanel.createLocalTerminal(cmd, dir);
        View view = new ViewJptoConnectionCreator(viewName, null, jediTermWidget);
        JrrSwingUtils.tranferFocus(view, jediTermWidget.terminalPanel)
        return view;
    }

    public abstract DockingWindow initHosts() throws Exception;

    void configureSshSession(Session session) {

    }

    SshConnectionDetailsI createSshConnectionFromSidePanel(String host1) {
        SshConSet sshConSet=new SshConSet()
        sshConSet.host = host1
        return sshConSet
    }
}
