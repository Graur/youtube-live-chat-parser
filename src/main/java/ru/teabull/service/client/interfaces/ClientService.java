package ru.teabull.service.client.interfaces;

import ru.teabull.model.Client;

import java.util.List;

public interface ClientService {

    void add(Client client);

    void update(Client client);

    List<Client> findAll();
}
