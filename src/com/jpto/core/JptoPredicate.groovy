package com.jpto.core;

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.JrrClassUtils;

import com.google.common.base.Predicate;

import java.awt.event.KeyEvent;


@CompileStatic
public abstract class JptoPredicate implements Predicate<KeyEvent>{

	private static final java.util.logging.Logger log = JrrClassUtils.getJdkLogForCurrentClass();

	@Override
	public boolean apply(KeyEvent input2) {
		try {
			return apply2(input2);
		} catch (Exception e) {
			log.warn("",e);
			return false;
		}
	}

	
	public abstract boolean apply2(KeyEvent input) throws Exception;
	
}
