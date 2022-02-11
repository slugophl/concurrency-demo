package com.eic.concurrency.demo.service;

public class CounterServiceImpl implements CounterService {
    private long counter;

    @Override
    public long getCurrent() {
        return counter;
    }

    @Override
    public long getNext() {
        return ++counter;
    }
}
