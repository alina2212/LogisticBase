package com.logisticsbase.app.entity.state;

import com.logisticsbase.app.entity.Truck;

public class CompletedState implements State {

    @Override
    public void handle(Truck truck) {
        System.out.println("Truck " + truck.getId() + " has completed its operation.");
    }
}