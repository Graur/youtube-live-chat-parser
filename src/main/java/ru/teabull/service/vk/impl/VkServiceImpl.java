package ru.teabull.service.vk.impl;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.teabull.config.interfaces.VkConfig;
import ru.teabull.model.Client;
import ru.teabull.service.vk.interfaces.VkService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VkServiceImpl implements VkService{

    private String token;
    private String version;
    private String targetVkGroup;

    private static Logger logger = LoggerFactory.getLogger(VkService.class);
    private final String VK_API_METHOD_TEMPLATE = "https://api.vk.com/method/";

    @Autowired
    public VkServiceImpl(VkConfig vkConfig) {
        token = vkConfig.getToken();
        version = vkConfig.getVersion();
        targetVkGroup = vkConfig.getTargetVkGroup();
    }

    public Optional<Client> getClientFromYoutubeLiveStreamByName(String name) {
        String fullName = name.replaceAll("(?U)[\\pP\\s]", "%20");
        String uriGetClient = VK_API_METHOD_TEMPLATE + "users.search?" +
                "q=" + fullName +
                "&count=1" +
                "&group_id=" + targetVkGroup +
                "&v=" + version +
                "&access_token=" + token;

        HttpGet httpGetClient = new HttpGet(uriGetClient);
        HttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setCookieSpec(CookieSpecs.STANDARD).build())
                .build();
        try {
            HttpResponse response = httpClient.execute(httpGetClient);
            String result = EntityUtils.toString(response.getEntity());
            JSONObject json = new JSONObject(result);
            JSONObject responseObject = json.getJSONObject("response");

            if (responseObject.getString("count").equals("0")) {
                return Optional.empty();
            } else {
                JSONArray jsonUsers = responseObject.getJSONArray("items");
                JSONObject jsonUser = jsonUsers.getJSONObject(0);
                long id = jsonUser.getLong("id");
                String firstName = jsonUser.getString("first_name");
                String lastName = jsonUser.getString("last_name");
                String vkLink = "https://vk.com/id" + id;
                String fullClientName = firstName + lastName;
                Client client = new Client(fullClientName, vkLink);
                return Optional.of(client);
            }
        } catch (JSONException e) {
            logger.error("Can not read message from JSON or YoutubeClient don't exist in VK group",e);
        } catch (IOException e) {
            logger.error("Failed to connect to VK server ", e);
        }
        return Optional.empty();
    }
}
