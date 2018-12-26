package jp.ac.numazu_ct.d14122.gpg3100.jnaNative;

import com.sun.jna.Callback;
import com.sun.jna.Pointer;

public interface AdConversionProcedure extends Callback {
    public void invoke(int wCh, long dwCount, Pointer pDat);
}
