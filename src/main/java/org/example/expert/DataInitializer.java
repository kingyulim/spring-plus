package org.example.expert;

import lombok.RequiredArgsConstructor;
import org.example.expert.client.WeatherClient;
import org.example.expert.config.PasswordEncoder;
import org.example.expert.domain.comment.entity.Comment;
import org.example.expert.domain.comment.repository.CommentRepository;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TodoRepository todoRepository;
    private final WeatherClient weatherClient;
    private final CommentRepository commentRepository;

    @Override
    public void run(String... args) throws Exception {

        // 유저 없으면 생성
        if (userRepository.count() == 0) {
            List<User> users = List.of(
                new User("admin@test.com", passwordEncoder.encode("1234"), "관리자", UserRole.ADMIN),
                new User("user1@test.com", passwordEncoder.encode("1234"), "테스트닉네임1", UserRole.USER),
                new User("user2@test.com", passwordEncoder.encode("1234"), "테스트닉네임2", UserRole.USER)
            );

            userRepository.saveAll(users);
        }


        if (todoRepository.count() == 0) {

            List<User> users = userRepository.findAll();

            List<Todo> todos = new ArrayList<>();

            for (User user : users) {
                if (user.getRole() == UserRole.USER) {
                    todos.add(new Todo(
                            user.getNickname() + "의 todo 1",
                            "내용 1",
                            weatherClient.getTodayWeather(),
                            user
                    ));
                    todos.add(new Todo(
                            user.getNickname() + "의 todo 2",
                            "내용 2",
                            weatherClient.getTodayWeather(),
                            user
                    ));
                    todos.add(new Todo(
                            user.getNickname() + "의 todo 3",
                            "내용 3",
                            weatherClient.getTodayWeather(),
                            user
                    ));
                }
            }

            todoRepository.saveAll(todos);
        }

        if (commentRepository.count() == 0) {

            User user1 = userRepository.findByEmail("user1@test.com")
                    .orElseThrow();
            User user2 = userRepository.findByEmail("user2@test.com")
                    .orElseThrow();

            List<Todo> todos = todoRepository.findAll();
            List<Comment> comments = new ArrayList<>();

            for (Todo todo : todos) {
                comments.add(new Comment(
                        "첫 번째 댓글입니다.",
                        user1,
                        todo
                ));
                comments.add(new Comment(
                        "두 번째 댓글입니다.",
                        user2,
                        todo
                ));
            }

            commentRepository.saveAll(comments);
        }

    }
}
