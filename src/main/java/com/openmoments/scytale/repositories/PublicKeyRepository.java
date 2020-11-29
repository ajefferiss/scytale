package com.openmoments.scytale.repositories;

import com.openmoments.scytale.entities.PublicKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PublicKeyRepository extends JpaRepository<PublicKey, Long> {
}
