package com.openmoments.scytale.entities;

import com.fasterxml.jackson.annotation.JsonFilter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"name"})})
@JsonFilter("clientFilter")
public class Client {
    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToOne
    @JoinColumn(name = "auth_type")
    private AuthType authType;
    private String apiKey = "";
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>();

    public Client() {}

    public Client(String name, AuthType authType, String apiKey) {
        this(name, authType, apiKey, ROLE_USER);
    }

    public Client(String name, AuthType authType, String apiKey, String role) {
        this.name = name;
        this.authType = authType;
        this.apiKey = apiKey;
        this.roles.add(role);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AuthType getAuthType() {
        return authType;
    }

    public void setAuthType(AuthType authType) {
        this.authType = authType;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRole(List<String> roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return id.equals(client.id) &&
                name.equals(client.name) &&
                authType.equals(client.authType) &&
                Objects.equals(apiKey, client.apiKey) &&
                roles.equals(client.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, authType, apiKey, roles);
    }

    @Override
    public String toString() {
        String tempAPIKey = apiKey.isEmpty() ? "" : "******************";

        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", authType=" + authType +
                ", apiKey='" + tempAPIKey + '\'' +
                ", roles=" + roles +
                '}';
    }
}
