package com.jpto.core

import com.jpto.core.concreator.JptoJSchShellTtyConnector
import com.jpto.core.concreator.SshConSet
import com.jpto.settings.SshSettings
import groovy.transform.CompileStatic
import net.infonode.docking.View
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.nonjdk.InfocationFrameworkStructure
import net.sf.jremoterun.utilities.nonjdk.rstarunner.RstaJavaEditor
import net.sf.jremoterun.utilities.nonjdk.rstarunner.RstaRunnerWithStackTrace2
import net.sf.jremoterun.utilities.nonjdk.tcpmon.Tcpmon
import net.sf.jremoterun.utilities.nonjdk.tcpmon.webclient.WebClient
import net.sf.jremoterun.utilities.nonjdk.vncviewer.VncViewer
import net.sf.jremoterun.utilities.nonjdk.weirdx.WeirdxDownloader
import org.apache.commons.lang3.SystemUtils
import org.apache.sshd.server.SshServer
import org.apache.sshd.server.auth.password.PasswordAuthenticator
import org.apache.sshd.server.auth.password.PasswordChangeRequiredException
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider
import org.apache.sshd.server.scp.ScpCommandFactory
import org.apache.sshd.server.session.ServerSession
import org.apache.sshd.server.shell.ProcessShellFactory

import java.util.logging.Logger

@CompileStatic
class AddHostFunctions {

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    public File userHome = System.getProperty('user.home') as File

    void addCmdViewer() {
        addCmdViewer(new File('.'))
    }

    View addCmdViewer(File file) {
        View view = SshSettings.customFunctions.createCmdTerminal(SshSettings.nativeShellCmd, file)
        com.jpto.core.JptoAddHostPanel.tabWindow.addTab(view)
        return view
    }


    View addCmdViewer(File file, String viewName) {
        View view = SshSettings.customFunctions.createCmdTerminal(viewName, file)
        JptoAddHostPanel.tabWindow.addTab(view)
        return view
    }

    View addLogViewer(File file) {
        return addLogViewer(file, file.name);
    }

    View addLogViewer(File file, String viewName) {
        View view = SshSettings.customFunctions.createPtyTerminalLogViewer(viewName, file)
        JptoAddHostPanel.tabWindow.addTab(view)
        return view;
    }

    View addHost(SshConSet sshConSet) {
        JptoJSchShellTtyConnector connection = sshConSet.createJcraftConnection()
        View view = SshSettings.customFunctions.createTerminal2(connection)
        JptoAddHostPanel.tabWindow.addTab(view)
        return view
    }

    RstaRunnerWithStackTrace2 addEditGroovyRunnerFile(File f) {
        RstaRunnerWithStackTrace2 rstaRunnerWithStackTrace2 = new RstaRunnerWithStackTrace2(f);
        JptoAddHostPanel.tabWindow.addTab(rstaRunnerWithStackTrace2.mainPanel3)
        return rstaRunnerWithStackTrace2
    }

    RstaJavaEditor addEditJavaFile(File f) {
        RstaJavaEditor rstaRunnerWithStackTrace2 = new RstaJavaEditor(f);
        JptoAddHostPanel.tabWindow.addTab(rstaRunnerWithStackTrace2.view)
        return rstaRunnerWithStackTrace2
    }


    Tcpmon addTcpMon() {
        File f = new File(InfocationFrameworkStructure.ifDir, 'resources/tcpmon/Sam.groovy')
        Tcpmon tcpmon = new Tcpmon(f);
        JptoAddHostPanel.tabWindow.addTab(tcpmon.notebook)
        return tcpmon
    }

    void runXserver(){
        WeirdxDownloader.run([])
    }

    void runVncViewer(){
        VncViewer.run([])
    }

    WebClient runWebClient(){
        WebClient webClient = new WebClient(JptoAddHostPanel.tabWindow);
        JptoAddHostPanel.tabWindow.addTab(webClient.infodockView);
        return webClient;
    }

//    void runPortForward(Params params){
////        Params params = new Params(2221, "127.0.0.1", 2142);
//        net.kanstren.tcptunnel.Main main = new net.kanstren.tcptunnel.Main(params);
//        main.start();
//        Thread.sleep(10_000)
//        main.stop()
//
//    }

    SshServer runSshServer(int port,List<String> shell){
        SshServer sshd = SshServer.setUpDefaultServer();
        File hostKey = new File(SystemUtils.userHome, "jrr/hostkey.ser")
        hostKey.parentFile.mkdir()
        assert hostKey.parentFile.exists()
        sshd.setKeyPairProvider(new SimpleGeneratorHostKeyProvider(hostKey));
        sshd.setShellFactory(new ProcessShellFactory(shell));
        sshd.setCommandFactory(new ScpCommandFactory());
        sshd.setPasswordAuthenticator(new PasswordAuthenticator(){
            @Override
            boolean authenticate(String username, String password, ServerSession session) throws PasswordChangeRequiredException {
                return true
            }
        });
        sshd.start();
        log.info "ssh server started"
        return sshd
    }



}
