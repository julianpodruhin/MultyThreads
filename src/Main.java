package MultiThreads;

public class Main {
    public static void main(String[] args) {
        System.out.println("Работы кода для данной практической: ");
        Exercise1();
        Exercise2();
        Exercise3();
        Exercise4();
    }

    private static void Exercise1() {
        System.out.println("1 задание: ");
        for (int i = 1; i < 11; i++) {
            System.out.printf("Поток номер %d работает.\n", i);
            int var = i;
            // Создаем потоки, также описывает их работу
            new Thread(() -> {
                for (int j = 0; j <= 100; j++) {
                    System.out.printf("Поток: %d число: %d\n", var, j);
                }
            }).start();
        }
    }

    private static void Exercise2() {
        try {
            Thread xThread = new xThread();
            System.out.printf("2 задание: \nСтатус потока: %s\n", xThread.getState());
            System.out.println("Поток запущен.");
            xThread.start(); // Запуск потока

            System.out.printf("Статус потока: %s\n", xThread.getState());
            //Приостанавливаем главный поток
            Thread.sleep(1000); // Приостановка работы потока на 1 секунду

            System.out.printf("Статус потока: %s\n", xThread.getState());
            xThread.join();
            System.out.printf("Статус потока: %s\n", xThread.getState());
        }
        catch (InterruptedException error) {
            System.out.println(error.getMessage());
        }
    }

    private static void Exercise3() {
        try {
            System.out.println("3 задание: ");
            for (int i = 0; i < 100; i++) {
                Thread thread = new Thread(() -> {
                    for (int j = 0; j < 1000; j++) {
                        Counter.Increment();
                    }
                });
                thread.start();
                thread.join();     // Ожидание остановки другого потока
            }
            System.out.printf("Итоговое число: %d\n", Counter.getCount());
        }
        catch (InterruptedException error) {
            System.out.println(error.getMessage());
        }
    }

    private static void Exercise4() {
        // Объект в качестве монитора (Управляющий доступом к потоку)
        Object objectMonitor = new Object();
        System.out.println("4 задание: ");
        try {
            Thread threadOne = new xxThread(objectMonitor);
            Thread threadTwo = new xxThread(objectMonitor);
            threadOne.setName("Поток первый");
            threadTwo.setName("Поток второй");
            threadOne.start();
            threadTwo.start();
            Thread.sleep(8000);
            // Завершить работу потоков
            threadOne.interrupt();
            threadTwo.interrupt();
        }
        catch (InterruptedException error) {
            error.getStackTrace();
        }
    }

    // Объявление потока через наследование класса Thread
    static class xThread extends Thread {
        @Override
        public void run() {
            System.out.println("Поток начал свою работу");
            for (long i = 0; i < 910000000L; i++) {
                if (i % 23634384 == 0) System.out.println("Работаем..");
            }
        }
    }

    static class xxThread extends Thread {
        private Object monitor;

        //Конструктор принимающий объект, который выступает в качестве монитора
        xxThread(Object monitor) {
            this.monitor = monitor;
        }

        @Override
        public void run() {
            try {
                while (!isInterrupted()) {
                    synchronized (monitor) {
                        System.out.println("Выполняется " + getName());
                        sleep(1000);
                        // Будим заснувший поток
                        monitor.notify();
                        // Отправляем поток спать
                        monitor.wait();
                    }
                }
            } catch (InterruptedException error) {
                System.out.printf("Поток %s завершен.\n",getName());
            }
        }
    }

    static class Counter {
        private static volatile int count = 0;
        public static int getCount() { return count; }
        synchronized public static void Increment() {
            count += 1;
        }
    }
}
