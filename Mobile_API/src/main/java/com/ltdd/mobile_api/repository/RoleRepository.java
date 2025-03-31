package com.ltdd.mobile_api.repository;

import com.ltdd.mobile_api.model.ERole;
import com.ltdd.mobile_api.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(ERole name);
}