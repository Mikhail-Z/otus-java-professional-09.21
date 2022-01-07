package com.company.demo;

import com.company.logframework.core.proxy.Ioc;
import com.company.logframework.logger.Logger;
import com.company.logframework.logger.StdoutLogger;

public class Main {
    public static void main(String[] args) throws Exception {
        Logger logger = new StdoutLogger();
        MyClassInterface object = Ioc.createLoggingProxy(MyClassInterface.class, MyClassImpl.class, logger);
        object.method("abc");
        object.method("abc", 2);
        object.methodWithAgeReturn("abc", new Data(20, "some name"));
        object.badMethod(2);
    }
}
