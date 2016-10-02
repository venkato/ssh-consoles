package com.jpto.settings

import com.google.common.collect.Maps
import com.jpto.core.JptoCustomFunctions
import groovy.transform.CompileStatic
import org.apache.commons.lang3.SystemUtils
import org.apache.log4j.Logger

@CompileStatic
public class SshSettings {

    public static int logLevel = com.jcraft.jsch.Logger.WARN;

    public static int logonTimeOutInMs = 2000;

    public static String nativeShellCmd = SystemUtils.IS_OS_WINDOWS ? 'cmd.exe' : 'bash';

    public static volatile boolean enableAdoptToFixMsgs = false;

    /**
     * Host name pattern used in 'add host'
     */
    public static String addHostNameTempalate = "";

    public static JptoCustomFunctions customFunctions;

    static Map<String, String> envs = new HashMap(System.getenv());

    public static JptoJediSettings defaultSettingsProvider = new JptoJediSettings();

    static {
        // envs.put('LANG', 'ru_RU.CP1251')
        envs.remove('groovypath')
        envs.remove('GROOVY_OPTS')
    }


}
