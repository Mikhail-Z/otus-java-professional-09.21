package ru.otus.demo;

import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cache.MyCache;
import ru.otus.cache.NoopCache;
import ru.otus.core.repository.DataTemplateHibernate;
import ru.otus.core.repository.HibernateUtils;
import ru.otus.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.service.DbServiceClientImpl;

import java.util.ArrayList;
import java.util.List;

/*VM Options -Xms50m -Xmx50m*/

public class DbServiceDemo {

    private static final Logger log = LoggerFactory.getLogger(DbServiceDemo.class);

    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();


        System.out.println("==============with cache=============");
        {
            var sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class, Address.class, Phone.class);

        var transactionManager = new TransactionManagerHibernate(sessionFactory);
        var clientTemplate = new DataTemplateHibernate<>(Client.class);
        var cache = new MyCache<Long, Client>();
        var dbServiceClient = new DbServiceClientImpl(transactionManager, clientTemplate, cache);

            long start = System.currentTimeMillis();
            final int n = 1000;
            List<Long> ids = new ArrayList<>(n);
            for (int i = 0; i < n; i++) {
                var id = dbServiceClient.saveClient(new Client("dbServiceSecond" + i, new Address("some address" + i), List.of(new Phone("some phone " + i)))).getId();
                ids.add(id);
            }

            for (int i = 0; i < 10; i++) {
                for (var id : ids) {
                    var client = dbServiceClient.getClient(id);
                    client.get().getId();
                }
            }


            long end = System.currentTimeMillis();
            long elapsedTime = end - start;
            System.out.println(elapsedTime); //4.4 sec
        }


        System.out.println("=============without cache===========");
        {
            var sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class, Address.class, Phone.class);

            var transactionManager = new TransactionManagerHibernate(sessionFactory);

            var clientTemplate = new DataTemplateHibernate<>(Client.class);

            var dbServiceClient2 = new DbServiceClientImpl(transactionManager, clientTemplate, new NoopCache<>());

            long start = System.currentTimeMillis();
            final int n = 1000;
            List<Long> ids = new ArrayList<>(n);
            for (int i = 0; i < n; i++) {
                var id = dbServiceClient2.saveClient(new Client("dbServiceSecond" + i, new Address("some address" + i), List.of(new Phone("some phone " + i)))).getId();
                ids.add(id);
            }

            for (int i = 0; i < 10; i++) {
                for (var id : ids) {
                    var client = dbServiceClient2.getClient(id);
                    client.get().getId();
                }
            }

            long end = System.currentTimeMillis();
            long elapsedTime = end - start;
            System.out.println(elapsedTime); //7.8 sec
        }
    }
}
