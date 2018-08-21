package ru.teabull.service.client.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.teabull.model.Client;
import ru.teabull.repository.interfaces.ClientDAO;
import ru.teabull.service.client.interfaces.ClientService;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientDAO clientDAO;

    @Autowired
    public ClientServiceImpl(ClientDAO clientDAO) {
        this.clientDAO = clientDAO;
    }

    @Override
    public void add(Client client) {
        clientDAO.saveAndFlush(client);
    }

    @Override
    public void update(Client client) {
        clientDAO.saveAndFlush(client);
    }
}
