package com.logisticsbase.app.entity.state;

import com.logisticsbase.app.entity.Truck;

import java.util.concurrent.TimeUnit;

public class LoadingState implements State {

    @Override
    public void handle(Truck truck) throws InterruptedException {
        System.out.println("Truck " + truck.getId() + " is loading cargo...");

        TimeUnit.SECONDS.sleep(1);
        System.out.println("Truck " + truck.getId() + " finished loading.");
        truck.setState(new CompletedState());
    }
}