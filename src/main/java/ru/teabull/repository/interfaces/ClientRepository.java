package ru.teabull.repository.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.teabull.model.Client;

import java.util.List;

public interface ClientRepository extends JpaRepository<Client, Long> {

    List<Client> findAll();
}
