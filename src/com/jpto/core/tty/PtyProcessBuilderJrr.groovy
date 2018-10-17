package com.jpto.core.tty

import com.pty4j.PtyProcess
import com.pty4j.PtyProcessBuilder
import groovy.transform.CompileStatic;
import net.sf.jremoterun.utilities.JrrClassUtils;
import java.util.logging.Logger;

@CompileStatic
class PtyProcessBuilderJrr extends PtyProcessBuilder{
    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    PtyProcessBuilderJrr() {
    }

    PtyProcessBuilderJrr(String[] command) {
        super(command)
    }

    @Override
    PtyProcess start() throws IOException {
        return super.start()
    }
}
