package com.lqz.utils;

import org.apache.commons.lang3.time.StopWatch;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class Test {

    public static void main(String[] args) {
        new Test().run();
    }

    private void run() {
        // id, score -> SimpleRankData
        DynamicRankMap<Integer, Integer, SimpleRankData> dynamicRankMap = DynamicRankMap.create();

        StopWatch watch = StopWatch.createStarted();
        dynamicRankMap.put(new SimpleRankData(1, 15));
        dynamicRankMap.put(new SimpleRankData(2, 5));
        dynamicRankMap.put(new SimpleRankData(3, 20));
        dynamicRankMap.put(new SimpleRankData(4, 10));
        dynamicRankMap.put(new SimpleRankData(6, 20));
        dynamicRankMap.put(new SimpleRankData(5, 20));
        dynamicRankMap.put(new SimpleRankData(1, 18));
        watch.stop();

        System.err.println("cost time: " + watch.getTime(TimeUnit.MILLISECONDS));

        System.err.println("dynamicRankMap: " + dynamicRankMap);
        System.err.println("size: " + dynamicRankMap.size());

        System.err.println("get(20): " + dynamicRankMap.get(20));
        System.err.println("ceilingKey(12): " + dynamicRankMap.ceilingKey(12));
        System.err.println("firstEntry: " + dynamicRankMap.firstEntry());
        System.err.println("lastEntry: " + dynamicRankMap.lastEntry());

        List<SimpleRankData> list = dynamicRankMap.toList(SimpleRankData::setRank);
        System.err.println("list: " + list);
    }
}
