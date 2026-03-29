package com.notifly.backend.User.Repository;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.notifly.backend.User.Entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    List<User> findAllByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    // ✅ Fetch user with job preferences (FIXED NAME)
    @Query("""
        SELECT u FROM User u
        LEFT JOIN FETCH u.jobPreference
        WHERE u.id = :id
    """)
    Optional<User> getUserWithJobPreferences(@Param("id") Integer id);

    // ✅ Users having at least one job preference
    @Query("SELECT u FROM User u WHERE u.jobPreference IS NOT EMPTY")
    List<User> findUsersWithJobPreference();
}