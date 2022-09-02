package com.learners.pizzaservice.cache;

import lombok.extern.slf4j.Slf4j;
import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;

@Slf4j
public class CacheEventLogger implements CacheEventListener<Object, Object> {

    @Override
    public void onEvent(CacheEvent<?, ?> event) {
        log.info("Cache event {} triggered. KEY: {}, NEW VALUE: {}, OLD VALUE: {}",
                event.getType(), event.getKey(), event.getNewValue(), event.getOldValue());
    }
}
