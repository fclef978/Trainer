package jp.ac.numazu_ct.d14122.math.series;

public interface Regenerable<T, Y extends Comparable<Y>> {
    T regenerate(SeriesStream<Y> stream);
}
