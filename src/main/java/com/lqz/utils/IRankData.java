package com.lqz.utils;

/**
 * 排行数据接口
 *
 * @param <K> 索引 key
 * @param <S> 排序分数（值） score
 * @param <V> 实体类 value
 */
public interface IRankData<K, S, V> extends Comparable<V> {

    K getKey();

    /**
     * 获取分数
     */
    S getScore();

    /**
     * 生成显示快照，防止排行榜更新导致数据不一致
     */
    V copy();
}
