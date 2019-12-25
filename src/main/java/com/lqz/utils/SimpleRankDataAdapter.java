package com.lqz.utils;

import java.util.Objects;

/**
 * @author Ariescat
 * @version 2019/12/24 16:37
 */
public abstract class SimpleRankDataAdapter<V extends SimpleRankDataAdapter<V>> implements IRankData<Integer, Integer, V>, Cloneable {

    protected long lastUpdateTime;

    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    @Override
    public V copy() {
        return Objects.requireNonNull(clone());
    }

    @Override
    protected V clone() {
        try {
            //noinspection unchecked
            return (V) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 默认比较策略：
     * 1.分数比较 [大 -> 小]
     * 2.若 分数 相同， 则 lastUpdateTime 比较 [小 -> 大]
     * 3.若 lastUpdateTime 相同， 则 key 比较 [小 -> 大, 注：这一步一定不能省，若没有唯一key指定，TreeMap会丢失后面的更新]
     */
    @Override
    public int compareTo(V o) {
        return o.getScore().equals(getScore())
                ? (int) (o.getLastUpdateTime() == lastUpdateTime ? getKey() - o.getKey() : lastUpdateTime - getLastUpdateTime())
                : (o.getScore() - getScore());
    }
}
