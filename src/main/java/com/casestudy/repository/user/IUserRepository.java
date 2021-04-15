package com.casestudy.repository.user;

import com.casestudy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends JpaRepository<User,Long> {
    @Query("SELECT u FROM User u WHERE u.username = :username")
    public User getUserByUserName(@Param("username") String username);
}
