package ru.teabull.repository.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.teabull.model.YoutubeClient;

import java.util.List;

public interface YoutubeClientDAO extends JpaRepository<YoutubeClient, Long> {

    List<YoutubeClient> findAll();

    YoutubeClient findByFullName(String name);
}
