package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.Comparator;

@Repository
public class InMemoryUserRepository extends AbstractInMemorySortedRepository<User> implements UserRepository {

    public InMemoryUserRepository() {
        defaultComparator = Comparator.comparing(User::getName);

        save(new User(null,
                "testUser",
                "user@email.com",
                "password",
                Role.USER, Role.ADMIN));
    }

    @Override
    public User getByEmail(String email) {
        return repository.values()
                .stream()
                .filter(user -> user.getPassword().equals(email))
                .findFirst()
                .orElse(null);
    }
}
