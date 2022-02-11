package com.eic.concurrency.demo;

import com.eic.concurrency.demo.service.CounterService;
import com.eic.concurrency.demo.service.CounterServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
public class CounterServiceTests {
    CounterService counterService;

    @Before
    public void setup() {
        this.counterService = new CounterServiceImpl();
    }

    @Test
    public void test_current_first_time() {
        long counter = counterService.getCurrent();
        assertEquals(0, counter);
    }

    @Test
    public void test_next_first_time() {
        long counter = counterService.getNext();
        assertEquals(1, counter);
    }

    @Test
    public void test_current_after_3_calls_to_next() {
        counterService.getNext();
        counterService.getNext();
        counterService.getNext();
        long counter = counterService.getCurrent();
        assertEquals(3, counter);
    }

    @Test
    public void test_multiple_requests_to_next() {
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        List<Callable<Long>> taskList = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            int j = i;
            taskList.add(() -> {
                long next = counterService.getNext();
                System.out.println("Task" + (j+1) + " next: " + next);
                System.out.println(Thread.currentThread().getName());
                return next;
            });
        }

        List<Future<Long>> resultList = null;
        try {
            resultList = executorService.invokeAll(taskList);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }

        List<Long> sortedResults = resultList.stream()
                .map(r -> {
                    try {
                        return r.get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .collect(Collectors.toList());
        sortedResults.sort(null);

        assertEquals(validateCounter(50), sortedResults);
    }

    private List<Long> validateCounter(int n) {
        List<Long> counter = new ArrayList<>();

        for (int i = 1; i <= n; i++) {
            counter.add(Long.valueOf(i));
        }

        return counter;
    }
}
