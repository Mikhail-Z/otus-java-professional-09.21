package com.company.logframework.logger;

import java.time.OffsetDateTime;

public class StdoutLogger implements Logger {
    @Override
    public void log(String s, LogLevel level) {
        var now = OffsetDateTime.now();
        System.out.printf("[%s] %td-%tm-%tY %tH:%tM:%tS.%tL: %s\n",
                level, now, now, now, now, now, now, now, s);
    }
}
