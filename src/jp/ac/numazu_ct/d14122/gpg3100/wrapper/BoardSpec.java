package jp.ac.numazu_ct.d14122.gpg3100.wrapper;

import jp.ac.numazu_ct.d14122.adConverter.ConverterSpec;
import jp.ac.numazu_ct.d14122.gpg3100.jnaNative.AdBoardSpec;

/**
 * AdBoardSpec構造体のラッパです。
 * ConverterSpecクラスとして振舞えます。
 */
public class BoardSpec extends ConverterSpec {

    private AdBoardSpec entity;

    /**
     * 指定したAdBoardSpec構造体で作成します。
     * @param entity 機器デフォルトのAdBoardSpec構造体です。
     */
    public BoardSpec(AdBoardSpec entity) {
        super("PCI-"+entity.boardType, (int)entity.chCountSingleEnd, (int)entity.resolution, 5.0);
        this.entity = entity;
    }

    public AdBoardSpec getEntity() {
        return entity;
    }
}
