package com.jpto.redefine

import groovy.transform.CompileStatic;
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.groovystarter.st.str2obj.StringToObjectConverter
import net.sf.jremoterun.utilities.nonjdk.ConsoleTextFunctionPrefix

import java.lang.reflect.Method
import java.lang.reflect.Type
import java.util.logging.Level;
import java.util.logging.Logger;

@CompileStatic
class TextMethodInvoker<T> {
    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();


    public static String argsSeparator = ' ';

    public final Map<String, Method> methodsMaps = [:];

    public final Object functions;
    public static volatile boolean invokeInNewThreadDefault = true;
    public volatile boolean invokeInNewThread = invokeInNewThreadDefault;
    public ThreadLocal<T> auxObjectThreadLocal = new ThreadLocal<>()



    TextMethodInvoker(Object functions1) {
        this.functions = functions1
        if (functions1 == null) {
            throw new NullPointerException('functons is null')
        }

        Method[] methods = functions1.getClass().getMethods()
        methods.toList().each {
            if (isGoodMethod(it)) {
                methodsMaps.put(it.getName(), it);
            }
        }
    }

    boolean isGoodMethod(Method m) {
        if (m.getDeclaringClass() == Object) {
            return false
        }
        //log.info "${m.getName()} ${m.getDeclaringClass().getName()}"
        return true
    }


    void invokeFunction(T auxObject,String text) {
        text = text.trim();
        if (text.length() == 0) {
            throw new Exception('text length is 0')
        }
        int ii = text.indexOf(argsSeparator)
        if (ii <= 0) {
            throw new Exception("bad text : ${text}")
        }
        String methodName = text.substring(0, ii)
        Method method = methodsMaps.get(methodName)
        if (method == null) {
            onMethodNotFound(methodName)
        }
        String args = text.substring(ii + argsSeparator.length())
        List<Object> args1 = parseArgs(method, args.trim())
        log.info "${ConsoleTextFunctionPrefix.ignoreWord} running ${args1}"
        Object[] args2 = args1.toArray(new Object[0])
        runMethod(auxObject,method, args2)
    }

    void onMethodNotFound(String methodName) {
        log.info "method not found : ${methodName}"
        throw new Exception("method not found : ${methodName} , existed : ${methodsMaps.keySet()}")
    }

    void runMethod(T auxObject,Method method, Object[] args2) {
        if (invokeInNewThread) {
            Runnable r = {
                try {
                    auxObjectThreadLocal.set(auxObject)
                    method.invoke(functions, args2);
                } catch (Throwable e) {
                    onException(e)
                }
            }
            Thread thread = new Thread(r, 'Method invoker')
            thread.start()
        } else {
            auxObjectThreadLocal.set(auxObject)
            method.invoke(functions, args2);
        }
    }

    void onException(Throwable e) {
        log.log(Level.SEVERE, "${ConsoleTextFunctionPrefix.ignoreWord} ", e);
        net.sf.jremoterun.utilities.JrrUtilitiesShowE.showException("${ConsoleTextFunctionPrefix.ignoreWord} ", e)
    }


    List parseArgs(Method method, String text) {
        int index1 = 0;
        Type[] genericParameterTypes = method.getGenericParameterTypes()
        List<Class> paramTypes = (List) method.parameterTypes.toList()
        int size = paramTypes.size() - 1
        int i = 0
        List args3 = paramTypes.collect {
            boolean isLast = (i == size)
            String arg;
            if (isLast) {
                arg = text.substring(index1)
            } else {
                int ii = text.indexOf(argsSeparator, index1)
                if (ii <= 0) {
                    throw new Exception("bad parse param ${i + 1} : ${text}")
                }
                arg = text.substring(index1, ii)
                index1 = ii + argsSeparator.length()
            }
            Type genericArg
            Class paramType = paramTypes[i]
            if (genericParameterTypes.length > 0) {
                genericArg = genericParameterTypes[i]
            }
            Object param = StringToObjectConverter.defaultConverter.convertFromStringToType(arg, paramType, genericArg)
            i++
            return param

        }
        return args3
    }
}
