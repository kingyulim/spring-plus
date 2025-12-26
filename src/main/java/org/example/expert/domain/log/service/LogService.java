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
     * 로그 데이터를 저장한다.
     * 이 메서드는 {@code Propagation.REQUIRES_NEW} 트랜잭션을 사용하여
     * 호출한 쪽의 트랜잭션과 완전히 분리된 새로운 트랜잭션에서 실행된다.
     * 따라서 상위 비즈니스 로직이 롤백되더라도
     * 로그 데이터는 정상적으로 DB에 저장된다.
     * (에러 로그, 감사 로그 등 실패와 무관하게 남겨야 하는 경우에 사용)
     *
     * @param log 저장할 로그 엔티티
     * @return 저장된 로그 엔티티
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Log logSave(Log log) {
        return logRepository.save(log);
    }
}
