package com.technorapper.coreandroidmvvm.network.global;

public class SystemVariable {
    public static final String REQUEST_METHOD = "GET";
    public static final int READ_TIMEOUT = 150000;
    public static final int CONNECTION_TIMEOUT = 150000;


    // Get max available VM memory, exceeding this amount will throw an
    // OutOfMemory exception. Stored in kilobytes as LruCache takes an
    // int in its constructor.
    public static final int MAXMEMORY = (int) (Runtime.getRuntime().maxMemory() / 1024);

    // Use 1/8th of the available memory for this memory cache.
    public static  final int CACHESIZE = MAXMEMORY / 8;
}
