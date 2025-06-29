package com.lbg.iau.repository;

import com.lbg.iau.model.UserCredential;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserCredentialRepository extends JpaRepository<UserCredential, String> {
    List<UserCredential> findByUserId(String userId);
}
