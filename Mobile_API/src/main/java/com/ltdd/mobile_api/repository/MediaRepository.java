package com.ltdd.mobile_api.repository;

import com.ltdd.mobile_api.model.Media;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MediaRepository extends JpaRepository<Media, Long> {
    List<Media> findAllByOwnerUsername(String ownerUsername);

    Optional<Media> findByIdAndOwnerUsername(Long id, String ownerUsername);
}