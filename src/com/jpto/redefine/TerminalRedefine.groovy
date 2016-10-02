package com.jpto.redefine

import com.jediterm.terminal.ui.JediTermWidget
import javassist.CtClass
import javassist.CtConstructor
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.groovystarter.st.JdkLogFormatter
import net.sf.jremoterun.utilities.javassist.JrrJavassistUtils
import org.junit.Test;

import java.util.logging.Logger;
import groovy.transform.CompileStatic;


@CompileStatic
class TerminalRedefine {

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    static boolean inited = false;

    @Test
    void redefine2(){
        JdkLogFormatter.setLogFormatter()
        log.info "1"
        redefine()
    }

    static void redefine(){
        if(inited){

        }else {
            Terminal3Redefine.redefine3()
//            Class clazz = JediTermWidget
//            CtClass ctClazz = JrrJavassistUtils.getClassFromDefaultPool(clazz)
//            CtConstructor constructor = JrrJavassistUtils.findConstructor(ctClazz, 3);
//            log.info "${constructor.getParameterTypes()}"
//            constructor.instrument(new JptoTerminalExprEditor())
//            JrrJavassistUtils.redefineClass(ctClazz, clazz)
        }
    }



}
