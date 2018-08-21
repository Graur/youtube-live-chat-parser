package ru.teabull.service.youtube.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.teabull.model.YoutubeClient;
import ru.teabull.repository.interfaces.YoutubeClientDAO;
import ru.teabull.service.youtube.interfaces.YoutubeClientService;

import java.util.List;

@Service
public class YoutubeClientServiceImpl implements YoutubeClientService {
    private final YoutubeClientDAO youtubeClientDAO;

    @Autowired
    public YoutubeClientServiceImpl(YoutubeClientDAO youtubeClientDAO) {
        this.youtubeClientDAO = youtubeClientDAO;
    }

    @Override
    public void add(YoutubeClient youtubeClient) {
        youtubeClientDAO.saveAndFlush(youtubeClient);
    }

    @Override
    public List<YoutubeClient> findAll() {
        return youtubeClientDAO.findAll();
    }

    @Override
    public YoutubeClient findByName(String name) {
        return youtubeClientDAO.findByFullName(name);
    }

    @Override
    public void update(YoutubeClient youtubeClient) {
        youtubeClientDAO.saveAndFlush(youtubeClient);
    }
}