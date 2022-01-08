package com.example.ratingmanagement.performancemonitoring;

import com.example.ratingmanagement.logger.LogCollector;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.springframework.aop.interceptor.AbstractMonitoringInterceptor;
import org.springframework.beans.factory.annotation.Autowired;

public class LoggingPerformanceMonitorInterceptor extends AbstractMonitoringInterceptor {

    @Autowired
    private LogCollector logCollector;

    public LoggingPerformanceMonitorInterceptor() {
    }

    public LoggingPerformanceMonitorInterceptor(boolean useDynamicLogger) {
        setUseDynamicLogger(useDynamicLogger);
    }

    @Override
    protected Object invokeUnderTrace(MethodInvocation invocation, Log log)
            throws Throwable {
        String name = createInvocationTraceName(invocation);
        long start = System.currentTimeMillis();

        try {
            return invocation.proceed();
        }
        finally {
            long end = System.currentTimeMillis();
            long time = end - start;

            String summary = "Method "+name+" execution lasted:"+time+" ms";

            log.info(summary);

            logCollector.addLog("PERFORMANCE", summary);
            logCollector.release();

        }
    }

}
