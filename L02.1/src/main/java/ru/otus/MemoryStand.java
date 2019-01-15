package ru.otus;

class MemoryStand {

    public void getUsedMemory(Factory factory) throws InterruptedException {
        final int size = 20000000;
        System.gc();
        Thread.sleep(1000);
        System.out.println("Starting ...");
        long mem = getMem();
        Object[] array = new Object[size];
        long mem2 = getMem();
        System.out.println("Ref size: " + (mem2 - mem) / size);
        for (int i = 0; i < size; i++) {
            array[i] = factory.createContents();
        }
        long mem3 = getMem();
        System.out.println("Element size: " + (mem3 - mem2) / size);
        System.out.println("End");
        array = null;
        System.gc();
        Thread.sleep(1000);
    }

    private static long getMem() {
        Runtime runtime = Runtime.getRuntime();
        return runtime.totalMemory() - runtime.freeMemory();
    }
}
