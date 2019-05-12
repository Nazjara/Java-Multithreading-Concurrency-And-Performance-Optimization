package com.nazjara.data_sharing;

public class DataRace {
    public static void main(String[] args) {
        SharedClass sharedClass = new SharedClass();
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < Integer.MAX_VALUE; i++) {
                sharedClass.increment();
            }
        });

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < Integer.MAX_VALUE; i++) {
                sharedClass.checkForDataRace();
                sharedClass.checkForRaceCondition();
            }

        });

        thread1.start();
        thread2.start();
    }

    public static class SharedClass {
        // specifying variables as volatile is solving data race by guaranteeing execution order
        private volatile int x = 0;
        private volatile int y = 0;

        public void increment() {
            x++;
            y++;
        }

        public void checkForDataRace() {
            if (y > x) {
                System.out.println("y > x - Data Race is detected");
            }
        }

        public void checkForRaceCondition() {
            // without synchronization mechanism race condition is still possible
            if (x > y) {
                System.out.println("x > y - Race Condition is detected");
            }
        }
    }
}
