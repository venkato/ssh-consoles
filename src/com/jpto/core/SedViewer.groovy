package com.jpto.core

import com.jpto.settings.SshSettings
import groovy.transform.CompileStatic
import net.infonode.docking.View;
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.nonjdk.StringFindRange
import net.sf.jremoterun.utilities.nonjdk.nativeprocess.NativeProcessResult
import net.sf.jremoterun.utilities.nonjdk.swing.JPanel4FlowLayout
import net.sf.jremoterun.utilities.nonjdk.swing.JPanelBorderLayout
import net.sf.jremoterun.utilities.nonjdk.swing.NameAndTextField
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea
import org.fife.ui.rsyntaxtextarea.SyntaxConstants
import org.fife.ui.rtextarea.RTextScrollPane

import javax.swing.JButton
import javax.swing.JPanel
import javax.swing.JSplitPane
import javax.swing.JTextField
import javax.swing.SwingUtilities
import java.awt.BorderLayout
import java.awt.FlowLayout
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.text.SimpleDateFormat
import java.util.logging.Logger;

@CompileStatic
class SedViewer {
    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    static enum ActionStyle {
        Run, Cancel,
    }


    public File scriptFile;
    public File textForAnalize;
    public static SimpleDateFormat sdf = new SimpleDateFormat('HH:mm:ss')

    public volatile Process process1;
    public volatile Thread runningThread;
    public volatile Date startTime; ;

    public final JTextField statusLine = new JTextField("Status")
    public final JptoTerminalPanel term;

    public final NameAndTextField upperLineField = new NameAndTextField('Upper', "0", 2);
    public final NameAndTextField buttomLineField = new NameAndTextField('Buttom', "0", 2);
    public final JButton runButton = new JButton(ActionStyle.Run.name());

    public final JPanel buttonsPanel = new JPanel(new FlowLayout());
    public final JPanel controlPanel = new JPanelBorderLayout();
    public final JPanel buttomPanel = new JPanelBorderLayout();


    public final RSyntaxTextArea outTextArea = new RSyntaxTextArea();

    public final RTextScrollPane outScrollPane = new RTextScrollPane(outTextArea, true);

