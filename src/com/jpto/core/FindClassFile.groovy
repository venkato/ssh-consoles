package com.jpto.core

import groovy.transform.CompileStatic;
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.classpath.ClRef
import net.sf.jremoterun.utilities.nonjdk.IfFrameworkSrcDirs
import net.sf.jremoterun.utilities.nonjdk.consoleprograms.DefaultConsolePrograms;

import java.util.logging.Logger;

@CompileStatic
class FindClassFile {
    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    ClRef inText(String fullLine, String text, JptoTerminalPanel term) {
        List<String> tokenize1 = text.tokenize(' ')
        return inText2(fullLine,tokenize1,term);
    }

    ClRef inText2(String fullLine, List<String> tokenize1, JptoTerminalPanel term) {

        int classNameIndex = -1;
        for (int i = 0; i < tokenize1.size(); i++) {
            String token = tokenize1.get(i)
            if (token.length() > 0 && token.contains('.')) {
                classNameIndex = i;
                break;
            }
        }
        if (classNameIndex < 0) {
            return null;
        }
        String className = tokenize1.get(classNameIndex)
        return new ClRef(className);
    }

    static ClRef findClassFromShortcut(String text) {
        Object textFound = DefaultConsolePrograms.defaultShortcuts2.get(text)
        if (textFound == null) {
            return null
        }
        if (textFound instanceof ClRef) {
            ClRef cll = (ClRef) textFound;
            return cll;
        }
        if (textFound instanceof String) {
            return new ClRef(textFound)
        }
        throw new Exception("failed resolve : ${textFound.getClass()} ${textFound}")
    }

    static void initSrcDirDeafult() {
        JptoMethodInfo.sourceRoot.addAll(IfFrameworkSrcDirs.all.collect { it.resolveToFile() })

    }

}
