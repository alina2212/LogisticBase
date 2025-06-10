package com.logisticsbase.app.entity.state;

import com.logisticsbase.app.entity.Truck;

public interface State {
    void handle(Truck truck) throws InterruptedException;
}
