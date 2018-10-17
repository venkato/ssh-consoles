package com.jpto.idea

import groovy.transform.CompileStatic
import com.intellij.execution.filters.ExceptionFilters
import com.intellij.execution.filters.Filter
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.terminal.JBTerminalPanel
import com.intellij.terminal.JBTerminalWidget
import com.jediterm.terminal.ui.TerminalSession
import com.jpto.core.concreator.JptoJSchShellTtyConnector
import com.jpto.core.concreator.SshConSet

import idea.plugins.thirdparty.filecompletion.share.JrrIdeaUtils
import idea.plugins.thirdparty.filecompletion.share.OSIntegrationIdea
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.nonjdk.swing.JrrSwingUtilsParent
import org.jetbrains.plugins.terminal.TerminalToolWindowFactory
import org.jetbrains.plugins.terminal.TerminalView

import java.awt.Window
import java.util.logging.Logger

@CompileStatic
class CreateSshTerminal {

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    SshConSet sshConSet;
    String tabName;

    MyJBTerminalSystemSettingsProvider2 mySettings
    JBTerminalWidget wi
    JptoJSchShellTtyConnector connector;
    TerminalSession session
    Runnable afterInit

    CreateSshTerminal(SshConSet sshConSet, String tabName) {
        this.sshConSet = sshConSet
        this.tabName = tabName
    }

    void run3() {
        Runnable r = { run2() }
        JrrIdeaUtils.submitTr2(r, false)
    }

    static ToolWindow getToolWindow() {
        Project project = OSIntegrationIdea.openedProject
        ToolWindow window = ToolWindowManager.getInstance(project).getToolWindow(TerminalToolWindowFactory.TOOL_WINDOW_ID);
        return window
    }


    static TerminalView getTerminalView() {
        Project project = OSIntegrationIdea.openedProject
        TerminalView terminalView = TerminalView.getInstance(project)
        ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(project)
        return terminalView
    }

    boolean isHasParent() {
        JBTerminalPanel terminalPanel1 = getTerminalPanel()
        Window parentComponent = JrrSwingUtilsParent.findParentComponent(terminalPanel1, Window)
        return parentComponent != null
    }

    void run2() {
        Project project = OSIntegrationIdea.openedProject
        TerminalView terminalView = getTerminalView()
        Object myTerminalWidget = JrrClassUtils.getFieldValue(terminalView, "myTerminalWidget")

        mySettings = new MyJBTerminalSystemSettingsProvider2();
        List<Filter> filters = ExceptionFilters.getFilters(GlobalSearchScope.allScope(project))
        wi = JrrClassUtils.invokeJavaMethod(myTerminalWidget, "createInnerTerminalWidget", mySettings) as JBTerminalWidget;
        connector = createConnector();
        session = wi.createTerminalSession(connector);
        session.start()
        filters.each { wi.addMessageFilter(it) }
        JrrClassUtils.invokeJavaMethod2('addTab',tabName,wi)
        if (afterInit != null) {
            afterInit.run()
        }
    }

    JptoJSchShellTtyConnector createConnector() {
        return new JptoJSchShellTtyConnector(sshConSet);
    }

    JBTerminalPanel getTerminalPanel() {
        wi.getTerminalPanel() as JBTerminalPanel;
    }

}
