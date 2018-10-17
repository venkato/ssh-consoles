package com.jpto.redefine

import com.jediterm.terminal.model.JediTerminal
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
    public boolean terminalTextBufferRedified = false;

    @Override
    void edit(ConstructorCall c) throws CannotCompileException {


    }

    @Override
    void edit(NewExpr c) throws CannotCompileException {
        switch (c.className){
            case com.jediterm.terminal.model.TerminalTextBuffer.getName():
//                log.info "TerminalTextBuffer : ${c.className} ${c.lineNumber}"
//                ClRef cnr = new ClRef("com.jpto.redefine.JptoTerminalTextBuffer")
//                c.replace("\$_ = new ${cnr.className}(\$\$);")
//                log.info "redefined TerminalTextBuffer"
                break;
            case com.jediterm.terminal.model.JediTerminal.getName():
                log.info "JptoJediTerminal : ${c.className} ${c.lineNumber}"
                ClRef cnr = new ClRef("com.jpto.redefine.JptoJediTerminal")
                c.replace("\$_ = new ${cnr.className}(\$\$);")
                log.info "redefined JptoJediTerminal"
                terminalTextBufferRedified = true
                break
            default:
                super.edit(c)
                break
        }
    }
}
