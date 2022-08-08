package com.kartikey.rest.webservices.restfulwebservices.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Component
public class UserDaoService {

    private static List<UserEntity> users = new ArrayList<>();
    private static Integer userCount = 4;

    static {
        users.add(new UserEntity(1, "Kartikey", new Date()));
        users.add(new UserEntity(2, "Dheeraj", new Date()));
        users.add(new UserEntity(3, "Sandhya", new Date()));
        users.add(new UserEntity(4, "Winnie", new Date()));
    }

    @Autowired
    private UserRepository repository;

    public List<UserEntity> findAll() {
        List<UserEntity> userList = users;
        if (userList.size() == 0) {
            throw new UserNotFoundException("No users found");
        }

        return userList;
    }

    public UserEntity save(UserEntity user) {
        if (user.getId() == null) {
            user.setId(++userCount);
        }

        users.add(user);

        return user;
    }

    public UserEntity findById(Integer id) {
        UserEntity foundUser = null;
        for (UserEntity user : users) {
            if (Objects.equals(user.getId(), id)) {
                foundUser = user;
            }
        }

        if (foundUser == null) {
            throw new UserNotFoundException("User " + id + " not found.");
        }

        return foundUser;
    }

    public void deleteById(Integer id) {
        users.remove(findById(id));
    }

}
