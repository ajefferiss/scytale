package com.openmoments.scytale.entities;

import com.openmoments.scytale.authentication.AuthenticationType;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class AuthType {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private AuthenticationType authType;

    AuthType() {}

    public AuthType(AuthenticationType authenticationType) {
        this.authType = authenticationType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AuthenticationType getAuthType() {
        return authType;
    }

    public void setAuthType(AuthenticationType authType) {
        this.authType = authType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthType authType1 = (AuthType) o;
        return id.equals(authType1.id) &&
                authType == authType1.authType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, authType);
    }

    @Override
    public String toString() {
        return "AuthType{" + "id=" + id + ", authType=" + authType + "}";
    }
}
