package com.jpto.core

import com.github.javaparser.JavaParser
import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.jpto.settings.SshSettings
import groovy.transform.CompileStatic
import javassist.bytecode.ClassFile
import net.infonode.docking.DockingWindow
import net.infonode.docking.View
import net.sf.jremoterun.JrrUtils;
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.classpath.ClRef
import net.sf.jremoterun.utilities.nonjdk.StringUtils
import net.sf.jremoterun.utilities.nonjdk.classpath.ClassNameSuffixes
import net.sf.jremoterun.utilities.nonjdk.classpath.UrlCLassLoaderUtils2
import net.sf.jremoterun.utilities.nonjdk.idwutils.TextAreaAndView
import org.apache.commons.io.IOUtils
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea
import org.fife.ui.rsyntaxtextarea.SyntaxConstants
import org.fife.ui.rsyntaxtextarea.folding.Fold
import org.fife.ui.rsyntaxtextarea.folding.FoldManager
import org.fife.ui.rtextarea.RTextScrollPane
import org.objectweb.asm.ClassReader
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.MethodNode

import javax.swing.JPanel
import javax.swing.SwingUtilities
import java.beans.PropertyChangeEvent
import java.beans.PropertyChangeListener;
import java.util.logging.Logger;

@CompileStatic
class JptoMethodInfo {
    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    public JptoTerminalPanel term;
    public JPanel rootPanel = new JPanel()

    public ClassLoader defaultCl = JrrClassUtils.getCurrentClassLoader();
    public RSyntaxTextArea textArea = new RSyntaxTextArea();

    public RTextScrollPane scrollPane = new RTextScrollPane(textArea, true);
    public View view = new View('MethodInfo', null, scrollPane);

    public static List<File> sourceRoot = [];
    public FoldManager foldManager
    public volatile boolean needCollapse = false

    JptoMethodInfo(JptoTerminalPanel term) {
        this.term = term
        textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
        scrollPane.setLineNumbersEnabled(true)
        scrollPane.setFoldIndicatorEnabled(true)
        textArea.setCodeFoldingEnabled(true)
        foldManager = textArea.getFoldManager();
        foldManager.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            void propertyChange(PropertyChangeEvent event) {
                log.info "eve = ${event.propertyName} ${needCollapse}"
                if (event.propertyName == FoldManager.PROPERTY_FOLDS_UPDATED) {
                    onFoldUpdated();
                }
            }
        })
    }

    void onFoldUpdated() {
        if (needCollapse) {
            needCollapse = false
            SwingUtilities.invokeLater {
                collapseMethods()
            }
        }
    }

    void buildAssist() {
        try {
            if(sourceRoot.size()==0){
                throw new Exception("sourceRoot not set")
            }
            String s = term.getLastLineNoWrap();
            log.info "last line : ${s}"
            String sf = 'gr '
            int i = s.indexOf(sf)
            if (i < 0) {
                log.info "gr not found"
            } else {
                int j = i + sf.length()
                String s2 = s.substring(j)
                inText(s, s2)
            }
        } catch (Throwable e) {
            log.severe("failed build method info", e)
            net.sf.jremoterun.utilities.JrrUtilitiesShowE.showException("failed build method info", e)
        }
    }

    void inText(String fullLine, String text) {
        ClRef className = SshSettings.findClassFile.inText(fullLine, text, term);
        if (className == null) {
            log.info "class name not found : ${text}"
        } else {
            displayAllMethodsForClassJava(className.className)
        }
    }


    void displayAllMethodsForClassJava(String className) {
        String aaa = className.replace('.', '/')
        final String fullRef2
        String fullRef = aaa + ClassNameSuffixes.dotjava.customName
        File dir1 = sourceRoot.find { it.child(fullRef).exists() }
        if (dir1 == null) {
            //log.info "not found java : ${fullRef}"
            String fullRef3 = aaa + ClassNameSuffixes.dotgroovy.customName
            dir1 = sourceRoot.find { it.child(fullRef3).exists() }
            if (dir1 == null) {
                throw new Exception("not found java or groovy : ${fullRef3}")
            }
//            log.info "fullRef3 = ${fullRef3}"
            fullRef2 = fullRef3
        } else {
            fullRef2 = fullRef
        }
        File javaFile = dir1.child(fullRef2)
        assert javaFile.exists();
        log.info "class found ${javaFile}"
        needCollapse = true
        textArea.setText(javaFile.text);

//        SwingUtilities.invokeLater{collapseMethods()}


//        CompilationUnit cu = JavaParser.parse(javaFile);
//        ClassOrInterfaceDeclaration typeDeclaration = cu.getPrimaryType().get() as ClassOrInterfaceDeclaration;
//        typeDeclaration.getMethods()
    }

    void collapseMethods() {


        int foldCount = foldManager.getFoldCount();
        if (foldCount >0 && foldCount <4) {
            log.info "fold count : ${foldCount}"
            for (int i = 0; i < foldCount-1; i++) {
                Fold foldImport = foldManager.getFold(i)
                if (!foldImport.isCollapsed()) {
                    foldImport.toggleCollapsedState();
                }
            }
            Fold foldClass = foldManager.getFold(foldCount - 1)
            if (foldClass.isCollapsed()) {
                foldClass.toggleCollapsedState();
            }
            int childCount = foldClass.getChildCount()
            log.info "childCount : ${childCount}"
            for (int i = 0; i < childCount; i++) {
                Fold childMethod = foldClass.getChild(i);
                if (!childMethod.isCollapsed()) {
                    childMethod.toggleCollapsedState()
                }
            }
        } else {
            log.info "fold count wrong : ${foldCount}"
        }
    }

    // what is needed for ?
//    private void displayAllMethodsForClassBinary(String className) {
//        String aaa = className.replace('.', '/')
//        String fullRef = aaa + '.class'
//        InputStream stream = defaultCl.getResourceAsStream(fullRef)
//        if (stream == null) {
//            log.info "class not found ${fullRef}"
//        } else {
//
//            byte[] content = IOUtils.toByteArray(stream)
//            ClassReader classReader = new ClassReader(content);
//            ClassNode classNode = new ClassNode();
//
//            classReader.accept(classNode, parsingOptions);
//            List<MethodNode> methods = classNode.methods
//            methods = methods.findAll { (it.access & Opcodes.ACC_PUBLIC) > 0 };
//
//        }
//    }

    public static int parsingOptions = 0;


}
