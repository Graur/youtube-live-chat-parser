package ru.teabull.service.youtube.interfaces;

public interface YoutubeService {

    void handleYoutubeLiveChatMessages();

    boolean checkLiveStreamStatus();
}
