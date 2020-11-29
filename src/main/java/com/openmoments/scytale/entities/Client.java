package com.openmoments.scytale.entities;

import com.fasterxml.jackson.annotation.JsonFilter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"name"})})
@JsonFilter("clientFilter")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToOne
    @JoinColumn(name = "auth_type")
    private AuthType authType;
    private String apiKey;

    Client() {}

    public Client(String name, AuthType authType, String apiKey) {
        this.name = name;
        this.authType = authType;
        this.apiKey = apiKey;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return id.equals(client.id) &&
                name.equals(client.name) &&
                authType.equals(client.authType) &&
                Objects.equals(apiKey, client.apiKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, authType, apiKey);
    }

    @Override
    public String toString() {
        String tempAPIKey = apiKey.isEmpty() ? "" : "******************";

        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", authType=" + authType +
                ", apiKey='" + tempAPIKey + '\'' +
                '}';
    }
}
