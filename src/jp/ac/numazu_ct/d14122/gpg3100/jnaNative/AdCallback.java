package jp.ac.numazu_ct.d14122.gpg3100.jnaNative;


import com.sun.jna.Callback;

/**
 * コールバック関数です。
 */
public interface AdCallback extends Callback {
    public void invoke(int num);
}
