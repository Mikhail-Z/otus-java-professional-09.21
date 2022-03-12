package com.company.servlet;

import com.company.crm.service.ClientService;
import com.company.dao.model.Address;
import com.company.dao.model.Client;
import com.company.dao.model.Phone;
import com.company.servlet.dto.ClientInfoDTO;
import com.google.gson.Gson;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class ClientApiServlet extends HttpServlet {

    private final ClientService clientService;
    private final Gson gson;

    public ClientApiServlet(ClientService clientService, Gson gson) {
        this.clientService = clientService;
        this.gson = gson;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var clients = clientService.findAll()
                .stream()
                .map(client -> new ClientInfoDTO(
                        client.getName(),
                        client.getAddress().getStreet(),
                        client.getPhones().stream().map(Phone::getNumber).toList())
                ).toList();

        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream out = response.getOutputStream();
        out.print(gson.toJson(clients));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var body = readBody(request);
        var mapper = new ObjectMapper();
        ClientInfoDTO clientInfo = mapper.readValue(body, ClientInfoDTO.class);
        var client = new Client(
                clientInfo.getName(),
                new Address(clientInfo.getAddress()),
                clientInfo.getPhones().stream().map(Phone::new).toList());
        clientService.saveClient(client);
    }

    private String readBody(HttpServletRequest request) throws IOException {
        var stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;
        try (var inputStream = request.getInputStream()) {
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            }
        }
        finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    throw ex;
                }
            }
        }

        return stringBuilder.toString();
    }
}
