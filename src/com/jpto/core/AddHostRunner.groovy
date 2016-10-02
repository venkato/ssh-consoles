package com.jpto.core;

import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.nonjdk.rstarunner.RstaRunnerWithStackTrace2;

import java.util.logging.Logger;
import groovy.transform.CompileStatic;


@CompileStatic
class AddHostRunner extends RstaRunnerWithStackTrace2{

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    AddHostRunner(File file) {
        super(file)
    }




}
