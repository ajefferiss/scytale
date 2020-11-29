package com.openmoments.scytale.repositories;

import com.openmoments.scytale.entities.Keystore;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KeystoreRepository extends JpaRepository<Keystore, Long> {
}
