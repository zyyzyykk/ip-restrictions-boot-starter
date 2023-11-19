package com.kkbapps.iprestrictionsbootstarter.Aspect;


import com.kkbapps.iprestrictionsbootstarter.Annotation.EnableIPLimit;
import com.kkbapps.iprestrictionsbootstarter.Exception.IpRequestErrorException;
import com.kkbapps.iprestrictionsbootstarter.Service.IpHandleService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
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

    @Before("requestInterceptor()")
    public void DoInterceptor(JoinPoint point) throws NoSuchMethodException, IpRequestErrorException {
        Object target = point.getTarget();
        Object[] args = point.getArgs();
        String methodName = point.getSignature().getName();
        Class<?>[] parameterTypes = ((MethodSignature) point.getSignature()).getMethod().getParameterTypes();
        Method method = target.getClass().getMethod(methodName, parameterTypes);
        EnableIPLimit enableIpLimit = method.getAnnotation(EnableIPLimit.class);

        if(null == enableIpLimit) return;

        // 进行IP检测与拦截
        ipHandleService.ipVerification(method, enableIpLimit);
    }



}
