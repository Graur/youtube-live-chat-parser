package ru.teabull.repository.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.teabull.model.Client;

import java.util.List;

@Repository
public interface ClientDAO extends JpaRepository<Client, Long> {
    List<Client> findByFullName(String title);
}
