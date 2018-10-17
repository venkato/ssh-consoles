package com.jpto.settings

import com.google.common.collect.Maps
import com.jcraft.jsch.JSch
import com.jpto.core.FindClassFile
import com.jpto.core.JptoCustomFunctions
import com.jpto.core.JscpLogger
import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.groovystarter.GroovyMethodRunnerParams
import org.apache.commons.lang3.SystemUtils
import org.apache.log4j.Logger

@CompileStatic
public class SshSettings {

    public static int logLevel = com.jcraft.jsch.Logger.WARN;


    public static int logonTimeOutInMs = 2000;
    public static int openShellChannelTimeoutInMs = -1;

    public static String nativeShellCmd = SystemUtils.IS_OS_WINDOWS ? 'cmd.exe' : 'bash';
    public static String pathEnvName = SystemUtils.IS_OS_WINDOWS ? 'Path' : 'PATH';
    public static String pathSeparator = System.getProperty('path.separator')
    public static volatile boolean enableAdoptToFixMsgs = false;

    public static File sedEvaluatorDir;
    public static int maxAuthFailedAttempts = -1;

    /**
     * Host name pattern used in 'add host'
     */
    public static String addHostNameTempalate = "";

    public static JptoCustomFunctions customFunctions;
    public static FindClassFile findClassFile = new FindClassFile();

    static Map<String, String> envs = new HashMap(System.getenv());

    public static JptoJediSettings defaultSettingsProvider = new JptoJediSettings();

    static {
        // envs.put('LANG', 'ru_RU.CP1251')
        envs.remove('groovypath')
        envs.remove('GROOVY_OPTS')
    }


    static List<File> getPathEnvOnlyExisted() {
        return getPathEnv().findAll { it.isDirectory() }.findAll { it.exists() }.collect { it.getCanonicalFile().getAbsoluteFile() }.unique()
    }

    static List<File> getPathEnv() {
        String value = envs.get(pathEnvName)
        if (value == null) {
            throw new Exception("env not found : ${pathEnvName}")
        }
        List<String> tokenize = value.tokenize(pathSeparator);
        return tokenize.collect { new File(it) }
    }

    static void setPath(List<File> dirs) {
        String value1 = dirs.collect { it.getCanonicalFile().getAbsolutePath() }.join(pathSeparator)
        envs.put(pathEnvName, value1);
    }

}
