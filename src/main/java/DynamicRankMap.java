import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.common.collect.TreeMultimap;

import java.util.Comparator;
import java.util.Map;
import java.util.NavigableSet;

/**
 * 动态排序map，底层由RBTree实现
 * 理论上比avl树或直接List操作性能要高，任何不平衡都会在三次旋转之内解决
 * 相比直接Lis实现多了一层Map的空间辅助
 * 2019/05/26
 *
 * @param <K> id
 * @param <S> score
 * @param <V> entity
 * @author lqz
 */
public class DynamicRankMap<K, S, V extends IRankData<K, S, V>> {

    private Map<K, V> cacheMap;
    private TreeMultimap<S, V> multiMap;

    public static <K, S extends Comparable, V extends IRankData<K, S, V>> DynamicRankMap<K, S, V> create() {
        return new DynamicRankMap<K, S, V>(
                Maps.<K, V>newHashMap(),
                TreeMultimap.<S, V>create()
        );
    }

    public static <K, S, V extends IRankData<K, S, V>> DynamicRankMap<K, S, V> create(Comparator<? super S> keyComparator) {
        return new DynamicRankMap<K, S, V>(
                Maps.<K, V>newHashMap(),
                TreeMultimap.<S, V>create(keyComparator, Ordering.natural())
        );
    }

    public static <K, S, V extends IRankData<K, S, V>> DynamicRankMap<K, S, V> create(
            Comparator<? super S> keyComparator,
            Comparator<? super V> valueComparator) {
        return new DynamicRankMap<K, S, V>(
                Maps.<K, V>newHashMap(),
                TreeMultimap.create(keyComparator, valueComparator)
        );
    }

    public static <K, S, V extends IRankData<K, S, V>> DynamicRankMap<K, S, V> create(
            Map<K, V> cacheMap,
            Comparator<? super S> keyComparator,
            Comparator<? super V> valueComparator) {
        return new DynamicRankMap<K, S, V>(
                cacheMap,
                TreeMultimap.create(keyComparator, valueComparator)
        );
    }

    private DynamicRankMap(Map<K, V> cacheMap, TreeMultimap<S, V> multiMap) {
        this.cacheMap = cacheMap;
        this.multiMap = multiMap;
    }

    private V getItemOrCreate(V value) {
        V cache = cacheMap.get(value.getID());
        if (cache == null) {
            cacheMap.put(value.getID(), value);
        }
        return cache;
    }

    public void put(V value) {
        V cache = getItemOrCreate(value);
        if (cache != null) {
            NavigableSet<V> set = multiMap.get(value.getScore());
            set.remove(value);
        }
        multiMap.put(value.getScore(), value);
        cacheMap.put(value.getID(), value);
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

    public String toString() {
        return "DynamicRankMap{" +
                "size=" + size() +
                ", entry=" + multiMap +
                '}';
    }
}
