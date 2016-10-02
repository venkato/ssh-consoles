package com.jpto.core

import net.sf.jremoterun.utilities.nonjdk.downloadutils.WinptyDownloader
import net.sf.jremoterun.utilities.nonjdk.idwutils.IdwUtils
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.nonjdk.idwutils.IdwUtilsStarter
import net.sf.jremoterun.utilities.nonjdk.swing.SimpleFrameCreator

import javax.imageio.ImageIO;
import java.awt.BorderLayout
import javax.swing.JFrame;

import org.apache.log4j.Logger

import com.jpto.settings.SshSettings;

import net.infonode.docking.RootWindow;
import net.infonode.docking.util.DockingUtil;
import net.infonode.docking.util.ViewMap;

import groovy.transform.CompileStatic

import java.awt.image.BufferedImage;




@CompileStatic
public class JptoSshConsoles {
	private static final Logger logger = Logger.getLogger(JptoSshConsoles);
	private static final Logger log = logger;

	public static void launchCore() throws Exception {
		com.jpto.core.SshSessionStateMonitor.createMonitor2();
//		assert JptoSshConsoles.classLoader == ClassLoader.getSystemClassLoader()
		RootWindow rootWindow = IdwUtilsStarter.createRootWindow();

		JFrame frame = SimpleFrameCreator.createAppFrame("Ssh consoles");
		InputStream iconRes = JrrClassUtils.currentClassLoader.getResourceAsStream("ssh_icon.png")
		assert iconRes!=null
		SimpleFrameCreator.setIcon(frame,iconRes)

		frame.getContentPane().add(rootWindow, BorderLayout.CENTER);
		rootWindow.setWindow(SshSettings.customFunctions.initHosts());
//		IdwUtils.addMaxButton(rootWindow);
		frame.setVisible(true);


		Thread.sleep(Long.MAX_VALUE);
	}

}
