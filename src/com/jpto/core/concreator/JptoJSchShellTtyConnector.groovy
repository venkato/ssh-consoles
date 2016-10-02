package com.jpto.core.concreator


import net.sf.jremoterun.utilities.JrrClassUtils
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jediterm.ssh.jsch.JSchShellTtyConnector;
import com.jediterm.terminal.Questioner;
import com.jpto.settings.SshSettings;
import groovy.transform.CompileStatic;

@CompileStatic
public class JptoJSchShellTtyConnector extends JSchShellTtyConnector {
	private static final Logger log = LogManager.getLogger();

	public SshConSet host;

	public volatile boolean initDone = false;
	public volatile boolean sessionDead = false;

	public JSch jsch
//	SftpUtils sftpUtils;
	Thread connectingThread;

	JptoJSchShellTtyConnector(SshConSet sshConSet) {
		super(sshConSet.host, sshConSet.port, sshConSet.user, sshConSet.password)
		this.host = sshConSet;
	}

	@Override
	String toString() {
		return "JptoJSchShellTtyConnector : ${host.host}"
	}

	@Override
	protected void configureJSch(JSch jsch) throws JSchException {
		this.jsch = jsch
		defaultAddKnownHosts()
		defaultPrivateKey()
		super.configureJSch(jsch);
	}


	void defaultAddKnownHosts(){
		if (host.knownHosts!=null) {
			jsch.setKnownHosts(host.knownHosts.absolutePath);
		}
	}

	void defaultPrivateKey(){
		if (host.sshKey!=null) {
			jsch.addIdentity(host.sshKey.absolutePath);
		}
	}

	Session getSession(){
		return JrrClassUtils.getFieldValue(this,'mySession') as Session;
	}

	@Override
	protected void configureSession(Session session, Properties config) throws JSchException {
		// session.setConfig("PreferredAuthentications", "password");
		// disable known host auth
		defaultConfigureSession(session,config)
		super.configureSession(session, config);
	}

	void defaultConfigureSession(Session session, Properties config){
		config.put("StrictHostKeyChecking", "no");
		if (host.sshKey == null) {
			session.setConfig("PreferredAuthentications", "password");
		} else {
			session.setConfig("PreferredAuthentications", "publickey");
		}
		if(SshSettings.customFunctions!=null) {
			SshSettings.customFunctions.configureSshSession(session)
		}
	}

	public Object initDonelock = new Object();

	@Override
	public boolean init(Questioner q) {
		log.info("connecting to " + host);
		connectingThread = Thread.currentThread()
		try {
			boolean res = super.init(q);
			log.info "${host} connected ? ${res}"
			if (res) {
				try {
					putCustomCommadnAfterLogin()
					synchronized (initDonelock) {
						initDone = true;
						initDonelock.notifyAll();
					}
					log.info("connected to " + host);
				} catch (IOException e) {
					log.warn("", e);
					return false;
				}
			}
			synchronized (initDonelock) {
				initDonelock.notifyAll();
			}
			return res;
		}catch (Throwable e){
			log.info "failed connect to ${host} ${e}"
//			e.printStackTrace()
			throw e;
		}
	}

	void putCustomCommadnAfterLogin(){
		if(SshSettings.customFunctions!=null) {
			SshSettings.customFunctions.putCustomCommadnAfterLogin(this);
		}
	}


	@Override
	boolean isConnected() {
		boolean res = super.isConnected()
//		log.info "${res}"
		return res
	}
}