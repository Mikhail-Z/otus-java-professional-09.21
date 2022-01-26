package ru.otus.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingListener<K, V> implements HwListener<K, V> {
    private final Logger logger = LoggerFactory.getLogger(LoggingListener.class);

    @Override
    public void notify(K key, V value, String action) {
        logger.info("key: {}, value: {}, action: {}", key, value, action);
    }
}
