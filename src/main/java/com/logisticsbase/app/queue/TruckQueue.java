package com.logisticsbase.app.queue;

import com.logisticsbase.app.entity.Truck;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TruckQueue {

    private final Queue<Truck> perishableQueue = new ConcurrentLinkedQueue<>();   // скоропортящиеся вне очереди
    private final Queue<Truck> normalQueue = new ConcurrentLinkedQueue<>();       // остальные

    // Добавить грузовик в очередь (по типу груза)
    public void enqueue(Truck truck) {
        if (truck.isPerishable()) {
            perishableQueue.add(truck);
        } else {
            normalQueue.add(truck);
        }
    }

    // Получить следующий грузовик на обслуживание (первыми — скоропортящиеся)
    public Truck dequeue() {
        Truck truck = perishableQueue.poll();
        if (truck != null) {
            return truck;
        }
        return normalQueue.poll();
    }

    // Проверить, пусты ли обе очереди
    public boolean isEmpty() {
        return perishableQueue.isEmpty() && normalQueue.isEmpty();
    }

    // Общее количество грузовиков в очередях
    public int size() {
        return perishableQueue.size() + normalQueue.size();
    }
}
