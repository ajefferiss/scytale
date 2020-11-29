package com.openmoments.scytale.entities;

import com.fasterxml.jackson.annotation.JsonFilter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@JsonFilter("keystoreFilter")
public class Keystore {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    public Keystore() {}

    Keystore(String name, Client client) {
        this.name = name;
        this.client = client;
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

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Keystore keystore = (Keystore) o;
        return id.equals(keystore.id) &&
                name.equals(keystore.name) &&
                client.equals(keystore.client);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, client);
    }

    @Override
    public String toString() {
        return "Keystore{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", client=" + client +
                '}';
    }
}
