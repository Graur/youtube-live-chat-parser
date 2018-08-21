package ru.teabull.shedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.teabull.model.Client;
import ru.teabull.model.YoutubeClient;
import ru.teabull.service.client.interfaces.ClientService;
import ru.teabull.service.vk.interfaces.VkService;
import ru.teabull.service.youtube.interfaces.YoutubeClientService;
import ru.teabull.service.youtube.interfaces.YoutubeService;

import java.util.List;
import java.util.Optional;

@Component
@EnableScheduling
public class ScheduleTask {

    private final YoutubeService youtubeService;

    private final VkService vkService;

    private final YoutubeClientService youtubeClientService;

    private final ClientService clientService;

    @Autowired
    public ScheduleTask(YoutubeService youtubeService, VkService vkService, YoutubeClientService youtubeClientService, ClientService clientService) {
        this.youtubeService = youtubeService;
        this.vkService = vkService;
        this.youtubeClientService = youtubeClientService;
        this.clientService = clientService;
    }

    @Scheduled(fixedRate = 6_000)
    private void handleYoutubeLiveStreams() {
        if (!youtubeService.checkLiveStreamStatus()) {
            youtubeService.handleYoutubeLiveChatMessages();
        } else {
            Optional<List<YoutubeClient>> youtubeClient = Optional.of(youtubeClientService.findAll());
            if (youtubeClient.isPresent()) {
                for (YoutubeClient client : youtubeClient.get()) {
                    Optional<Client> newClient = vkService.getClientFromYoutubeLiveStreamByName(client.getFullName());
                    if (newClient.isPresent()) {
                        clientService.update(newClient.get());
                    } else {
                        clientService.add(newClient.get());
                    }
                }
            }
        }
    }
}
