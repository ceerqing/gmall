package com.atguigu.mall.cache.cache.aspect;


import com.atguigu.mall.cache.cache.annotation.GmallCache;
import com.atguigu.mall.cache.cache.constant.SysRedisConstant;
import com.atguigu.mall.cache.cache.service.CacheOpsService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * Author：张世平
 * Date：2022/9/7 10:04
 */
@Aspect
@Component
public class CacheAspect {
        //解析对象，线程安全
    ExpressionParser parser=new SpelExpressionParser();

    TemplateParserContext template=new TemplateParserContext();

    @Autowired
    CacheOpsService cacheOpsService;

    @Around("@annotation(com.atguigu.mall.cache.cache.annotation.GmallCache)")
    public Object addCacheAspect(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = null;

        String cacheKey = getCacheKey(joinPoint);
        Type type = getReturnType(joinPoint);
        //查缓存
        Class cls = getResultClass(joinPoint);
        result = cacheOpsService.getCacheData(cacheKey, type, cls);

        //没命中，过布隆过滤器
        if (result == null) {
            //有些场景不使用布隆过滤器，直接枪锁查数据库
            String bloomName = getBloomName(joinPoint);
            if (!StringUtils.isEmpty(bloomName)) {
                Object inBloomValue = getInBloomValue(joinPoint);
                boolean flag = cacheOpsService.boolmContain(bloomName, inBloomValue);
                if (!flag) {
                    return null;
                }
            }
            //缓存没有。布隆没有使用布隆过滤器或者布隆过滤器判段可能会有
            String lockName = null;
            boolean isGetLock = false;
            try {
                lockName = getLockName(joinPoint);
                isGetLock = cacheOpsService.tryLock(lockName);
                if (isGetLock) {
                    //回源
                    Object proceed = joinPoint.proceed(joinPoint.getArgs());
                    Long ttl = getCacheTtl(joinPoint);
                    cacheOpsService.saveCacheData(cacheKey, proceed, ttl);
                    return proceed;
                } else {
                    Thread.sleep(1000);
                    return cacheOpsService.getCacheData(cacheKey, type, cls);
                }
            } finally {
                if (isGetLock) cacheOpsService.unLock(lockName);
            }

            //布隆过滤器判断可能存在，抢分布式锁调用被代理的方
        }


        return result;
    }

    private Long getCacheTtl(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        GmallCache annotation = signature.getMethod().getAnnotation(GmallCache.class);
        long ttl = annotation.ttl();
        return ttl;
    }

    private String getLockName(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        GmallCache annotation = signature.getMethod().getAnnotation(GmallCache.class);
        String parExp = annotation.lockName();
        if (parExp==""){
            return SysRedisConstant.U_LOCK+signature.getMethod().getName();
        }
        //解析每个特定的id，一个id一个lock
        String lockName = getValue(parExp, joinPoint, String.class);
        return lockName;
    }

    private Object getInBloomValue(ProceedingJoinPoint joinPoint) {
        MethodSignature  signature = (MethodSignature) joinPoint.getSignature();
        GmallCache annotation = signature.getMethod().getAnnotation(GmallCache.class);
        String parExp = annotation.bloomValue();
        Object bloomVale = getValue(parExp, joinPoint, Object.class);
        return bloomVale;
    }

    private String getBloomName(ProceedingJoinPoint joinPoint) {
        MethodSignature  signature = (MethodSignature) joinPoint.getSignature();
        GmallCache annotation = signature.getMethod().getAnnotation(GmallCache.class);
        String annoValue = annotation.bloomName();
        String bloomName = getValue(annoValue, joinPoint, String.class);
        return bloomName;
    }

    private Class getResultClass(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        GmallCache annotation = signature.getMethod().getAnnotation(GmallCache.class);
        Class cls = annotation.cls();
        return cls;
    }

    private Type getReturnType(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Type returnType = signature.getMethod().getGenericReturnType();
        return returnType;
    }

    private String getCacheKey(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        GmallCache annotation = method.getAnnotation(GmallCache.class);
        String exps = annotation.cacheKey();
        String cacheKey = getValue(exps, joinPoint, String.class);
        return cacheKey;
    }

    private<T> T getValue(String exps, ProceedingJoinPoint joinPoint,Class<T> cls) {
        Expression expression = parser.parseExpression(exps, template);
        Object[] args = joinPoint.getArgs();
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariable("params",args);
        T value = expression.getValue(context, cls);
        return value;
    }


}
