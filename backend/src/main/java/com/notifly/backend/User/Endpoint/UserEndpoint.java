package com.notifly.backend.User.Endpoint;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.notifly.backend.JobPreferences.Entity.JobPreference;
import com.notifly.backend.User.DTO.UserDTO;
import com.notifly.backend.User.Entity.User;
import com.notifly.backend.User.Repository.UserRepository;
import com.notifly.backend.User.Response.UserResponse;
import com.notifly.backend.User.Service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserEndpoint {

    private final UserRepository userRepository;
    private final UserService userService;


public String getUserName() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    if (auth == null || !auth.isAuthenticated()) {
        return null;
    }

    if (auth.getPrincipal().equals("anonymousUser")) {
        return null;
    }

    return auth.getName();
}


    public final User getUser(){
        String username=getUserName();
        if(username!=null){
            Optional<User> user=userRepository.findByUsername(username);
           return user.orElse(null);

        }
        return null;
    }


    @PostMapping("/job-preference")
    public ResponseEntity<?> addUserJobPrefences(@Valid @RequestBody List<JobPreference>jobPreferenceList,BindingResult result){

        if(result.hasErrors()==false){
        if(getUserName()!=null){
            User currentUser=getUser();
            UserDTO user=userService.userJobPreferencesAdded(currentUser.getId(),jobPreferenceList);
            if(user!=null){
                return ResponseEntity.status(200).body(new UserResponse("200","Job-Preferences added",user));
            }
            return ResponseEntity.status(500).body(new UserResponse("501","Something went wrong",null));
               
        }
        return ResponseEntity.status(401).body(new UserResponse("401","User not Authenticated",null));

    }
            return ResponseEntity.status(500).body(new UserResponse("501","Something went wrong",null));


}

@GetMapping("/job-preferences")
public ResponseEntity<UserResponse> getUserPrefences(){
    User user=getUser();
    if(user!=null){
        UserDTO users=userService.getUserJobPreferences(user.getId());
        if(users!=null){
         
             return ResponseEntity.status(200).body(new UserResponse("200","Users job preferences:",users));
        }
        else{
                return ResponseEntity.status(500).body(new UserResponse("501","Something went wrong",null));
 }

        
    }
    return ResponseEntity.status(401).body(new UserResponse("401","Unauthorized",null));
}
    
}
