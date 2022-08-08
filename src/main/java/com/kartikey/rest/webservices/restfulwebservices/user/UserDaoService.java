package com.kartikey.rest.webservices.restfulwebservices.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Component
public class UserDaoService {

    private static List<User> users = new ArrayList<>();
    private static Integer userCount = 4;

    static {
        users.add(new User(1, "Kartikey", new Date(), new ArrayList<>()));
        users.add(new User(2, "Dheeraj", new Date(), new ArrayList<>()));
        users.add(new User(3, "Sandhya", new Date(), new ArrayList<>()));
        users.add(new User(4, "Winnie", new Date(), new ArrayList<>()));
    }

    @Autowired
    private UserRepository repository;

    public List<User> findAll() {
        List<User> userList = users;
        if (userList.size() == 0) {
            throw new UserNotFoundException("No users found");
        }

        return userList;
    }

    public User save(User user) {
        if (user.getId() == null) {
            user.setId(++userCount);
        }

        users.add(user);

        return user;
    }

    public User findById(Integer id) {
        User foundUser = null;
        for (User user : users) {
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
