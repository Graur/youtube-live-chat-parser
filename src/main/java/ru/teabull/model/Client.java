package ru.teabull.model;

import javax.persistence.*;

@Entity
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false, unique = true)
    private String fullName;

    @Column
    private String message;

    @Column
    private String vkLink;
}