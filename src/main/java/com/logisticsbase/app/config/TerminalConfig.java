package com.logisticsbase.app.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.locks.ReentrantLock;

public class TerminalConfig {

    private final int terminalCount;
    private final int serviceTimeSeconds;

    private static volatile TerminalConfig instance;
    private static final ReentrantLock lock = new ReentrantLock();

    private TerminalConfig(int terminalCount, int serviceTimeSeconds) {
        this.terminalCount = terminalCount;
        this.serviceTimeSeconds = serviceTimeSeconds;
    }

    public int getTerminalCount() {
        return terminalCount;
    }

    public int getServiceTimeSeconds() {
        return serviceTimeSeconds;
    }

    public static TerminalConfig getInstance() {
        if (instance == null) {
            lock.lock();
            try {
                if (instance == null) {
                    Properties properties = new Properties();
                    try (InputStream input = TerminalConfig.class
                            .getClassLoader()
                            .getResourceAsStream("terminal.properties")) {

                        if (input == null) {
                            throw new RuntimeException("Файл terminal.properties не найден в resources");
                        }

                        properties.load(input);

                        int terminalCount = Integer.parseInt(properties.getProperty("terminal.count"));
                        int serviceTimeSeconds = Integer.parseInt(properties.getProperty("service.time.seconds"));

                        instance = new TerminalConfig(terminalCount, serviceTimeSeconds);

                    } catch (IOException | NumberFormatException e) {
                        throw new RuntimeException("Ошибка при чтении terminal.properties", e);
                    }
                }
            } finally {
                lock.unlock();
            }
        }
        return instance;
    }
}