    public final JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, controlPanel, buttomPanel);

    public final View view = new View("Sed viewer", null, splitPane);

    public final RSyntaxTextArea sedExpression = new RSyntaxTextArea("sed -r 's///g'")

    public OutputStream outputStream1 = new OutputStream() {
        @Override
        void write(int oneByte) throws IOException {
            byte b = (byte) oneByte
            char c = (char) b;
            appendText(Character.toString(c))
        }

        @Override
        void write(byte[] buffer) throws IOException {
            appendText(new String(buffer))
        }

        @Override
        void write(byte[] buffer, int offset, int count) throws IOException {
            appendText(new String(buffer, offset, count))
        }
    };

    void appendText(String text) {
        SwingUtilities.invokeLater {
            outTextArea.append(text)
        }
    }


    SedViewer(JptoTerminalPanel term) {
        if(SshSettings.sedEvaluatorDir==null){
            throw new Exception('SshSettings.sedEvaluatorDir is null')
        }
        assert SshSettings.sedEvaluatorDir.isDirectory()
        scriptFile = new File(SshSettings.sedEvaluatorDir, "sedviewerr.bat")
        textForAnalize = new File(SshSettings.sedEvaluatorDir, "text_for_investigate.txt");
        buttomPanel.add(outScrollPane, BorderLayout.CENTER);
        buttomPanel.add(statusLine, BorderLayout.SOUTH);
        statusLine.setEditable(false)
        this.term = term
        sedExpression.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_UNIX_SHELL);
        sedExpression.setLineWrap(true)
        sedExpression.setWrapStyleWord(true)
        sedExpression.setEditable(true)
        outTextArea.setWrapStyleWord(true);
        outTextArea.setLineWrap(true);
        outTextArea.setEditable(false)
        buttonsPanel.add(upperLineField)
        buttonsPanel.add(buttomLineField)
        buttonsPanel.add(runButton)
        controlPanel.add(sedExpression, BorderLayout.CENTER);

        controlPanel.add(buttonsPanel, BorderLayout.EAST);
        runButton.addActionListener { buttonPressed() };
        sedExpression.addKeyListener(new KeyAdapter() {
            @Override
            void keyReleased(KeyEvent e) {
                int code = e.getKeyCode();
                switch (code) {
                    case KeyEvent.VK_ENTER:
                        ActionStyle actionStyle = ActionStyle.valueOf(runButton.getText());
                        log.info "actionStyle = ${actionStyle}"
                        if (actionStyle == ActionStyle.Run) {
                            try {
                                doRun()
                            } catch (Exception e2) {
                                net.sf.jremoterun.utilities.JrrUtilitiesShowE.showException("Failed in sed viewer", e2);
                                runButton.setText(ActionStyle.Run.name())
                            }
                        }
                        break
                    default:
                        break;
                }
            }
        });
        splitPane.setDividerLocation((double) 0.3)
    }

    void buttonPressed() {
        try {
            ActionStyle actionStyle = ActionStyle.valueOf(runButton.getText());
            if (actionStyle == ActionStyle.Run) {
                doRun()
            } else {
                assert process1 != null
                log.info("calling destroy")
                process1.destroy()
                runButton.setText(ActionStyle.Run.name())
                log.info("destroy called")
            }
        } catch (Exception e) {
            net.sf.jremoterun.utilities.JrrUtilitiesShowE.showException("Failed in sed viewer", e);
            runButton.setText(ActionStyle.Run.name())
        }

    }

    void doRun() {
        outTextArea.setText("")
        statusLine.setText("Started at : ${sdf.format(new Date())}")
        String sedExpNow = sedExpression.getText();
        sedExpNow = sedExpNow.replace('\r', '').replace('\n', '').trim()
        sedExpression.setText(sedExpNow)
        if(sedExpNow.endsWith('|')){
            int length1 =sedExpNow.length()
            StringFindRange findRange = new StringFindRange(sedExpNow)
            sedExpNow = findRange.subStringInclusiveStart()
            assert sedExpNow.length()-1==length1
        }
        //assert process1 == null
        log.info "before process is null : ${process1 == null}"
        log.info "before thread is null : ${runningThread == null}"
        List<String> text = term.getVisibleText();
        int upper = upperLineField.textField.getText() as int
        int buttom = buttomLineField.textField.getText() as int
        if (upper > 0) {
            for (int i = 0; i < upper; i++) {
                text.remove(0)
            }
        }
        if (buttom > 0) {
            for (int i = 0; i < buttom; i++) {
                text.remove(text.size() - 1)
            }
        }
        textForAnalize.text = text.join('\n');
        scriptFile.text = """
@echo off
cat ${textForAnalize.getAbsolutePath()} | ${sedExpNow}
""";

        Runnable r = {
            try {
                runSedProcess()
            } catch (Exception e) {
                net.sf.jremoterun.utilities.JrrUtilitiesShowE.showException("Failed in sed viewer", e);
            }finally{
                SwingUtilities.invokeLater {
                    runButton.setText(ActionStyle.Run.name())
                }
            }

        }
        runButton.setText(ActionStyle.Cancel.name())
        runningThread = new Thread(r, 'sed eval')
        runningThread.start();
    }


    void runSedProcess() {
        String[] env1 = SshSettings.envs.entrySet().collect { "${it.key}=${it.value}".toString() }.toArray(new String[0])
        startTime = new Date();
        process1 = Runtime.getRuntime().exec(scriptFile.getAbsolutePath(), env1, SshSettings.sedEvaluatorDir);
        NativeProcessResult processResult = new NativeProcessResult(process1)
        processResult.timeoutInSec = 1
        processResult.exceptionOnError = false
        processResult.out2.addStream(outputStream1)
        processResult.err2.addStream(outputStream1)
//        GeneralUtils.waitFinish(process1, outputStream1, outputStream1, false)
        processResult.waitWithPeriodicCheck()
        long duration = System.currentTimeMillis() - startTime.getTime();
        int exitValue = processResult.exitCode
        SwingUtilities.invokeLater {
            statusLine.setText("Finished with exit code = ${exitValue} within ${duration} ms at ${sdf.format(startTime)}")
        }
        process1 = null;
    }

}
