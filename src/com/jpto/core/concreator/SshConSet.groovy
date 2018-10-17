package com.jpto.core.concreator

import com.jcraft.jsch.Session
import net.sf.jremoterun.utilities.nonjdk.sshsup.SshConSet3
import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.JrrClassUtils

import java.util.logging.Logger

@CompileStatic
class SshConSet extends SshConSet3 implements Closeable {

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();


    public JptoJSchShellTtyConnector jcraftConn

//    public SftpUtils sftpUtils

    void close() {
        super.close()
        if(jcraftConn!=null) {
            jcraftConn.close()
        }
    }

    SshConSet clone2(){
        SshConSet clone9 = new SshConSet()
        cloneTo(clone9)
        return clone9
    }


    public Session getJcraftSession() {
        assert jcraftConn != null;
        Session session = jcraftConn.session
        assert session != null
        return session
    }

    void checkConnectAndCreateIfNeeded() {
        if (jcraftConn == null) {
            createJcraftConnection()
        } else {
            if (jcraftConn.isConnected()) {

            } else {
                log.info "not connected, reconnecting to ${host} ${user}"
                jcraftConn = null
                checkConnectAndCreateIfNeeded()
                if (sftpUtils != null) {
                    if (sftpUtils.sftp != null) {
                        sftpUtils.sftp = null
                        createJcrftSftp(sftpUtils)
                    }
                }

            }
        }
        super.checkConnectAndCreateIfNeeded()
    }

    JptoJSchShellTtyConnector createJcraftConnection() {
        createJcraftConnection2(true)
    }

    JptoJSchShellTtyConnector createJcraftConnection2(boolean doIsConnectedCheck) {
        if (jcraftConn != null) {
            return jcraftConn;
        }
        if (sshKey != null) {
            assert sshKey.exists()
        } else {
            if(passwordReceiver==null || !passwordReceiver.isPasswordSet()) {
                throw new Exception("set password or public key")
            }
        }
        assert host != null
        SshConSet thisObj = this;
        JptoJSchShellTtyConnector connector = new JptoJSchShellTtyConnector(thisObj);
        JptoAddHostPanel2.createTerminalImpl3(connector);
        if (doIsConnectedCheck&& !connector.initDone) {
            connector.checkIfConnected()
        }
        jcraftConn = connector
        return connector
    }


}
