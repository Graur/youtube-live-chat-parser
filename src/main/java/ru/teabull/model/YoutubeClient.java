package ru.teabull.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table
public class YoutubeClient {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false, unique = true)
    private String fullName;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        YoutubeClient that = (YoutubeClient) o;
        return id == that.id &&
                Objects.equals(fullName, that.fullName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, fullName);
    }

    @Override
    public String toString() {
        return "YoutubeClient{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                '}';
    }
}
