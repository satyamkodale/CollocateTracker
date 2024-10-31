package com.be.service;

import com.be.entity.User;

public interface UserService {

    public User findUserProfileByJwt(String jwt) throws Exception;

    public User findUserByEmail(String email) throws Exception;

    public User findUserById(Long userId) throws Exception;

    public User updateUsersProjectSize(User user,int number);

}
