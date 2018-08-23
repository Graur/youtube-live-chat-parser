package ru.teabull.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.teabull.service.client.interfaces.ClientService;

@Controller
public class MainController {

    private final ClientService clientService;

    @Autowired
    public MainController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/")
    public String homePage(Model model) {
        model.addAttribute("list", clientService.findAll());
        return "index";
    }
}
