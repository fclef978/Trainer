package nitnc.kotanilab.trainer.math.series;

public interface Regenerable<T, Y extends Comparable<Y>> {
    T regenerate(SeriesStream<Y> stream);
}
