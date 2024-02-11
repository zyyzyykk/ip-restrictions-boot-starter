package com.kkbapps.iprestrictionsbootstarter.Aspect;


import com.kkbapps.iprestrictionsbootstarter.Annotation.EnableIPLimit;
import com.kkbapps.iprestrictionsbootstarter.Service.IPContext;
import com.kkbapps.iprestrictionsbootstarter.Service.IpHandleService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Order(1)
@Component
public class GlobalIPInterceptor {

    @Autowired
    private IpHandleService ipHandleService;

    /**
     * 定义切入点
     */
    @Pointcut("@annotation(com.kkbapps.iprestrictionsbootstarter.Annotation.EnableIPLimit)")
    private void requestInterceptor() {
    }

    @Around("requestInterceptor()")
    public Object DoInterceptor(ProceedingJoinPoint point) throws Throwable {
        Object target = point.getTarget();
        String methodName = point.getSignature().getName();
        Class<?>[] parameterTypes = ((MethodSignature) point.getSignature()).getMethod().getParameterTypes();
        Method method = target.getClass().getMethod(methodName, parameterTypes);
        EnableIPLimit enableIpLimit = method.getAnnotation(EnableIPLimit.class);

        if(enableIpLimit != null) {
            // 进行IP检测与拦截
            ipHandleService.ipVerification(method, enableIpLimit);
        }

        try {
            return point.proceed();
        } finally {
            IPContext.remove();
        }
    }



}
