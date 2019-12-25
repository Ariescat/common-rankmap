package com.lqz.utils;

import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.common.collect.TreeMultimap;

import java.util.*;

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

    // -------------------- create --------------------
    public static <K, S extends Comparable, V extends IRankData<K, S, V>> DynamicRankMap<K, S, V> create() {
        return new DynamicRankMap<K, S, V>(
                Maps.newHashMap(),
                TreeMultimap.create(Ordering.natural().reverse(), Ordering.natural())
        );
    }

    public static <K, S, V extends IRankData<K, S, V>> DynamicRankMap<K, S, V> create(Comparator<? super S> keyComparator) {
        return new DynamicRankMap<K, S, V>(
                Maps.newHashMap(),
                TreeMultimap.create(keyComparator, Ordering.natural())
        );
    }

    public static <K, S, V extends IRankData<K, S, V>> DynamicRankMap<K, S, V> create(Comparator<? super S> keyComparator,
                                                                                      Comparator<? super V> valueComparator) {
        return new DynamicRankMap<K, S, V>(
                Maps.<K, V>newHashMap(),
                TreeMultimap.create(keyComparator, valueComparator)
        );
    }

    public static <K, S, V extends IRankData<K, S, V>> DynamicRankMap<K, S, V> create(Map<K, V> cacheMap,
                                                                                      Comparator<? super S> keyComparator,
                                                                                      Comparator<? super V> valueComparator) {
        return new DynamicRankMap<K, S, V>(
                cacheMap,
                TreeMultimap.create(keyComparator, valueComparator)
        );
    }

    private Map<K, V> cacheMap;
    /**
     * 本质上是封装了 TreeMap[score -> TreeSet]
     */
    private TreeMultimap<S, V> rankMap;

    private DynamicRankMap(Map<K, V> cacheMap, TreeMultimap<S, V> rankMap) {
        this.cacheMap = cacheMap;
        this.rankMap = rankMap;
    }

    private V getItemOrCreate(V value) {
        return cacheMap.putIfAbsent(value.getKey(), value);
    }

    public void put(V value) {
        V oldValue = getItemOrCreate(value);
        if (oldValue != null) {
            rankMap.remove(oldValue.getScore(), oldValue);
        }
        rankMap.put(value.getScore(), value);
        cacheMap.put(value.getKey(), value);
    }

    public Map<K, V> getCacheMap() {
        return cacheMap;
    }

    public TreeMultimap<S, V> getRankMap() {
        return rankMap;
    }

    public NavigableSet<V> get(S score) {
        return rankMap.get(score);
    }

    public S firstEntry() {
        return rankMap.asMap().firstKey();
    }

    public S lastEntry() {
        return rankMap.asMap().lastKey();
    }

    /**
     * 找到第一个大于或等于指定key的值
     */
    public S ceilingKey(S score) {
        return rankMap.asMap().ceilingKey(score);
    }

    /**
     * 找到第一个小于或等于指定key的值
     */
    public S floorKey(S score) {
        return rankMap.asMap().floorKey(score);
    }

    public void remove(V value) {
        V v = getItemOrCreate(value);
        if (v != null) {
            rankMap.remove(v.getScore(), v);
            cacheMap.remove(v.getKey());
        }
    }

    public int size() {
        return rankMap.size();
    }

    public boolean isEmpty() {
        return rankMap.isEmpty();
    }

    public void clear() {
        cacheMap.clear();
        rankMap.clear();
    }

    public List<V> toList() {
        return toList(null);
    }

    public List<V> toList(Function<V> function) {
        if (rankMap.isEmpty()) {
            return Collections.emptyList();
        }
        int i = 1;
        ArrayList<V> list = new ArrayList<>(rankMap.size());
        for (Collection<V> value : rankMap.asMap().values()) {
            for (V v : value) {
                list.add(function == null ? v : function.apply(v, i++));
            }
        }
        return list;
    }

    @Override
    public String toString() {
        return rankMap.toString();
    }

    public interface Function<V> {
        V apply(V data, int rank);
    }
}
