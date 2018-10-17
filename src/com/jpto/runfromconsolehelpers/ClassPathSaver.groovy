package com.jpto.runfromconsolehelpers

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.nonjdk.classpath.calchelpers.ClassPathCalculatorSup2Groovy

import java.util.logging.Logger


// IDE already know what jar needed to run and where they located.
// Make life easy by using it ?
// This class save location of all jars used by this project to 'classpath.txt' file
@CompileStatic
public class ClassPathSaver {

	private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

	
	public static void main(String[] args) throws Exception {
		 File jars = new File("classpath.groovy");
		ClassPathCalculatorSup2Groovy classPathCalculatorGroovy = new ClassPathCalculatorSup2Groovy();
		classPathCalculatorGroovy.saveClassPathFromJmx(jars);
	}




}
