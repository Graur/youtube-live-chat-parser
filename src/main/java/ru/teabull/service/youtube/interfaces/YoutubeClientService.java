package ru.teabull.service.youtube.interfaces;

import ru.teabull.model.YoutubeClient;

import java.util.List;

public interface YoutubeClientService {

    void add(YoutubeClient youtubeClient);

    List<YoutubeClient> findAll();

    YoutubeClient findByName(String name);

    void update(YoutubeClient youtubeClient);
}
