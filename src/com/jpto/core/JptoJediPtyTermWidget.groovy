package com.jpto.core


import com.jediterm.terminal.model.StyleState
import com.jediterm.terminal.model.TerminalTextBuffer
import com.jediterm.terminal.ui.JediTermWidget
import com.jediterm.terminal.ui.TerminalPanel
import com.jediterm.terminal.ui.settings.SettingsProvider
import groovy.transform.CompileStatic


@CompileStatic
public class JptoJediPtyTermWidget extends JptoCommonJediTermWidget {


	public JptoTtyConnector jSchShellTtyConnector;

	public JptoJediPtyTermWidget(SettingsProvider settingsProvider, JptoTtyConnector jSchShellTtyConnector) {
		super(settingsProvider);
		this.jSchShellTtyConnector = jSchShellTtyConnector;
	}

	@Override
	protected TerminalPanel createTerminalPanel(SettingsProvider settingsProvider, StyleState styleState,
			TerminalTextBuffer terminalTextBuffer) {
		return new JptoTerminalPanel(settingsProvider, terminalTextBuffer, styleState);
	}
}
