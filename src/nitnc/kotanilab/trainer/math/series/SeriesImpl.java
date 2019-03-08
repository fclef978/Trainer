package nitnc.kotanilab.trainer.math.series;

import nitnc.kotanilab.trainer.math.Unit;
import nitnc.kotanilab.trainer.math.point.AbstractPoint;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

/**
 * Seriesの実装スーパークラスです。
 *
 * @param <X> 点データのX軸のクラス
 * @param <Y> 点データのY軸のクラス
 * @param <E> 点データのクラス
 */
public class SeriesImpl<X extends Comparable<X>, Y extends Comparable<Y>, E extends AbstractPoint<? extends X, ? extends Y>> implements Series<X, Y, E> {

    protected Y yMax;
    protected Y yMin;
    protected List<E> list;
    protected Unit xUnit;
    protected Unit yUnit;

    /**
     * 空の系列データを作ります。
     *
     * @param yMax  y最大値
     * @param yMin  y最小値
     * @param xUnit x単位
     * @param yUnit y単位
     */
    public SeriesImpl(Y yMax, Y yMin, Unit xUnit, Unit yUnit) {
        init(yMax, yMin, xUnit, yUnit);
        list = new ArrayList<>();
    }

    /**
     * 空の系列データを作ります。物理的情報も空になります。
     *
     * @param yMax y最大値
     * @param yMin y最小値
     */
    public SeriesImpl(Y yMax, Y yMin) {
        this(yMax, yMin, Unit.none(), Unit.none());
    }

    /**
     * 浅いコピーですでにある系列データから新しい系列データを作ります。
     *
     * @param c     元になるコレクション
     * @param yMax  y最大値
     * @param yMin  y最小値
     * @param xUnit x単位
     * @param yUnit y単位
     */
    public SeriesImpl(Collection<? extends E> c, Y yMax, Y yMin, Unit xUnit, Unit yUnit) {
        init(yMax, yMin, xUnit, yUnit);
        list = new ArrayList<>(c);
    }

    /**
     * 指定した長さで領域を確保した新しい系列データを作ります。
     *
     * @param n     長さ
     * @param yMax  y最大値
     * @param yMin  y最小値
     * @param xUnit x単位
     * @param yUnit y単位
     */
    public SeriesImpl(int n, Y yMax, Y yMin, Unit xUnit, Unit yUnit) {
        init(yMax, yMin, xUnit, yUnit);
        list = new ArrayList<>(n);
    }

    /**
     * 初期化処理です。コンストラクタから呼び出されるため適宜オーバーライドしてください。
     *
     * @param yMax  y最大値
     * @param yMin  y最小値
     * @param xUnit x単位
     * @param yUnit y単位
     */
    protected void init(Y yMax, Y yMin, Unit xUnit, Unit yUnit) {
        this.yMax = yMax;
        this.yMin = yMin;
        this.xUnit = xUnit;
        this.yUnit = yUnit;
    }

    /**
     * 点データのX値のみを持つListを返します。
     * 順番はこのSeriesの順番を保持します。
     *
     * @return 点データのX値のみを持つList
     */
    public List<X> getXList() {
        List<X> ret = new ArrayList<>(this.size());
        this.forEach(point -> ret.add(point.getX()));
        return ret;
    }

    /**
     * 点データのY値のみを持つListを返します。
     * 順番はこのSeriesの順番を保持します。
     *
     * @return 点データのY値のみを持つList
     */
    public List<Y> getYList() {
        List<Y> ret = new ArrayList<>(this.size());
        this.forEach(point -> ret.add(point.getY()));
        return ret;
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append(super.toString());
        for (E elem : list) {
            ret.append(elem).append("\n");
        }
        return ret.toString();
    }

    @Override
    public Y getYMax() {
        return yMax;
    }

    @Override
    public Y getYMin() {
        return yMin;
    }

    @Override
    public void addFirst(E e) {
        list.add(0, e);
    }

    @Override
    public boolean add(E e) {
        return list.add(e);
    }

    @Override
    public E get(int index) {
        return list.get(index < 0 ? size() + index : index);
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public E remove(int index) {
        return list.remove(index);
    }

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public SeriesStream<Y> stream() {
        return new SeriesStream<>(this);
    }

    @Override
    public E set(int index, E e) {
        return list.set(index, e);
    }

    @Override
    public void forEach(Consumer<? super E> action) {
        list.forEach(action);
    }

    @Deprecated
    @Override
    public Collection<E> getEntity() {
        return list;
    }

    @Override
    public Unit getXUnit() {
        return xUnit;
    }

    @Override
    public Unit getYUnit() {
        return yUnit;
    }
}
