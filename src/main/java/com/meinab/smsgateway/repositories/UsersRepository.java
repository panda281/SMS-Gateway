package com.meinab.smsgateway.repositories;

import com.meinab.smsgateway.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users,Integer> {
    Optional<Users> findFirstByUserName(String userName);
}
