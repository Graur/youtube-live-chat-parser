package ru.teabull.service.vk.interfaces;

import ru.teabull.model.Client;

import java.util.Optional;

public interface VkService {

    Optional<Client> getClientFromYoutubeLiveStreamByName(String name);
}
