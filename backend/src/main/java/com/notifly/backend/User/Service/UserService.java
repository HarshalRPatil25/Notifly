package com.notifly.backend.User.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.notifly.backend.User.Entity.User;
import com.notifly.backend.User.Repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {



     @Autowired
     private UserRepository userRepository;



    @Transactional
    public boolean saveUser(User user) {
      //Save user logic
      if(user!=null){
          // Prevent creating duplicate username
          if(userRepository.existsByUsername(user.getUsername())){
              return false; // caller should handle conflict (409)
          }

          User SavedUser=userRepository.save(user);
          if(SavedUser!=null){
              return true;
          }
      }

      return false;
    }

    public boolean userExists(String username){
        return userRepository.existsByUsername(username);
    }
    
}
