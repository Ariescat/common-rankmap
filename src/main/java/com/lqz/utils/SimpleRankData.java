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

    @Override
    public int getRank() {
        return rank;
    }

    @Override
    public void setRank(int rank) {
        this.rank = rank;
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
