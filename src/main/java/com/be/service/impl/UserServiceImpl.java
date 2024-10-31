package com.be.service.impl;

import com.be.config.JwtProvider;
import com.be.entity.User;
import com.be.repository.UserRepository;
import com.be.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;



@Service
public class UserServiceImpl implements UserService {

    @Autowired
   private UserRepository userRepository;

    @Override
    public User findUserProfileByJwt(String jwt) throws Exception {
        String email = JwtProvider.getEmailFromJwtToken(jwt);

        User user = userRepository.findByEmail(email);

//		int projectSize=projectService.getProjectsByTeam(user,null,null).size();
//		user.setProjectSize(projectSize);

        userRepository.save(user);

        if (user == null) {
            throw new Exception("user not exist with email " + email);
        }
        return user;
    }

    @Override
    public User findUserByEmail(String email) throws Exception {
        User user = userRepository.findByEmail(email);

        if (user != null) {

            return user;
        }

        throw new Exception("user not exist with username " + email);
    }

    @Override
    public User findUserById(Long userId) throws Exception {
        Optional<User> opt = userRepository.findById(userId);

        if (opt.isEmpty()) {
            throw new Exception("user not found with id " + userId);
        }
        return opt.get();
    }

    @Override
    public User updateUsersProjectSize(User user, int number) {
        user.setProjectSize(user.getProjectSize()+number);
        if(user.getProjectSize()==-1){
            return user;
        }
        return userRepository.save(user);
    }
}
