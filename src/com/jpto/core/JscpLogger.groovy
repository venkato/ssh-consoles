package com.jpto.core;

import org.apache.logging.log4j.Logger;
import groovy.transform.CompileStatic;

import net.sf.jremoterun.utilities.JrrClassUtils;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

@CompileStatic
public class JscpLogger implements com.jcraft.jsch.Logger {

	private int enabledLogger = WARN;

	public static Level[] levelsMapping = new Level[5];

	static {
		levelsMapping[DEBUG] = Level.DEBUG;
		levelsMapping[INFO] = Level.INFO;
		levelsMapping[WARN] = Level.WARN;
		levelsMapping[ERROR] = Level.ERROR;
		levelsMapping[FATAL] = Level.FATAL;
	}

	public JscpLogger(int enabledLogger) {
		this.enabledLogger = enabledLogger;
	}

	@Override
	public boolean isEnabled(int level) {
		return level >= enabledLogger;
	}

	@Override
	public void log(int level, String message) {
		Logger logger = LogManager.getLogger(JrrClassUtils.getCurrentClass());
		Level level2 = levelsMapping[level];
		logger.log(level2, message);

	}

}
