package com.jpto.redefine

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.groovystarter.st.str2obj.StringToObjectConverter
import net.sf.jremoterun.utilities.nonjdk.ConsoleTextFunctionPrefix

import java.lang.reflect.Method
import java.lang.reflect.Type
import java.util.logging.Level
import java.util.logging.Logger

@CompileStatic
class JptoNewStringListenerSound implements JptoNewStringListener {
    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    public static volatile TextMethodInvoker<JptoJediTerminal> textMethodInvoker;
//    public static volatile JptoFunctionsFactory functionsFactory;;



    @Override
    void newValue(JptoJediTerminal jptoJediTerminal, String s) {
        if(textMethodInvoker!=null) {
            int ii = s.indexOf(ConsoleTextFunctionPrefix.functionWord)
            if (ii >= 0) {
                String findd = ConsoleTextFunctionPrefix.ignoreWords.find { s.contains(it) }
                if (findd == null) {
                    try {
                        log.info "${ConsoleTextFunctionPrefix.ignoreWord} ${ii} ${s} ."
                        String text = s.substring(ii + ConsoleTextFunctionPrefix.functionWord.length())
                        textMethodInvoker.invokeFunction(jptoJediTerminal,text)
                    } catch (Exception e) {
                        onException(jptoJediTerminal, s, e);
                    }

                }
            }
        }
    }


    void onException(JptoJediTerminal jptoJediTerminal, String s, Exception e) {
        log.log(Level.SEVERE, "${ConsoleTextFunctionPrefix.ignoreWord} ${s}", e);
        net.sf.jremoterun.utilities.JrrUtilitiesShowE.showException("${ConsoleTextFunctionPrefix.ignoreWord} ${s}",e)
    }


    static void init() {
        com.jpto.redefine.TerminalRedefine.redefine()
        JptoJediTerminal.jptoNewStringListener = new JptoNewStringListenerSound()
    }
}
