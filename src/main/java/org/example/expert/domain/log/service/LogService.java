package org.example.expert.domain.log.service;

import lombok.RequiredArgsConstructor;
import org.example.expert.domain.log.entity.Log;
import org.example.expert.domain.log.repository.LogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LogService {

    private final LogRepository logRepository;

    /**
     * 시스템 동작에 대한 로그를 저장한다.
     *
     * 이 메서드는 {@code Propagation.REQUIRES_NEW} 트랜잭션을 사용하여
     * 호출한 쪽의 트랜잭션과 분리된 새로운 트랜잭션에서 실행된다.
     * 따라서 상위 비즈니스 로직이 예외로 인해 롤백되더라도
     * 로그 데이터는 영향을 받지 않고 정상적으로 DB에 저장된다.
     * 에러 로그, 감사 로그, 관리자 행위 기록 등
     * 실패 여부와 관계없이 반드시 남겨야 하는 로그 처리에 사용된다.
     *
     *
     * @param action  수행된 동작의 종류 (예: CREATE_MANAGER, UPDATE_MANAGER)
     * @param message 로그에 남길 상세 메시지
     * @param success 동작의 성공 여부 (true: 성공, false: 실패)
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logSave(String action, String message, boolean success) {
        logRepository.save(new Log(action, message, success));
    }
}
