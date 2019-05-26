public interface IRankData<K, S, V> extends Comparable<V> {

    K getID();

    S getScore();
}
