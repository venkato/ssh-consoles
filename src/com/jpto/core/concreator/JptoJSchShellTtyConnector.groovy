package com.jpto.core.concreator


import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.classpath.ClRef
import net.sf.jremoterun.utilities.nonjdk.sshsup.JrrJSch
import net.sf.jremoterun.utilities.nonjdk.sshsup.JrrJschSession
import net.sf.jremoterun.utilities.nonjdk.sshsup.auth.SshAuthType
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
	private volatile JrrJschSession sessionCatched;

	public JSch jsch
//	SftpUtils sftpUtils;
	Thread connectingThread;
	public JptoSshJediTermWidget jediSshWidget
	public final Date startDate = new Date();

	JptoJSchShellTtyConnector(SshConSet sshConSet) {
		super(sshConSet.host, sshConSet.port, sshConSet.user, sshConSet.password)
		this.host = sshConSet;
	}

	@Override
	String toString() {
		return "JptoJSchShellTtyConnector : ${host.host}"
	}

	@Override
	protected JSch createJSch() {
		return new JrrJSch()
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
		Session session23= JrrClassUtils.getFieldValue(this,'mySession') as Session;
		if(session23==null){
			return sessionCatched
		}
		return session23
	}

	@Override
	protected void configureSession(Session session, Properties config) throws JSchException {

		if (!(session instanceof JrrJschSession)) {
			if(session==null){
				throw new Exception("session is null")
			}
			throw new Exception("session not jrr ${session.getClass().getName()}")
		}

		JrrJschSession  session1= (JrrJschSession) session;
		session1.jrrSchSessionLog = new JptoJrrSchSessionLog(jediSshWidget.getTerminal())
		sessionCatched = session1;
		if(SshSettings.maxAuthFailedAttempts >=0) {
			session1.setMax_auth_triesC(SshSettings.maxAuthFailedAttempts)
		}
		// session.setConfig("PreferredAuthentications", "password");
		// disable known host auth
		defaultConfigureSession(session,config)
		super.configureSession(session, config);
	}

	void defaultConfigureSession(Session session, Properties config){

		config.put("StrictHostKeyChecking", "no");
		if (host.sshKey == null) {
			setAuthTypes(session,[SshAuthType.password])
		} else {
			setAuthTypes(session,[SshAuthType.publickey])
		}
		if(SshSettings.customFunctions!=null) {
			SshSettings.customFunctions.configureSshSession(session)
		}
	}

	static void setAuthTypes(Session session,List<SshAuthType> authType){
		session.setConfig("PreferredAuthentications", authType.collect {it.customName}.join(','));
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
					log.info("connected to " + host);
					synchronized (initDonelock) {
						initDone = true;
					}
				} catch (IOException e) {
					log.error("failed put custom command", e);
					JrrJschSession  session1= (JrrJschSession) session;
					if(session1.jrrSchSessionLog!=null){
						session1.jrrSchSessionLog.logMsg("failed put custom command ${e}")
					}
					synchronized (initDonelock) {
						initDone = true;
						initDonelock.notifyAll();
					}
					return false;
				}
			}
			synchronized (initDonelock) {
				initDonelock.notifyAll();
			}
			long diffDate = System.currentTimeMillis() - startDate.getTime();
			log.info "${host.host} total init time : ${diffDate} ms"
			if(sessionCatched!=null){
				sessionCatched.jrrSchSessionLog.logMsg("total init time : ${diffDate} ms")
			}
			return res;
		}catch (Throwable e){
			log.info "failed connect to ${host} ${e}"
			throw e;
		}finally{

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

	void onClose(){
		log.info "on close"
	}

	static{
		new ClRef('com.jpto.core.DebugModeSet');
	}

	void checkIfConnected(){
		if(!initDone) {
			StackTraceElement[] trace = connectingThread.getStackTrace()
			trace.toList().each { println(it) }

			String additionInfo;
			Session session33 = getSession()
			if(session33==null){
				additionInfo = "jrr session is null"
			}else if (session33 instanceof JrrJschSession) {
				JrrJschSession session2 = (JrrJschSession) session33;
				additionInfo = session2.getConnectionStateHuman()
			}else{
				additionInfo = "not jrr session : ${session33.getClass().getName()}"
			}
			log.info("Logon failed ${additionInfo}, for debug call com.jpto.core.DebugModeSet.setDebugMode()");
			throw new Exception("Ssh connection failed : ${additionInfo}");
		}
	}


}