package org.example.expert.domain.todo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TodoSearchRequest {
    private String title;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) // 2025-00-00 이런형식으로 받기
    private LocalDate createAt;

    private String nickName;
}
