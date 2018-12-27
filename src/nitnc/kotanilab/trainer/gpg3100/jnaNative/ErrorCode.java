package nitnc.kotanilab.trainer.gpg3100.jnaNative;

/**
 * エラーコードを記述するクラスです。
 */
public class ErrorCode {
    /**
     * 正常終了
     */
    public static final int AD_ERROR_SUCCESS = 0;
    /**
     * ドライバが呼び出せません
     * 指定されたデバイスが見つかりませんでした。
     * 指定しているデバイス番号のデバイスが存在するかどうかを確認してください。
     */
    public static final int AD_ERROR_NOT_DEVICE = 0xC0000001;
    /**
     * ドライバがオープンできません
     * デバイスのオープン時に何らかのエラーが発生しました。
     */
    public static final int AD_ERROR_NOT_OPEN = 0xC0000002;
    /**
     * デバイス番号が正しくありません
     * -255 以外のデバイス番号でオープンを行おうとしました。
     * オープン関数でオープンしていないデバイス番号を指定しました。
     */
    public static final int AD_ERROR_INVALID_DEVICENO = 0xC0000003;
    /** Win用 */
    public static final int AD_ERROR_INVALID_HANDLE = 0xC0000003;
    /**
     * 既にオープンしているデバイスです
     * 既にオープンされているデバイスをオープンしようとしました。
     */
    public static final int AD_ERROR_ALREADY_OPEN = 0xC0000004;
    /** 不明 */
    public static final int AD_ERROR_INVALID_DEVICE_NUMBER = 0xC0000005;
    /**
     * デバイスがサポートしていない関数です
     * デバイスがサポートしていない機能を制御する関数です。
     */
    public static final int AD_ERROR_NOT_SUPPORTED = 0xC0000009;

    /**
     * サンプリングを実行中です
     * サンプリング実行中に再度サンプリングを実行しようとしました。
     * ンプリング実行中に実行できない関数を呼び出そうとしました。
     */
    public static final int AD_ERROR_NOW_SAMPLING = 0xC0001001;
    /**
     * サンプリングは停止中です
     * サンプリング停止中に実行できない関数を呼び出そうとしました。
     */
    public static final int AD_ERROR_STOP_SAMPLING = 0xC0001002;
    /** 不明 */
    public static final int AD_ERROR_START_SAMPLING = 0xC0001003;
    /** 不明 */
    public static final int AD_ERROR_SAMPLING_TIMEOUT = 0xC0001004;
    /**
     * パラメータが不正です
     * 引数の値が不正です。引数の値が指定範囲外です。
     */
    public static final int AD_ERROR_INVALID_PARAMETER = 0xC0001021;
    /**
     * NULL ポインタを指定しました
     * サンプリングデータ領域の指定に NULL を指定しました。
     * データ変換関数において、データ変換元となるデータへのポインタが NULL で指定された。
     * 変換後のデータを返すためのポインタがNULL で指定された。
     */
    public static final int AD_ERROR_NULL_POINTER = 0xC0001023;
    /**
     * サンプリングデータの取得ができませんでした
     * まだ、サンプリングが行われていないのでサンプリングデータの取得ができませんでした。
     * サンプリングバッファがクリアされているのでサンプリングデータが取得できませんでした。
     */
    public static final int AD_ERROR_GET_DATA = 0xC0001024;
    /**
     * DA ドライバ使用中です。
     * DA 側で使用中の機能を停止してから、関数を実行しなおしてください。
     */
    public static final int AD_ERROR_USED_DA = 0xC0001025;
    /**
     * ファイルのオープンに失敗しました
     * ファイルのパス名が存在しません。存在しないドライブを指定しました。
     */
    public static final int AD_ERROR_FILE_OPEN = 0xC0001041;
    /**
     * ファイルのクローズに失敗しました
     * ファイルアクセス時に何らかのエラーが発生しました。
     * データファイルに異常がないかご確認ください。
     */
    public static final int AD_ERROR_FILE_CLOSE = 0xC0001042;
    /**
     * ファイルのリードに失敗しました
     * ファイルアクセス時に何らかのエラーが発生しました。データファイルに異常がないかご確認ください。
     */
    public static final int AD_ERROR_FILE_READ = 0xC0001043;
    /**
     * ファイルのライトに失敗しました
     * ファイルアクセス時に何らかのエラーが発生しました。データファイルに異常がないかご確認ください。
     */
    public static final int AD_ERROR_FILE_WRITE = 0xC0001044;
    /**
     * データ形式が無効です
     * データ形式をご確認ください。
     */
    public static final int AD_ERROR_INVALID_DATA_FORMAT = 0xC0001061;
    /**
     * 平均またはスムージングの指定が正しくありません
     * 平均またはスムージングの回数が０またはデータ件数を超えている。
     * 平均またはスムージングが指定されているのにも関わらず、平均またはスムージングの件数が 0 に指定された。
     */
    public static final int AD_ERROR_INVALID_AVERAGE_OR_SMOOTHING = 0xC0001062;
    /**
     * データ変換元として指定されたデータが正しくありません
     * データ変換元のアドレス指定箇所などに間違いがないかご確認ください。
     */
    public static final int AD_ERROR_INVALID_SOURCE_DATA = 0xC0001063;
    /**
     * メモリが確保できません。
     * ドライバ内部で作業するためのメモリの確保に失敗しました。
     */
    public static final int AD_ERROR_NOT_ALLOCATE_MEMORY = 0xC0001081;
    /** 不明*/
    public static final int AD_ERROR_CALIBRATION = 0xC0001084;
    /** 不明 */
    public static final int AD_ERROR_NOT_THREAD = 0xC0001090;
    /** 不明 */
    public static final int AD_ERROR_NOT_DEVICE_NODE = 0xC0001091;

    public static final int AD_ERROR_USBIO_FAILED = 0xC0001085;
    public static final int AD_ERROR_USBIO_TIMEOUT = 0xC0001086;
    public static final int AD_ERROR_USBLIB_LOAD_FAILED = 0xC0001087;
}
