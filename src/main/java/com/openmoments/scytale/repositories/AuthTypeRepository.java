package com.openmoments.scytale.repositories;

import com.openmoments.scytale.authentication.AuthenticationType;
import com.openmoments.scytale.entities.AuthType;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthTypeRepository extends JpaRepository<AuthType, Long> {
    ExampleMatcher AUTH_TYPE_MATCHER = ExampleMatcher.matching().withMatcher("authType", ExampleMatcher.GenericPropertyMatchers.exact());
    Example<AuthType> API_KEY_MATCHER = Example.of(new AuthType(AuthenticationType.API_KEY), AUTH_TYPE_MATCHER);
    Example<AuthType> CERT_MATCHER = Example.of(new AuthType(AuthenticationType.CERTIFICATE), AUTH_TYPE_MATCHER);
}
