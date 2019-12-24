package com.lqz.utils;

import org.apache.commons.lang3.time.StopWatch;

import java.util.Comparator;
import java.util.concurrent.TimeUnit;

public class Test {

    /**
     * id, score -> com.lqz.utils.RankData
     */
    private DynamicRankMap<Integer, Integer, SimpleRankData> dynamicRankMap = DynamicRankMap.create(new Comparator<Integer>() {
        @Override
        public int compare(Integer o1, Integer o2) {
            return o2 - o1;
        }
    });

    public static void main(String[] args) {
        new Test().run();
    }

    private void run() {

        StopWatch watch = StopWatch.createStarted();

        dynamicRankMap.put(new SimpleRankData(1, 15));
        dynamicRankMap.put(new SimpleRankData(2, 5));
        dynamicRankMap.put(new SimpleRankData(3, 20));
        dynamicRankMap.put(new SimpleRankData(4, 10));
        dynamicRankMap.put(new SimpleRankData(6, 20));
        dynamicRankMap.put(new SimpleRankData(5, 20));

        watch.stop();
        System.err.println("time: " + watch.getTime(TimeUnit.MILLISECONDS));

        System.err.println("dynamicRankMap: " + dynamicRankMap);
        System.err.println("get(20): " + dynamicRankMap.get(20));
        System.err.println("ceilingKey(10): " + dynamicRankMap.ceilingKey(10));
        System.err.println("firstEntry: " + dynamicRankMap.firstEntry());
        System.err.println("lastEntry: " + dynamicRankMap.lastEntry());
    }
}
