package com.cp;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Main {

    private static long totalNoOfPoints = 1000000;

    private static long noOfThreads = 1;

    private static long attempts = 10;

    private static long pointsInsideCircle;

    private static long calculatedPoints;

    private static long timeStart, timeStop;

    private static List<Double> executionTimes = new ArrayList<>();

    public static void main(String[] args) {
        for (int i = 0; i < 6; i++) {
            System.out.println("JVM WARMUP");
            processCalculations();
            clearResources();
            System.out.println("JVM WARMUP");
        }
        executionTimes.clear();

        for (int i = 0; i < attempts; i++) {
            System.out.println((i + 1) + ". attempt");
            processCalculations();
            clearResources();
            System.out.println();
        }
        System.out.println("-------------------------------------------------");
        System.out.println("Available processors: " + Runtime.getRuntime().availableProcessors() +
                "\nThreads used = " + noOfThreads);
        executionTimes.stream()
                .mapToDouble(value -> value)
                .average()
                .ifPresent(average -> System.out.println("Average time = " + average));

    }

    private static void clearResources() {
        pointsInsideCircle = 0;
        calculatedPoints = 0;
        timeStart = 0;
        timeStop = 0;
    }

    private static void processCalculations() {
        List<Thread> threads = createThreads();

        startAndJoinThreads(threads);

        calculatePiAndWriteMessage();
    }

    static List<Point> generateRandomPoints(long count) {
        Random random = new Random();
        List<Double> x = random.doubles(count).boxed().collect(Collectors.toList());
        List<Double> y = random.doubles(count).boxed().collect(Collectors.toList());
        List<Point> points = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            points.add(new Point(x.get(i), y.get(i)));
        }
        return points;
    }

    private static List<Thread> createThreads() {
        List<Thread> threads = new ArrayList<>();
        long perThreadPointListSize = (totalNoOfPoints / noOfThreads);
        for (int i = 0; i < noOfThreads; i++) {
            threads.add(createPointCalculationThread(perThreadPointListSize));
        }
        return threads;
    }

    static Thread createPointCalculationThread(Long pointCount) {
        return new Thread(() -> {
            for (Point point : generateRandomPoints(pointCount)) {
                if (isPointInCircle(point)) {
                    pointsInsideCircle++;
                }
                calculatedPoints++;
                timeStop = System.nanoTime();
            }
        });
    }

    static boolean isPointInCircle(Point point) {
        double r = 0.5, xp = 0.5, yp = 0.5;
        double rough = Math.pow((xp - point.x), 2) + Math.pow((yp - point.y), 2);
        double aftersqrt = Math.sqrt(rough);
        return aftersqrt < r;

    }

    private static void startAndJoinThreads(List<Thread> threads) {
        boolean started = false;
        for (Thread th : threads) {
            if (!started) {
                timeStart = System.nanoTime();
                started = true;
            }
            th.run();
        }

        threads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    private static void calculatePiAndWriteMessage() {
        double piEstimation = 4 * pointsInsideCircle / (double) totalNoOfPoints;
        double timeMeasurment = ((double) (timeStop - timeStart)) / Math.pow(10, 6);
        executionTimes.add(timeMeasurment);
        System.out.println("PI estimation = " + piEstimation +
                "\nExecution time = " + timeMeasurment + "ms" +
                "\nTotal calculated points = " + calculatedPoints +
                "\nPoints inside circle = " + pointsInsideCircle);
    }

}


