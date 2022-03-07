package com.company.servlet;

import com.company.crm.service.ClientService;
import com.company.services.TemplateProcessor;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class ClientServlet extends HttpServlet {

    private static final String USERS_PAGE_TEMPLATE = "clients.html";

    private final ClientService clientService;
    private final TemplateProcessor templateProcessor;

    public ClientServlet(TemplateProcessor templateProcessor, ClientService clientService) {
        this.templateProcessor = templateProcessor;
        this.clientService = clientService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        Map<String, Object> paramsMap = new HashMap<>();
        var clients = clientService.findAll();
        paramsMap.put("clients", clients);
        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(USERS_PAGE_TEMPLATE, paramsMap));
    }

}
