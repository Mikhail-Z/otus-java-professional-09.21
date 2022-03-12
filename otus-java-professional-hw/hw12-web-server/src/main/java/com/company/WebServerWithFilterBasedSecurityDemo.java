package com.company;

import com.company.crm.service.ClientService;
import com.company.crm.service.ClientServiceImpl;
import com.company.dao.dbmigrations.MigrationsExecutorFlyway;
import com.company.dao.model.Address;
import com.company.dao.model.Client;
import com.company.dao.model.Phone;
import com.company.dao.repository.AuthUserRepository;
import com.company.dao.repository.DataTemplateHibernate;
import com.company.dao.repository.HibernateUtils;
import com.company.dao.repository.InMemoryAuthUserRepository;
import com.company.dao.sessionmanager.TransactionManagerHibernate;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.company.services.TemplateProcessor;
import com.company.services.TemplateProcessorImpl;
import com.company.services.UserAuthService;
import com.company.services.UserAuthServiceImpl;
import com.company.server.ClientWebServer;
import com.company.server.ClientWebServerWithFilterBasedSecurity;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/*
    Полезные для демо ссылки

    // Стартовая страница
    http://localhost:8080

    // Страница пользователей
    http://localhost:8080/users

    // REST сервис
    http://localhost:8080/api/user/3
*/
public class WebServerWithFilterBasedSecurityDemo {
    private static final int WEB_SERVER_PORT = 8080;

    private static final String TEMPLATES_DIR = "/templates/";

    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) throws Exception {
        var service = initService();
        var server = initServer(service);
        server.start();
        server.join();
    }

    private static ClientService initService() {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();

        var sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class, Address.class, Phone.class);

        var transactionManager = new TransactionManagerHibernate(sessionFactory);
        var clientTemplate = new DataTemplateHibernate<>(Client.class);

        var svc = new ClientServiceImpl(transactionManager, clientTemplate);
        svc.saveClient(new Client("some client", new Address("some address" ), List.of(new Phone("some phone "))));
        return svc;
    }

    private static ClientWebServer initServer(ClientService clientService) throws IOException {
        AuthUserRepository userDao = new InMemoryAuthUserRepository();
        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);
        UserAuthService authService = new UserAuthServiceImpl(userDao);

        return new ClientWebServerWithFilterBasedSecurity(WEB_SERVER_PORT,
                authService, clientService, gson, templateProcessor);
    }
}
