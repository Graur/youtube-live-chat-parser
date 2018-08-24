package ru.teabull.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false, unique = true)
    private String fullName;

    @Column
    private String vkLink;

    public Client() {
    }

    public Client(String fullName, String vkLink) {
        this.fullName = fullName;
        this.vkLink = vkLink;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getVkLink() {
        return vkLink;
    }

    public void setVkLink(String vkLink) {
        this.vkLink = vkLink;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return id == client.id &&
                Objects.equals(fullName, client.fullName) &&
                Objects.equals(vkLink, client.vkLink);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, fullName, vkLink);
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", vkLink='" + vkLink + '\'' +
                '}';
    }
}