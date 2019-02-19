package nitnc.kotanilab.trainer.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.UnaryOperator;

public class CallbackArrayList<T> extends ArrayList<T> {
    private UnaryOperator<T> addCallback = t -> t;
    private UnaryOperator<T> removeCallback = t -> t;

    public CallbackArrayList(int initialCapacity) {
        super(initialCapacity);
    }

    public CallbackArrayList() {
    }

    public CallbackArrayList(Collection<? extends T> c) {
        super(c);
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

    public UnaryOperator<T> getAddCallback() {
        return addCallback;
    }

    public void setAddCallback(UnaryOperator<T> addCallback) {
        this.addCallback = addCallback;
    }

    public UnaryOperator<T> getRemoveCallback() {
        return removeCallback;
    }

    public void setRemoveCallback(UnaryOperator<T> removeCallback) {
        this.removeCallback = removeCallback;
    }
}
