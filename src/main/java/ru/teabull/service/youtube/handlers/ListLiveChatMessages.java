/*
 * Copyright (c) 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package ru.teabull.service.youtube.handlers;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeRequestInitializer;
import com.google.api.services.youtube.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.teabull.model.YoutubeClient;
import ru.teabull.service.youtube.interfaces.YoutubeClientService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@Component
public class ListLiveChatMessages {

    private static final String LIVE_CHAT_FIELDS =
            "items(authorDetails(channelId,displayName,isChatModerator,isChatOwner,isChatSponsor,"
                    + "profileImageUrl),snippet(displayMessage,superChatDetails,publishedAt)),"
                    + "nextPageToken,pollingIntervalMillis";
    private YouTube youtube;
    private boolean isLiveStreamInAction = false;
    private YoutubeClientService youtubeClientService;
    private static Logger logger = LoggerFactory.getLogger(ListLiveChatMessages.class);

    @Autowired
    public ListLiveChatMessages(YoutubeClientService youtubeClientService) {
        this.youtubeClientService = youtubeClientService;
    }

    public void getNamesAndMessagesFromYoutubeLiveStreamByVideoId(String apiKey, String videoId) {

        try {
            // This object is used to make YouTube Data API requests. The last
            // argument is required, but since we don't need anything
            // initialized when the HttpRequest is initialized, we override
            // the interface and provide a no-op function.
            youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), new HttpRequestInitializer() {
                public void initialize(HttpRequest request) throws IOException {
                }
            }).setYouTubeRequestInitializer(new YouTubeRequestInitializer(apiKey)).build();

            // Get the liveChatId
            String liveChatId = getLiveChatId(youtube, videoId);
            if (liveChatId != null) {
                isLiveStreamInAction = true;
                logger.info("Live chat id: " + liveChatId);
            } else {
                isLiveStreamInAction = false;
                logger.error("Unable to find a live chat id");
            }

            // Get live chat messages
            listChatMessages(liveChatId, null, 10);
        } catch (GoogleJsonResponseException e) {
            logger.error("GoogleJsonResponseException code: ", e);
        } catch (IOException e) {
            logger.error("IOException: ", e);
        }
    }

    /**
     * Lists live chat messages, polling at the server supplied interval. Owners and moderators of a
     * live chat will poll at a faster rate.
     *
     * @param liveChatId    The live chat id to list messages from.
     * @param nextPageToken The page token from the previous request, if any.
     * @param delayMs       The delay in milliseconds before making the request.
     */
    private void listChatMessages(
            final String liveChatId,
            final String nextPageToken,
            long delayMs) {
        Timer pollTimer = new Timer();
        pollTimer.schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        try {
                            // Get chat messages from YouTube
                            LiveChatMessageListResponse response = youtube
                                    .liveChatMessages()
                                    .list(liveChatId, "snippet, authorDetails")
                                    .setPageToken(nextPageToken)
                                    .setFields(LIVE_CHAT_FIELDS)
                                    .execute();

                            // Iterate messages and add it to DB
                            List<LiveChatMessage> messages = response.getItems();
                            for (int i = 0; i < messages.size(); i++) {
                                LiveChatMessage message = messages.get(i);
                                LiveChatMessageSnippet snippet = message.getSnippet();
                                addYoutubeClientToDB(message.getAuthorDetails().getDisplayName());
                                System.out.println(buildOutput(
                                        snippet.getDisplayMessage(),
                                        message.getAuthorDetails(),
                                        snippet.getSuperChatDetails()));
                            }

                            // Request the next page of messages
                            listChatMessages(
                                    liveChatId,
                                    response.getNextPageToken(),
                                    response.getPollingIntervalMillis());
                        } catch (GoogleJsonResponseException e) {
                            isLiveStreamInAction = false;
                            logger.error("Youtube Live stream is not in action any more", e);
                        } catch (IOException e) {
                            logger.error("Failed to get list of live names and messages", e);
                        }
                    }
                }, delayMs);
    }

    private void addYoutubeClientToDB(String name) {
        YoutubeClient youtubeClient = youtubeClientService.findByName(name);

        if (youtubeClient != null) {
            youtubeClientService.update(youtubeClient);
            logger.info("YoutubeClient with name{} has been updated from Youtube", youtubeClient.getFullName());
        } else {
            YoutubeClient newYoutubeClient = new YoutubeClient();
            newYoutubeClient.setFullName(name);
            youtubeClientService.add(newYoutubeClient);
            logger.info("YoutubeClient with name{} has been added from Youtube", newYoutubeClient.getFullName());
        }
    }

    private String getLiveChatId(YouTube youtube, String videoId) throws IOException {
        // Get liveChatId from the video
        YouTube.Videos.List videoList = youtube.videos()
                .list("liveStreamingDetails")
                .setFields("items/liveStreamingDetails/activeLiveChatId")
                .setId(videoId);
        VideoListResponse response = videoList.execute();
        for (Video v : response.getItems()) {
            String liveChatId = v.getLiveStreamingDetails().getActiveLiveChatId();
            if (liveChatId != null && !liveChatId.isEmpty()) {
                return liveChatId;
            }
        }

        return null;
    }

    public boolean isLiveStreamInAction() {
        return isLiveStreamInAction;
    }

    /**
     * Formats a chat message for console output.
     *
     * @param message The display message to output.
     * @param author The author of the message.
     * @param superChatDetails SuperChat details associated with the message.
     * @return A formatted string for console output.
     */
    private static String buildOutput(
            String message,
            LiveChatMessageAuthorDetails author,
            LiveChatSuperChatDetails superChatDetails) {
        StringBuilder output = new StringBuilder();
        if (superChatDetails != null) {
            output.append(superChatDetails.getAmountDisplayString());
            output.append("SUPERCHAT RECEIVED FROM ");
        }
        output.append(author.getDisplayName());
        List<String> roles = new ArrayList<String>();
        if (author.getIsChatOwner()) {
            roles.add("OWNER");
        }
        if (author.getIsChatModerator()) {
            roles.add("MODERATOR");
        }
        if (author.getIsChatSponsor()) {
            roles.add("SPONSOR");
        }
        if (roles.size() > 0) {
            output.append(" (");
            String delim = "";
            for (String role : roles) {
                output.append(delim).append(role);
                delim = ", ";
            }
            output.append(")");
        }
        if (message != null && !message.isEmpty()) {
            output.append(": ");
            output.append(message);
        }
        return output.toString();
    }
}
