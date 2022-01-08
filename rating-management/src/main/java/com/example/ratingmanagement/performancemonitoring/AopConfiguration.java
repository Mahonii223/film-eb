package com.example.ratingmanagement.performancemonitoring;

import com.example.ratingmanagement.logger.LogCollector;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.interceptor.PerformanceMonitorInterceptor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

/*
@Configuration
@EnableAspectJAutoProxy
@Aspect
public class AopConfiguration {

    @Pointcut(
            "execution(public * com.example.ratingmanagement.requesthandler.DataAccessManager.callDA(..))"
    )
    public void monitor() { }

    @Bean
    public LoggingPerformanceMonitorInterceptor loggingPerformanceMonitorInterceptor() {
        return new LoggingPerformanceMonitorInterceptor(true);
    }

    @Bean
    public Advisor performanceMonitorAdvisor() {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("com.example.ratingmanagement.performancemonitoring.AopConfiguration.monitor()");
        return new DefaultPointcutAdvisor(pointcut, loggingPerformanceMonitorInterceptor());
    }

}*/

@Aspect
@Component
public class AopConfiguration
{
    @Autowired
    private LogCollector logCollector;

    //AOP expression for which methods shall be intercepted
    @Around("execution(public * com.example.ratingmanagement.requesthandler.DataAccessManager.callDA(..))")
    public Object profileAllMethods(ProceedingJoinPoint proceedingJoinPoint) throws Throwable
    {
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();

        //Get intercepted method details
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();

        final StopWatch stopWatch = new StopWatch();

        //Measure method execution time
        stopWatch.start();
        Object result = proceedingJoinPoint.proceed();
        stopWatch.stop();

        logCollector.addLog("PERFORMANCE",
                "Execution time of " + className + "." + methodName + " :: " + stopWatch.getTotalTimeMillis() + " ms");
        logCollector.release();

        return result;
    }
}