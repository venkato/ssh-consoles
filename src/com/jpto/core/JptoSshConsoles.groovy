package com.jpto.core


import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.nonjdk.idwutils.IdwUtilsStarter
import net.sf.jremoterun.utilities.nonjdk.swing.SimpleFrameCreator
import com.jpto.redefine.JptoNewStringListenerSound

import java.awt.BorderLayout
import javax.swing.JFrame;

import org.apache.log4j.Logger

import com.jpto.settings.SshSettings;

import net.infonode.docking.RootWindow
import groovy.transform.CompileStatic

@CompileStatic
public class JptoSshConsoles {
	private static final Logger logger = Logger.getLogger(JptoSshConsoles);
	private static final Logger log = logger;

	public static JFrame launchCore() throws Exception {
		JptoNewStringListenerSound.init()
		com.jpto.core.SshSessionStateMonitor.createMonitor2();
//		assert JptoSshConsoles.classLoader == ClassLoader.getSystemClassLoader()
		RootWindow rootWindow = IdwUtilsStarter.createRootWindow();

		JFrame frame = SimpleFrameCreator.createAppFrame(SshSettings.frameName);
		InputStream iconRes = JrrClassUtils.currentClassLoader.getResourceAsStream("ssh_icon.png")
		assert iconRes!=null
		SimpleFrameCreator.setIcon(frame,iconRes)

		frame.getContentPane().add(rootWindow, BorderLayout.CENTER);
		if(SshSettings.customFunctions==null){
			throw new NullPointerException('SshSettings.customFunctions is null')
		}
		rootWindow.setWindow(SshSettings.customFunctions.initHosts());
//		IdwUtils.addMaxButton(rootWindow);
		frame.setVisible(true);
		return frame

	}

}
