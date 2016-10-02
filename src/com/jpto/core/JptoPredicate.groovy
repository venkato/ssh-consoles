package com.jpto.core;

import groovy.transform.CompileStatic;
import org.apache.logging.log4j.Logger;

import com.google.common.base.Predicate;

import java.awt.event.KeyEvent;

import org.apache.logging.log4j.LogManager;

@CompileStatic
public abstract class JptoPredicate implements Predicate<KeyEvent>{
	private static final Logger log = LogManager.getLogger();

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
