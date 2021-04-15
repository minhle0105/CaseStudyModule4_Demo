package com.casestudy.service.user;

import com.casestudy.model.User;
import com.casestudy.repository.user.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserService implements IUserService {

    @Autowired
    private IUserRepository userRepository;


    @Override
    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public void remove(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public Iterable<User> findByName(String name) {
        Iterable<User> users = findAll();
        ArrayList<User> results = new ArrayList<>();
        for (User u : users) {
            if (u.getName().toLowerCase().contains(name.toLowerCase())) {
                results.add(u);
            }
        }
        return results;
    }
}
