package com.casestudy.service.user;

import com.casestudy.model.User;
import com.casestudy.repository.user.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public Page<User> findAll(Pageable pageable) {
        return null;
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
    public User findByUsername(String username) {
        return userRepository.getUserByUserName(username);
    }

    public boolean validatePayment(String creditCardNumber) {
        boolean cardIsValid;
        int counter1 = creditCardNumber.length() - 1;
        int counter2 = creditCardNumber.length() - 2;
        int sum1 = 0;
        int sum2 = 0;
        ArrayList<Integer> outcome = new ArrayList<>();
        while (counter1 >= 0) {
            sum1 += Character.getNumericValue(creditCardNumber.charAt(counter1));
            counter1 = counter1 - 2;
        }
        while (counter2 >= 0) {
            if (Character.getNumericValue(creditCardNumber.charAt(counter2)) * 2 > 9) {
                outcome.add(Character.getNumericValue(creditCardNumber.charAt(counter2)) * 2 - 9);
            }
            else {
                outcome.add(Character.getNumericValue(creditCardNumber.charAt(counter2)) * 2);
            }
            counter2 -= 2;
        }
        for (int i = 0; i < outcome.size(); i++) {
            sum2 += outcome.get(i);
        }

        if ((sum1+sum2) % 10 == 0) {
            cardIsValid = true;
        }
        else {
            cardIsValid = false;
        }
        return cardIsValid;
    }
}
