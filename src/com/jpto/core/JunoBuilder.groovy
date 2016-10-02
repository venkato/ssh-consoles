package com.jpto.core

import net.sf.jremoterun.utilities.UrlCLassLoaderUtils;

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger;


//@CompileStatic
class JunoBuilder extends GroovyTestCase{
    private static final Logger log = LogManager.getLogger();
    
    AntBuilder ant = new AntBuilder()


    void test1(){

        File binFolder =  UrlCLassLoaderUtils.getClassLocation(JunoBuilder)
        File target =  new File(binFolder.parentFile, "sshtool.jar")
        ant.zip(update: "false", destfile: target , duplicate: "fail" ) {
            zipfileset(dir: binFolder, includes: 'com/jpto/core/java/**'  )
        }
    }



}
