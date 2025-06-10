package com.logisticsbase.app.entity.state;

import com.logisticsbase.app.entity.Truck;

import java.util.concurrent.TimeUnit;

public class UnloadingState implements State {

    @Override
    public void handle(Truck truck) throws InterruptedException {
        System.out.println("Truck " + truck.getId() + " is unloading cargo...");

        TimeUnit.SECONDS.sleep(1);
        System.out.println("Truck " + truck.getId() + " finished unloading.");

        truck.setState(new CompletedState());
    }
}