package org.example.expert.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.example.expert.domain.log.service.LogService;
import org.example.expert.domain.manager.dto.request.ManagerSaveRequest;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class ManagerRegisterLoggingAspect {

    private final LogService logService;

    /**
     * 매니저 등록(saveManager) 요청에 대한 성공/실패 로그를 기록하는 AOP 메서드.
     *
     * ManagerController saveManager 실행을 @Around로 감싸
     * 비즈니스 로직 실행 결과에 따라 로그를 남긴다.
     *
     * ProceedingJoinPoint getArgs를 통해 전달된 파라미터 중
     * 타입을 기준으로 todoId와 ManagerSaveRequest를 추출하여
     * 로그에 필요한 정보를 구성한다.
     *
     * 로그 저장은 REQUIRES_NEW 트랜잭션에서 처리되어
     * 상위 트랜잭션 롤백 여부와 관계없이 항상 DB에 기록된다.
     *
     * @param joinPoint AOP에서 가로챈 메서드 실행 정보
     * @return 실제 메서드의 반환값
     * @throws Throwable 실행 중 발생한 예외
     */
    @Around("execution(* org.example.expert.domain.manager.controller.ManagerController.saveManager(..))")
    public Object managerRegisterLog(ProceedingJoinPoint joinPoint) throws Throwable {

        Long todoId = null;
        Long managerUserId = null;

        // 전달된 메서드 파라미터 중 타입을 기준으로 필요한 값 추출
        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof Long) {
                todoId = (Long) arg;
            }

            if (arg instanceof ManagerSaveRequest req) {
                managerUserId = req.getManagerUserId();
            }
        }

        try {
            // 실제 매니저 등록 비즈니스 로직 실행
            Object result = joinPoint.proceed();

            // 매니저 등록 성공 로그 기록
            logService.logSave(
                    "MANAGER_REGISTER",
                    "매니저 등록 성공: todo_id = " + todoId + ", user_id = " + managerUserId,
                    true
            );

            return result;

        } catch (Exception e) {
            // 매니저 등록 실패 로그 기록 (실패 원인 포함)
            logService.logSave(
                    "MANAGER_REGISTER",
                    "매니저 등록 실패: todo_id = " + todoId
                            + ", user_id = " + managerUserId
                            + ", reason = " + e.getMessage(),
                    false
            );

            // 예외를 다시 던져 상위 계층에서 정상적으로 처리되도록 함
            throw e;
        }
    }
}
