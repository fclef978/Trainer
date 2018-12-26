package jp.ac.numazu_ct.d14122.math;

import java.lang.reflect.Array;

/**
 * プリミティブのdoubleの簡素なキューです。
 * スレッドセーフにする予定。
 */
public class DoubleQueue {
    double[] elements;
    int rear = 0;
    int front = 0;

    /*
    public void offer(int e) {
        elements[tail] = e;
        tail = (tail + 1) % elements.length;
    }

    public int poll() {
        int result = elements[head];
        head = (head + 1)  % elements.length;
        return result;
    }
    */
}
