package com.logisticsbase.app.entity;

import com.logisticsbase.app.entity.state.State;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Truck implements Runnable {
    private static final Logger logger = LogManager.getLogger(Truck.class);

    private final String id;
    private final boolean requiresLoading;
    private final boolean perishable;

    private State state;

    private final Lock stateLock = new ReentrantLock();

    public Truck(String id, boolean requiresLoading, boolean perishable) {
        this.id = id;
        this.requiresLoading = requiresLoading;
        this.perishable = perishable;
    }

    public String getId() {
        return id;
    }

    public boolean requiresLoading() {
        return requiresLoading;
    }

    public boolean isPerishable() {
        return perishable;
    }

    public void setState(State state) {
        stateLock.lock();
        try {
            this.state = state;
            logger.info("Truck {} changed state to {}", id, state.getClass().getSimpleName());
        } finally {
            stateLock.unlock();
        }
    }

    public State getState() {
        stateLock.lock();
        try {
            return state;
        } finally {
            stateLock.unlock();
        }
    }


    public void processCurrentState() throws InterruptedException {
        State currentState = getState();
        if (currentState != null) {
            currentState.handle(this);
        } else {
            logger.warn("Truck {} has no state set!", id);
        }
    }


    @Override
    public void run() {
        try {
            processCurrentState();
        } catch (InterruptedException e) {
            logger.warn("Truck {} was interrupted during operation.", id);
            Thread.currentThread().interrupt();
        }
    }


    public void startProcessing() {
        new Thread(this).start();
    }

    @Override
    public String toString() {
        return "Truck{" +
                "id='" + id + '\'' +
                ", requiresLoading=" + requiresLoading +
                ", perishable=" + perishable +
                '}';
    }
}
