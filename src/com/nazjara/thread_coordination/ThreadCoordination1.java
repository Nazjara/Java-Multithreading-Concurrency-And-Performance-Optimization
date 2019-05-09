package com.nazjara.thread_coordination;

import java.math.BigInteger;

public class ThreadCoordination1 {

    public BigInteger calculateResult(BigInteger base1, BigInteger power1, BigInteger base2, BigInteger power2) throws InterruptedException {
        PowerCalculatingThread calc1 = new PowerCalculatingThread(base1, power1);
        PowerCalculatingThread calc2 = new PowerCalculatingThread(base2, power2);
        calc1.setDaemon(true);
        calc2.setDaemon(true);

        calc1.start();
        calc2.start();
        calc1.join(2000);
        calc2.join(2000);

        return calc1.getResult().add(calc2.getResult());
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println(new ThreadCoordination1().calculateResult(BigInteger.TEN, BigInteger.TWO, BigInteger.ZERO, BigInteger.ONE));
    }

    private static class PowerCalculatingThread extends Thread {
        private BigInteger result = BigInteger.ONE;
        private BigInteger base;
        private BigInteger power;

        public PowerCalculatingThread(BigInteger base, BigInteger power) {
            this.base = base;
            this.power = power;
        }

        @Override
        public void run() {
            result = base.pow(power.intValue());
        }

        public BigInteger getResult() {
            return result;
        }
    }
}
