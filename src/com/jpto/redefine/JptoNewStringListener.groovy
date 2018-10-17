package com.jpto.redefine

import groovy.transform.CompileStatic;
import net.sf.jremoterun.utilities.JrrClassUtils;
import java.util.logging.Logger;

@CompileStatic
interface JptoNewStringListener {


    void newValue(JptoJediTerminal jptoJediTerminal, String s)
}
