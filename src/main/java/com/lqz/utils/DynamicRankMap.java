package com.lqz.utils;

import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.common.collect.TreeMultimap;

import java.util.*;
import java.util.function.Function;

/**
 * 动态排序map，底层由RBTree实现
 * 理论上比avl树或直接List操作性能要高，任何不平衡都会在三次旋转之内解决
 * 相比直接Lis实现多了一层Map的空间辅助
 * <p>
 * Ordering.natural() 最终用到的是 {@see com.google.common.collect.NaturalOrdering#compare}，也就是会调用 V 实现的 Comparable 接口
 * 若需要自定义比较器，则用下面的构造器 {@link DynamicRankMap#create(Comparator, Comparator)}
 * <p>
 * 2019年12月24日
 *
 * @see IRankData
 */
public class DynamicRankMap<K, S, V extends IRankData<K, S, V>> {

    private Map<K, V> cacheMap;
    private TreeMultimap<S, V> multiMap;

    private DynamicRankMap(Map<K, V> cacheMap, TreeMultimap<S, V> multiMap) {
        this.cacheMap = cacheMap;
        this.multiMap = multiMap;
    }

    public static <K, S extends Comparable, V extends IRankData<K, S, V>> DynamicRankMap<K, S, V> create() {
        return new DynamicRankMap<>(
                Maps.newHashMap(),
                TreeMultimap.create()
        );
    }

    public static <K, S, V extends IRankData<K, S, V>> DynamicRankMap<K, S, V> create(Comparator<? super S> keyComparator) {
        return new DynamicRankMap<>(
                Maps.newHashMap(),
                TreeMultimap.create(keyComparator, Ordering.natural())
        );
    }

    public static <K, S, V extends IRankData<K, S, V>> DynamicRankMap<K, S, V> create(
            Comparator<? super S> keyComparator,
            Comparator<? super V> valueComparator) {
        return new DynamicRankMap<>(
                Maps.<K, V>newHashMap(),
                TreeMultimap.create(keyComparator, valueComparator)
        );
    }

    public static <K, S, V extends IRankData<K, S, V>> DynamicRankMap<K, S, V> create(
            Map<K, V> cacheMap,
            Comparator<? super S> keyComparator,
            Comparator<? super V> valueComparator) {
        return new DynamicRankMap<>(
                cacheMap,
                TreeMultimap.create(keyComparator, valueComparator)
        );
    }

    private V getItemOrCreate(V value) {
        return cacheMap.putIfAbsent(value.getKey(), value);
    }

    public void put(V value) {
        V cache = getItemOrCreate(value);
        if (cache != null) {
            NavigableSet<V> set = multiMap.get(value.getScore());
            set.remove(value);
        }
        multiMap.put(value.getScore(), value);
        cacheMap.put(value.getKey(), value);
    }

    public Map<K, V> getCacheMap() {
        return cacheMap;
    }

    public TreeMultimap<S, V> getMultiMap() {
        return multiMap;
    }

    public NavigableSet<V> get(S score) {
        return multiMap.get(score);
    }

    public S firstEntry() {
        return multiMap.asMap().firstKey();
    }

    public S lastEntry() {
        return multiMap.asMap().lastKey();
    }

    /**
     * 找到第一个大于或等于指定key的值
     */
    public S ceilingKey(S score) {
        return multiMap.asMap().ceilingKey(score);
    }

    /**
     * 找到第一个小于或等于指定key的值
     */
    public S floorKey(S score) {
        return multiMap.asMap().floorKey(score);
    }

    public int size() {
        return multiMap.size();
    }

    public boolean isEmpty() {
        return multiMap.isEmpty();
    }

    public void clear() {
        cacheMap.clear();
        multiMap.clear();
    }

    public List<V> toList() {
        return toList(null);
    }

    public List<V> toList(Function<V, V> function) {
        if (multiMap.isEmpty()) {
            return Collections.emptyList();
        }
        int i = 1;
        ArrayList<V> list = new ArrayList<>(multiMap.size());
        for (Collection<V> value : multiMap.asMap().values()) {
            for (V v : value) {
                v.setRank(i++);
                list.add(function == null ? v : function.apply(v));
            }
        }
        return list;
    }
}
