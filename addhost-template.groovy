import com.jcraft.jsch.Session
import com.jpto.core.AddHostFunctions
import com.jpto.core.JptoAddHostPanel
import com.jpto.core.concreator.JptoJSchShellTtyConnector
import com.jpto.core.concreator.SshConSet
import com.jpto.settings.SshSettings;
import groovy.transform.CompileStatic
import net.infonode.docking.View;
import net.sf.jremoterun.utilities.nonjdk.rstarunner.RstaScriptHelper
import net.sf.jremoterun.utilities.nonjdk.sshsup.SshConSet2
import net.sf.jremoterun.utilities.nonjdk.sshsup.SshConnectionDetailsI
import net.sf.jremoterun.utilities.nonjdk.sshsup.SshPasswordReceiverI;

@CompileStatic
class AddHost extends RstaScriptHelper {

    AddHostFunctions f = new AddHostFunctions();

    @Override
    void r() {
        // f.addHost('b');
        // f.addEditGroovyRunnerFile new File(f.userHome,'jrr/configs/addhost.groovy');
        // f.addCmdViewer();
        addSshConnection2();
    }

    void addSshConnection2(){
        SshConSet sshConSet = new SshConSet();
        sshConSet.host = "";
        sshConSet.passwordReceiver = new SshPasswordReceiverI() {
            @Override
            String readPassword(SshConnectionDetailsI sett) {
                return 'myPassword'
            }

            @Override
            boolean isPasswordSet() {
                return true
            }
        };
        sshConSet.sshKey = "keyFile" as File;
        f.addHost(sshConSet)
    }

}