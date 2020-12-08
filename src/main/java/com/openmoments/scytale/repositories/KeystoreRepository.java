package com.openmoments.scytale.repositories;

import com.openmoments.scytale.entities.Keystore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KeystoreRepository extends JpaRepository<Keystore, Long> {

    List<Keystore> findByNameContaining(String name);
}
