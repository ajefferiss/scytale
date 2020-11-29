package com.openmoments.scytale.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class PublicKey {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "keystore_id")
    private Keystore keystore;

    @Column(nullable = false, unique = true)
    private String publicKey;

    public PublicKey() {}

    PublicKey(Keystore keystore, String publicKey) {
        this.keystore = keystore;
        this.publicKey = publicKey;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Keystore getKeystore() {
        return keystore;
    }

    public void setKeystore(Keystore keystore) {
        this.keystore = keystore;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PublicKey publicKey1 = (PublicKey) o;
        return id.equals(publicKey1.id) &&
                keystore.equals(publicKey1.keystore) &&
                publicKey.equals(publicKey1.publicKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, keystore, publicKey);
    }

    @Override
    public String toString() {
        return "PublicKey{" +
                "id=" + id +
                ", keystore=" + keystore +
                ", publicKey='" + publicKey + "'}";
    }
}
