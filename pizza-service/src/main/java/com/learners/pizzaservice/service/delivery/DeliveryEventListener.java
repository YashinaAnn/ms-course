package com.learners.pizzaservice.service.delivery;

import com.learners.model.events.PizzaDeliveryEvent;

public interface DeliveryEventListener {

    void listen(PizzaDeliveryEvent event);
}
