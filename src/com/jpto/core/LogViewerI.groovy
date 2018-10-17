package com.jpto.core

import groovy.transform.CompileStatic;
import net.sf.jremoterun.utilities.JrrClassUtils;
import java.util.logging.Logger;

@CompileStatic
interface LogViewerI {

    String name();

    File getFile();


}
