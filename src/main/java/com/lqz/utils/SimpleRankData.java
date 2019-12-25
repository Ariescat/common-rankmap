package com.lqz.utils;

/**
 * 一个简单的排行实体类
 */
public class SimpleRankData extends SimpleRankDataAdapter<SimpleRankData> {

    private int playerID;
    private int score;
    private int rank;

    public SimpleRankData(int playerID, int score) {
        this.playerID = playerID;
        this.score = score;
        this.lastUpdateTime = System.currentTimeMillis();
    }

    @Override
    public Integer getKey() {
        return playerID;
    }

    @Override
    public Integer getScore() {
        return score;
    }

    /**
     * 获取排行
     */
    public int getRank() {
        return rank;
    }

    /**
     * 修改排行
     * <p>
     * 配合 {@link DynamicRankMap#toList()} 使用
     */
    public SimpleRankData setRank(int rank) {
        this.rank = rank;
        return this;
    }

    @Override
    public String toString() {
        return "SimpleRankData{" +
                "playerID=" + playerID +
                ", score=" + score +
                ", rank=" + rank +
                ", lastUpdateTime=" + lastUpdateTime +
                '}';
    }
}
