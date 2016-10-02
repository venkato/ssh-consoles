package com.jpto.core

import groovy.transform.CompileStatic
import org.apache.log4j.Logger

@CompileStatic
class JediLogger extends Logger {


    final Logger nativeLogger;

    protected JediLogger(String name) {
        super(name)
        nativeLogger = Logger.getLogger(name)
    }

    static List<String> badStr = [
            "Attempt to get line out of bounds: -1 < 0",
            "Attempt to erase characters in line: -1",
            "Mode EightBigInt is not "
    ]

    @Override
    void error(Object message) {
        if (message instanceof String) {
            String msg = message;
            if (msg != null) {
                if(badStr.find {msg.contains(it)}!=null){
                    return
                }

            }
        }

        nativeLogger.error(message)
    }
}
