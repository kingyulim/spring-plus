package org.example.expert.domain.todo.repository;

import org.example.expert.domain.todo.dto.request.TodoSearchRequest;
import org.example.expert.domain.todo.dto.response.TodoSearchResponse;
import org.example.expert.domain.todo.entity.Todo;

import java.util.List;
import java.util.Optional;

public interface TodoCustomRepository {
    Optional<Todo> TodoFindByIdWithUser(Long todoId);

    List<TodoSearchResponse> todoSearch(TodoSearchRequest request);
}
