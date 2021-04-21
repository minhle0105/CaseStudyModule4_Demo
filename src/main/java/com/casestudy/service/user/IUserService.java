package com.casestudy.service.user;

import com.casestudy.model.User;
import com.casestudy.service.IGeneralService;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface IUserService extends IGeneralService<User> {
    User findByUsername(String username);
}