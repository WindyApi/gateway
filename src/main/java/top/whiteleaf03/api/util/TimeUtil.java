package top.whiteleaf03.api.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TimeUtil {
    private static final ThreadLocal<Long> threadLocalStorage = new ThreadLocal<>();
    public static void startTiming() {
        threadLocalStorage.set(System.currentTimeMillis());
        log.info("开始计时");
    }

    public static void endTiming() {
        long startTime = threadLocalStorage.get();
        threadLocalStorage.remove();
        log.info("计时结束，共耗时{}ms", System.currentTimeMillis() - startTime);
    }
}
