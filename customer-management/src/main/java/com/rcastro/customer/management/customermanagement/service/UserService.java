package com.rcastro.customer.management.customermanagement.service;

import com.rcastro.customer.management.customermanagement.domain.User;
import com.rcastro.customer.management.customermanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public Long nextSequence() {
        List<User> all = repository.findAll();
        int next = 1;
        if (!CollectionUtils.isEmpty(all)) {
            next = all.size() + 1;
        }
        return Long.valueOf(next);
    }

    public User create(String name, LocalDate birthday) {
        final User user = new User();
        user.setBirthday(birthday);
        user.setId(nextSequence());
        user.setName(name);
        user.setCreated(LocalDateTime.now());

        return repository.save(user);
    }

    public Optional<User> findUser(Long id) {
        if (Optional.ofNullable(id).isPresent()) {
            return repository.findById(id);
        }

        return Optional.empty();
    }

    public void update(User user) {
        repository.save(user);
    }

    public List<User> list(Pageable pagination) {
        List<User> users = new ArrayList<>();

        Page<User> all = repository.findAll(pagination);
        if (all.getTotalElements() > 0) {
            users.addAll(all.getContent());
        }

        return users;
    }
}
