package ru.teabull.service.client.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.teabull.model.Client;
import ru.teabull.repository.interfaces.ClientRepository;
import ru.teabull.service.client.interfaces.ClientService;

import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public void add(Client client) {
        clientRepository.saveAndFlush(client);
    }

    @Override
    public void update(Client client) {
        clientRepository.saveAndFlush(client);
    }

    public List<Client> findAll() {
        return clientRepository.findAll();
    }
}
