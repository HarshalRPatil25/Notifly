package com.notifly.backend.User.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.notifly.backend.User.Entity.User;


@Repository
public interface UserRepository extends JpaRepository<User,Integer>{
    
}
