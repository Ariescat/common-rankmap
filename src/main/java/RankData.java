public class RankData implements IRankData<Integer, Integer, RankData> {

    private int playerID;
    private int score;
    private long time;

    public RankData(int playerID, int score) {
        this.playerID = playerID;
        this.score = score;
        this.time = System.currentTimeMillis();
    }

    @Override
    public Integer getID() {
        return playerID;
    }

    @Override
    public Integer getScore() {
        return score;
    }

    public long getTime() {
        return time;
    }

    /**
     * 比较策略：
     * 1.score比较
     * 2.若score相同，则time比较
     * 3.若time相同，则ID比较（注：第3步一定不能省，若time相同不管的话TreeMap会丢失后面的更新）
     */
    @Override
    public int compareTo(RankData o) {
        return o.getScore().equals(score)
                ? (o.getTime() == time ? o.getID() - playerID : (int) (o.getTime() - time))
                : (o.getScore() - score);
    }

    @Override
    public String toString() {
        return "RankData{" +
                "playerID=" + playerID +
                ", score=" + score +
                ", time=" + time +
                '}';
    }
}
