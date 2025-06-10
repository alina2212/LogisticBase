package com.logisticsbase.app;

import com.logisticsbase.app.config.TerminalConfig;
import com.logisticsbase.app.entity.Truck;
import com.logisticsbase.app.queue.TruckQueue;
import com.logisticsbase.app.terminal.TerminalManager;
import com.logisticsbase.app.util.TruckLoader;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {

        int terminals = TerminalConfig.getInstance().getTerminalCount();
        int serviceTime = TerminalConfig.getInstance().getServiceTimeSeconds();

        TruckQueue queue = new TruckQueue();
        TerminalManager terminalManager = new TerminalManager(3);

        // Запускаем обработчик грузовиков
        Runnable truckProcessor = new TruckLoader (queue, terminalManager);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(truckProcessor);

        // Загружаем грузовики из файла
        List<Truck> trucks = TruckLoader.loadTrucksFromFile("src/main/resources/trucks.txt");
        for (Truck truck : trucks) {
            queue.enqueue(truck);
        }

        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        executor.shutdownNow();
    }
}
