package wooni.spring.aop.config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("within(wooni.spring.aop.service..*) && !@annotation(wooni.spring.aop.config.LogExecution)")
    public void serviceMethods() {
    }

    @Before("serviceMethods()")
    public void logBeforeMethodExecution(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().toShortString();
        Object[] methodArgs = joinPoint.getArgs();
        log.info("============ Before ============");
        log.info("=======> MethodName: {}", methodName);
        if (log.isInfoEnabled()) {
        log.info("=======> Arguments: {}", Arrays.toString(methodArgs));
        }
        log.info("================================");
    }

    @Around("serviceMethods()")
    public Object advice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        log.info("=======> startTim: {}", startTime);
        Object proceed = proceedingJoinPoint.proceed();
        long endTime = System.currentTimeMillis();
        log.info("=======> endTime: {}", endTime);
        long executionTime = endTime - startTime;
        log.info("=======> executionTime: {}", executionTime);
        return proceed;
    }

    @After("serviceMethods()")
    public void logAfterExecution() {
        log.info("============ After ============");
    }
}