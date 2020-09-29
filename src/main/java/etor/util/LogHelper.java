package etor.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogHelper {
    final static Logger logger = LogManager.getLogger();

    public static Logger getLogger() {
        return logger;
    }
}
