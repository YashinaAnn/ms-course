package com.learners.inventoryservice.service;

import com.learners.model.events.DeliveryEvent;

public interface DeliveryService {

    void receiveDelivery(DeliveryEvent event);
}
