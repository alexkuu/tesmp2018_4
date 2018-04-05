package com.alex.app.services

import org.springframework.stereotype.Service
import org.apache.log4j.Level;

@Service
class Logger {

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(Logger.class);
    private static final String CLASS_NAME = Logger.class.getCanonicalName();

    private static void log(Level lvl, String msg, Throwable t) {
        LOG.log(CLASS_NAME, lvl, msg, t)
    }

    static void debug(String msg) {
        log(Level.DEBUG, msg, null)
    }

    static void info(String msg) {
        log(Level.INFO, msg, null)
    }

    static void warning(String msg) {
        log(Level.WARN, msg, null)
    }

    static void error(String msg) {
        log(Level.ERROR, msg, null)
    }

    static void error(Throwable t) {
        log(Level.ERROR, "", t)
    }

    static void error(String message, Throwable t) {
        log(Level.ERROR, message, t)
    }

}
