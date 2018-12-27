package nitnc.kotanilab.trainer.math.series;

import nitnc.kotanilab.trainer.math.point.AbstractPoint;
import nitnc.kotanilab.trainer.math.point.Point;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * 二次元の系列データを表すインターフェースです。
 * java.util.Listと似たメソッドをいくつか持ちます。
 * Created by Hirokazu SUZUKI on 2018/07/31.
 */
public interface Series<X extends Comparable<X>, Y extends Comparable<Y>, E extends AbstractPoint<? extends X, ? extends Y>> {

    /**
     * y軸の最大値を返します。
     *
     * @return y軸最大値
     */
    Y getYMax();

    /**
     * Y軸の最小値を返します。
     *
     * @return y軸最小値
     */
    Y getYMin();

    /**
     * 指定された点データをこの系列データの末尾に追加します。
     *
     * @param e この系列データに追加される点データ
     * @return true
     */
    boolean add(E e);

    /**
     * 指定された点データをこの系列リストの最初に挿入します。
     *
     * @param e この系列データに追加される点データ
     */
    void addFirst(E e);

    /**
     * この系列データ内の指定された位置にある点データを、指定された点データに置き換えます。
     *
     * @param index 置換される点データのインデックス
     * @param e     指定された位置に格納される点データ
     * @return 指定された位置に以前あった点データ
     */
    E set(int index, E e);

    /**
     * この系列データ内の指定された位置にある点データを返します。
     *
     * @param index 返される点データのインデックス
     * @return この系列データ内の指定された位置にある点データ
     */
    E get(int index);

    /**
     * この系列データ内の指定された位置にある点データを削除します。
     *
     * @param index 削除される点データのインデックス
     * @return 指定された位置に以前あった点データ
     */
    E remove(int index);

    /**
     * すべての点データをこの系列データから削除します。
     * この呼出しが戻ると、この系列データは空になります。
     */
    void clear();

    /**
     * この系列データ内にある点データの数を返します。
     *
     * @return この系列データ内の点データ数
     */
    int size();

    /**
     * この系列データをソースとして使用して、SeriesStreamを返します。
     *
     * @return この系列データ内の要素に対するSeriesStream
     */
    SeriesStream<Y> stream();

    /**
     * この系列データの各点データに対して指定されたアクションを、すべての点データが処理されるか、アクションが例外をスローするまで実行します。
     *
     * @param action 各点データに対して実行されるアクション
     */
    void forEach(Consumer<? super E> action);

    /**
     * この系列データがデータを保持するために用いているCollectionを返します。
     *
     * @return この系列データがデータを保持するために用いているCollection
     */
    @Deprecated
    Collection<E> getEntity();

    /**
     * この系列データのX軸の単位の定義を返します。
     *
     * @return この系列データのX軸の単位の定義
     */
    Unit getXUnit();

    /**
     * この系列データのY軸の単位の定義を返します。
     *
     * @return この系列データのY軸の単位の定義
     */
    Unit getYUnit();

}
