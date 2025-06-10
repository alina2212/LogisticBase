package com.logisticsbase.app.terminal;

import com.logisticsbase.app.entity.Truck;
import com.logisticsbase.app.entity.state.LoadingState;
import com.logisticsbase.app.entity.state.UnloadingState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class TerminalManager {

    private static final Logger logger = LogManager.getLogger(TerminalManager.class);

    private final Semaphore terminals;

    public TerminalManager(int terminalCount) {
        this.terminals = new Semaphore(terminalCount);
    }

    public void serveTruck(Truck truck) throws InterruptedException {
        logger.info("Truck {} is waiting for a terminal...", truck.getId());

        terminals.acquire();
        try {
            logger.info("Truck {} got a terminal.", truck.getId());

            if (truck.requiresLoading()) {
                truck.setState(new LoadingState());
            } else {
                truck.setState(new UnloadingState());
            }

            truck.processCurrentState();  // вызываем работу грузовика

            TimeUnit.SECONDS.sleep(1);

        } finally {
            terminals.release();
            logger.info("Truck {} released the terminal.", truck.getId());
        }


    }
}
