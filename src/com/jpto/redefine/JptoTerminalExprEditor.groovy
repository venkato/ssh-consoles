package com.jpto.redefine

import groovy.transform.CompileStatic
import javassist.CannotCompileException
import javassist.expr.ConstructorCall
import javassist.expr.ExprEditor
import javassist.expr.NewExpr
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.classpath.ClRef

import java.util.logging.Logger

@Deprecated
@CompileStatic
class JptoTerminalExprEditor extends ExprEditor {

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    @Override
    void edit(ConstructorCall c) throws CannotCompileException {


    }

    @Override
    void edit(NewExpr c) throws CannotCompileException {
//        log.info "${c.className} ${c.lineNumber}"
        if (c.className == com.jediterm.terminal.model.TerminalTextBuffer.name) {
            log.info "${c.className} ${c.lineNumber}"
            ClRef cnr = new ClRef("com.jpto.redefine.JptoTerminalTextBuffer")
            c.replace("\$_ = new ${cnr.className}(\$\$);")
            log.info "redefined 23"
        } else {
            super.edit(c)
//            super.edit(e)
        }
    }
}
