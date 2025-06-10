package com.logisticsbase.app.util;

import com.logisticsbase.app.entity.Truck;
import com.logisticsbase.app.queue.TruckQueue;
import com.logisticsbase.app.terminal.TerminalManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TruckLoader implements Runnable {

    private static final Logger logger = LogManager.getLogger(TruckLoader.class);

    private final TruckQueue queue;
    private final TerminalManager terminalManager;

    public TruckLoader(TruckQueue queue, TerminalManager terminalManager) {
        this.queue = queue;
        this.terminalManager = terminalManager;
    }

    @Override
    public void run() {
        logger.info("TruckLoader started.");
        while (true) {
            try {
                Truck truck = queue.dequeue(); // забираем следующий фургон из очереди

                if (truck != null) {
                    try {
                        terminalManager.serveTruck(truck);
                    } catch (InterruptedException e) {
                        logger.error("Ошибка при обслуживании грузовика " + truck.getId(), e);
                    }
                } else {
                    TimeUnit.MILLISECONDS.sleep(100);
                }

            } catch (InterruptedException e) {
                logger.error("TruckLoader interrupted", e);
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public static List<Truck> loadTrucksFromFile(String filePath) {
        List<Truck> trucks = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                if (parts.length == 3) {
                    String id = parts[0].trim();
                    boolean requiresLoading = Boolean.parseBoolean(parts[1].trim());
                    boolean perishable = Boolean.parseBoolean(parts[2].trim());

                    Truck truck = new Truck(id, requiresLoading, perishable);
                    trucks.add(truck);
                }
            }
        } catch (IOException e) {
            logger.error("Ошибка при чтении файла грузовиков: " + filePath, e);
        }

        return trucks;
    }
}