package com.company.crm.service;

import com.company.dao.model.Client;

import java.util.List;
import java.util.Optional;

public interface ClientService {

    Client saveClient(Client client);

    Optional<Client> getClient(long id);

    List<Client> findAll();
}
