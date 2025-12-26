package org.example.expert.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.example.expert.domain.log.service.LogService;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class ManagerRegisterLoggingAspect {

    private final LogService logService;

    @Around("execution(* org.example.expert.domain.manager.controller.ManagerController.saveManager(..))")
    public Object managerRegisterLog(ProceedingJoinPoint joinPoint) throws Throwable {
        return joinPoint;
    }
}
