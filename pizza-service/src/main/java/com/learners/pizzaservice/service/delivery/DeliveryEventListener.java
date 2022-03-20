package com.learners.pizzaservice.service.delivery;

import com.learners.pizzaservice.events.PizzaDeliveryEvent;

public interface DeliveryEventListener {

    void listen(PizzaDeliveryEvent event);
}
