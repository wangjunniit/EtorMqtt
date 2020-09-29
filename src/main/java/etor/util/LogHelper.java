package etor.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogHelper {
    final static Logger logger = LogManager.getLogger();

    public static void trace(String content){
        logger.trace(content);
    }

    public static void debug(String content){
        logger.debug(content);
    }

    public static void info(String content){
        logger.info(content);
    }

    public static void warn(String content){
        logger.warn(content);
    }

    public static void error(String content){
        logger.error("error level");
    }

    public static void fatal(String content){
        logger.fatal(content);
    }
}
