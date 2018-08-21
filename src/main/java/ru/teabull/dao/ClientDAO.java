package ru.teabull.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.teabull.model.Client;

import java.util.List;

@Repository
public interface ClientDAO extends CrudRepository<Client, Long> {
    List<Client> findByFullName(String title);
}
