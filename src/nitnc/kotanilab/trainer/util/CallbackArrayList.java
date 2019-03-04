package nitnc.kotanilab.trainer.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.UnaryOperator;

/**
 * データの追加、削除時に指定したコールバック演算子を対象のオブジェクト適用するArrayListです。
 * @param <T> このリスト内に存在する要素の型
 */
public class CallbackArrayList<T> extends ArrayList<T> {
    private UnaryOperator<T> addCallback = t -> t;
    private UnaryOperator<T> removeCallback = t -> t;

    /**
     * 指定された初期容量で空のリストを作成します。
     * @param initialCapacity リストの初期容量
     */
    public CallbackArrayList(int initialCapacity) {
        super(initialCapacity);
    }

    /**
     * 初期容量 10 で空のリストを作成します。
     */
    public CallbackArrayList() {
        super();
    }

    /**
     * 指定されたコレクションの要素が含まれているリストを、要素がコレクションのイテレータによって返される順序で作成します。
     * @param c 要素がリストに配置されるコレクション
     */
    public CallbackArrayList(Collection<? extends T> c) {
        super(c);
    }

    public UnaryOperator<T> getAddCallback() {
        return addCallback;
    }

    /**
     * データ追加時のコールバックを設定します。
     * このコールバックはadd(),addAll(),set()時に実行されます。
     * @param addCallback
     */
    public void setAddCallback(UnaryOperator<T> addCallback) {
        this.addCallback = addCallback;
    }

    public UnaryOperator<T> getRemoveCallback() {
        return removeCallback;
    }

    /**
     * データ削除時のコールバックを設定します。
     * このコールバックはremove(),removeAll(),clear()時に実行されます。
     * @param removeCallback
     */
    public void setRemoveCallback(UnaryOperator<T> removeCallback) {
        this.removeCallback = removeCallback;
    }

    @Override
    public boolean add(T t) {
        return super.add(addCallback.apply(t));
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        int a = size();
        c.forEach(this::add);
        return a == size();
    }

    @Override
    public T set(int index, T element) {
        return super.set(index, addCallback.apply(element));
    }

    @Override
    public void clear() {
        forEach(t -> removeCallback.apply(t));
        super.clear();
    }

    @Override
    public T remove(int index) {
        return removeCallback.apply(super.remove(index));
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean remove(Object o) {
        return super.remove(removeCallback.apply((T) o));
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        int a = size();
        c.forEach(this::remove);
        return a == size();
    }
}
