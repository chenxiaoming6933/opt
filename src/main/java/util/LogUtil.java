package util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @ClassName 日志工具类
 * @Author XiaoMing
 * @Date
 * @Vsersion V1.0
 */
public class LogUtil {

    private static final Log LOG = LogFactory.getLog(LogUtil.class);

    /**
     * @param message
     */
    public static void info(String message) {
        LOG.info(message);
    }

    public static void error(String errorMessage) {
        LOG.error(errorMessage);
    }
}
