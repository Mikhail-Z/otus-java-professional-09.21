package com.company.server;

import com.company.crm.service.ClientService;
import com.google.gson.Gson;
import com.company.services.TemplateProcessor;
import com.company.services.UserAuthService;
import com.company.servlet.AuthorizationFilter;
import com.company.servlet.LoginServlet;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;


import java.util.Arrays;

public class ClientWebServerWithFilterBasedSecurity extends ClientWebServerSimple {
    private final UserAuthService authService;

    public ClientWebServerWithFilterBasedSecurity(int port,
                                                  UserAuthService authService,
                                                  ClientService clientService,
                                                  Gson gson,
                                                  TemplateProcessor templateProcessor) {
        super(port, clientService, gson, templateProcessor);
        this.authService = authService;
    }

    @Override
    protected Handler applySecurity(ServletContextHandler servletContextHandler, String... paths) {
        servletContextHandler.addServlet(new ServletHolder(new LoginServlet(templateProcessor, authService)), "/login");
        AuthorizationFilter authorizationFilter = new AuthorizationFilter();
        Arrays.stream(paths).forEachOrdered(path -> servletContextHandler.addFilter(new FilterHolder(authorizationFilter), path, null));
        return servletContextHandler;
    }
}
