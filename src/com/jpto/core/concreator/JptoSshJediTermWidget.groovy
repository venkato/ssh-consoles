package com.jpto.core.concreator

import com.jpto.core.JptoCommonJediTermWidget
import com.jpto.core.JptoTerminalPanel
import org.apache.log4j.Logger;
import groovy.transform.CompileStatic;

import com.jediterm.terminal.model.StyleState;
import com.jediterm.terminal.model.TerminalTextBuffer
import com.jediterm.terminal.ui.TerminalPanel;
import com.jediterm.terminal.ui.settings.SettingsProvider;

@CompileStatic
public class JptoSshJediTermWidget extends JptoCommonJediTermWidget {

	private static final Logger logger = Logger.getLogger(JptoSshJediTermWidget);
	private static final Logger log = logger;
	
	public JptoJSchShellTtyConnector jSchShellTtyConnector;

	public JptoSshJediTermWidget(SettingsProvider settingsProvider, JptoJSchShellTtyConnector jSchShellTtyConnector) {
		super(settingsProvider);
		this.jSchShellTtyConnector = jSchShellTtyConnector;
	}

	@Override
	protected TerminalPanel createTerminalPanel(SettingsProvider settingsProvider, StyleState styleState,
			TerminalTextBuffer terminalTextBuffer) {
		return new JptoTerminalPanel(settingsProvider, terminalTextBuffer, styleState);
	}


}
