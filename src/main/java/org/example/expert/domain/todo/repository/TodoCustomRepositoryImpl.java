package org.example.expert.domain.todo.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.expert.domain.comment.entity.QComment;
import org.example.expert.domain.manager.entity.QManager;
import org.example.expert.domain.todo.dto.request.TodoSearchRequest;
import org.example.expert.domain.todo.dto.response.TodoSearchResponse;
import org.example.expert.domain.todo.entity.QTodo;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.user.entity.QUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class TodoCustomRepositoryImpl implements TodoCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<Todo> TodoFindByIdWithUser(Long todoId) {
        QTodo todo = QTodo.todo;
        QUser user = QUser.user;

        Todo result = jpaQueryFactory
                .selectFrom(todo)
                .leftJoin(todo.user, user).fetchJoin()
                .where(todo.id.eq(todoId))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    /**
     * 일정 검색 필터링 queryDSL
     * @param request 검색 파라미터
     * @param pageable pageable 파라미터
     * @return TodoSearchResponse api json 반환
     */
    @Override
    public Page<TodoSearchResponse> todoSearch(TodoSearchRequest request, Pageable pageable) {
        QTodo todo = QTodo.todo;
        QUser user = QUser.user;
        QManager manager = QManager.manager;
        QComment comment = QComment.comment;

        BooleanBuilder builder = new BooleanBuilder();

        // 제목 검색
        if (request.getTitle() != null && !request.getTitle().isBlank()) {
            builder.and(todo.title.contains(request.getTitle()));
        }

        // 생성 날짜 기준 검색
        if (request.getCreateAt() != null) {
            builder.and(todo.createdAt.goe(request.getCreateAt().atStartOfDay())); // 2025-00-00 으로 검색 가능 하게 포멧
        }

        // 닉네임 검색
        if (request.getNickName() != null && !request.getNickName().isBlank()) {
            builder.and(user.nickname.contains(request.getNickName()));
        }

        List<TodoSearchResponse> result =  jpaQueryFactory
                .select(Projections.constructor(
                        TodoSearchResponse.class,
                        todo.title,
                        comment.id.countDistinct(),
                        manager.id.countDistinct()
                ))
                .from(todo)
                .leftJoin(todo.comments, comment)
                .leftJoin(todo.managers, manager)
                .leftJoin(todo.user, user)
                .where(builder)
                .groupBy(todo.id)
                .orderBy(todo.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = jpaQueryFactory
                .select(todo.id.countDistinct())
                .from(todo)
                .leftJoin(todo.comments, comment)
                .leftJoin(todo.managers, manager)
                .leftJoin(todo.user, user)
                .where(builder)
                .fetchOne();

        if (total == null) {
            total = 0L;
        }

        return new PageImpl<>(result, pageable, total);
    }
}
