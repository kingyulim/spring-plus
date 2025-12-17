package org.example.expert;

import lombok.RequiredArgsConstructor;
import org.example.expert.client.WeatherClient;
import org.example.expert.config.PasswordEncoder;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TodoRepository todoRepository;
    private final WeatherClient weatherClient;

    @Override
    public void run(String... args) throws Exception {

        // 유저 없으면 생성
        if (userRepository.count() == 0) {
            User u1 = new User("user1@test.com", passwordEncoder.encode("1234"), "테스트닉네임1", UserRole.USER);
            User u2 = new User("user2@test.com", passwordEncoder.encode("1234"), "테스트닉네임2", UserRole.USER);

            userRepository.saveAll(List.of(u1, u2));
        }


        if (todoRepository.count() == 0) {

            User user1 = userRepository.findByEmail("user1@test.com")
                    .orElseThrow();

            List<Todo> todos = List.of(
                    new Todo("todo1 제목", "todo1 내용", weatherClient.getTodayWeather(), user1),
                    new Todo("todo2 제목", "todo2 내용", weatherClient.getTodayWeather(), user1),
                    new Todo("todo3 제목", "todo3 내용", weatherClient.getTodayWeather(), user1)
            );

            todoRepository.saveAll(todos);
        }
    }
}
