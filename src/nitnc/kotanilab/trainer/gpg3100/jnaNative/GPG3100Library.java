package nitnc.kotanilab.trainer.gpg3100.jnaNative;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;

/**
 * JNAのインターフェースです。
 * unsignedは全てsignedになっていることに注意する必要があります。
 * TODO シングルトンかつスレッドセーフでないとPCI-3135の他の機能を使えない
 */
public interface GPG3100Library extends Library {
    /** インスタンス */
    GPG3100Library INSTANCE = (GPG3100Library)Native.loadLibrary("gpg3100", GPG3100Library.class);

    /**
     * アナログ入力デバイスのオープンを行い、以後デバイスへのアクセスを行なえるようにします。
     * @param nDevice オープンするデバイス番号を指定します。
     * @return 成否
     */
    int AdOpen(int nDevice);

    /**
     * アナログ入力デバイスのクローズを行い、デバイスアクセスのために使用されていた各種リソースの解放を行い、以後のデバイスへのアクセスを禁止します。
     * @param nDevice AdOpen 関数でオープンしたデバイス番号を指定してください。
     * @return 成否
     */
    int AdClose(int nDevice);

    /**
     * アナログ入力デバイスからデバイスの各種仕様の取得を行います。
     * @param nDevice AdOpen 関数でオープンしたデバイス番号を指定してください。
     * @param pBoardSpec アナログデバイスの仕様を格納する構造体(ADBOARDSPEC 構造体)へのポインタです。
     * @return 成否
     */
    int AdGetDeviceInfo(int nDevice, AdBoardSpec.ByReference pBoardSpec);

    /**
     * アナログ入力デバイスのコールバック関数の設定を行います。
     * @param nDevice AdOpen 関数でオープンしたデバイス番号を指定してください。
     * @param reserved 予約：0 を指定してください。
     * @param pEventProc サンプリング停止時に呼び出すユーザ・コールバック関数のアドレスを指定します。ユーザ・コールバック関数の呼び出しを行わない場合には、NULL を指定してください。
     * @param uData ユーザ・コールバック関数へ引き渡すユーザ・データを指定します。ユーザ・コールバック関数の呼び出しを行わない場合には、0 を指定してください。・デフォルト：0
     * @return 成否
     */
    int AdSetBoardConfig(int nDevice, int reserved, AdCallback pEventProc, int uData);

    /**
     * アナログ入力デバイスのコールバック関数が呼び出された要因を取得します。
     * @param nDevice AdOpen 関数でオープンしたデバイス番号を指定してください。
     * @param ulAdSmplEventFactor コールバック関数の発生要因を格納する変数へのポインタです。
     * @return 成否
     */
    int AdGetBoardConfig(int nDevice, LongByReference ulAdSmplEventFactor);

    /**
     * サンプリング条件設定関数(AdSetSamplingConfig 関数)、データ変換関数(AdDataConv 関数)で指定するサンプリング時の条件を設定する構造体です。
     * @param nDevice AdOpen 関数でオープンしたデバイス番号を指定してください。
     * @param pSamplConfig AD サンプリング構造体(ADSMPLEQ 構造体)へのポインタを指定します。
     * @return 成否
     */
    int AdSetSamplingConfig(int nDevice, AdSamplingRequest.ByReference pSamplConfig);

    /**
     * アナログ入力デバイスの現在設定されているサンプリング条件を取得します。
     * デバイスオープン直後に本関数を実行することにより AD サンプリング構造体(ADSMPLREQ 構造体)のデフォルトの設定値を取得することができます。
     * @param nDevice AdOpen 関数でオープンしたデバイス番号を指定してください。
     * @param pSamplConfig 現在の設定されているサンプリング条件を格納するための ADサンプリング構造体(ADSMPLREQ構造体)へのポインタを指定します。
     * @return 成否
     */
    int AdGetSamplingConfig(int nDevice, AdSamplingRequest.ByReference pSamplConfig);

    /**
     * アナログ入力デバイスから入力されたサンプリングデータを取得します。
     * @param nDevice AdOpen 関数でオープンしたデバイス番号を指定してください。
     * @param pSmplData アナログ入力デバイスから取得するサンプリングデータを格納するためのバッファへのポインタを指定します。
     * @param ulSmplNum アナログ入力デバイスから取得するサンプリングデータ件数を格納している変数へのポインタを指定します。関数実行後、サンプリングデータ件数を ulSmplNum に格納します。
     * @return 成否
     */
    int AdGetSamplingData(int nDevice, Pointer pSmplData, LongByReference ulSmplNum);

    /**
     * アナログ入力デバイスから入力されたサンプリングデータとデジタルデータを取得します。
     * @param nDevice AdOpen 関数でオープンしたデバイス番号を指定してください。
     * @param pSmplData アナログ入力デバイスから取得するサンプリングデータを格納するためのバッファへのポインタを指定します。
     * @param pDiData アナログ入力デバイスから取得するデジタルデータを格納するためのバッファへのポインタを指定します。
     * @param ulSmplNum アナログ入力デバイスから取得するサンプリングデータ件数を格納している変数へのポインタを指定します。関数実行後、サンプリングデータ件数を ulSmplNum に格納します。
     * @return 成否
     */
    int AdFifoGetSamplingData(int nDevice, Pointer pSmplData, Pointer pDiData, LongByReference ulSmplNum);

    /**
     * サンプリングバッファに格納されているサンプリングデータを取得します。
     * @param nDevice AdOpen 関数でオープンしたデバイス番号を指定してください。
     * @param lOffset サンプリングバッファ内のサンプリングデータを取得開始するオフセットアドレスを指定します。アドレスは件数単位で指定します。-1 を指定した場合、最新の ulSmplNum 件数分のデータを取得できます。
     * @param ulSmplNum サンプリングバッファから取得するサンプリングデータ件数が格納されている変数へのポインタを指定します。希望する件数を指定して本関数を呼び出します。本関数呼び出し後、実際に取得したデータ件数が格納されます。
     * @param pSmplData サンプリングバッファから取得するサンプリングデータを格納するためのバッファへのポインタを指定します。
     * @return 成否
     */
    int AdReadSamplingBuffer(int nDevice, long lOffset, LongByReference ulSmplNum, Pointer pSmplData);

    /**
     * サンプリング入力バッファ内のサンプリングデータをクリアします。
     * @param nDevice AdOpen 関数でオープンしたデバイス番号を指定してください。
     * @return 成否
     */
    int AdClearSamplingData(int nDevice);

    /**
     * アナログ入力デバイスの連続サンプリングをスタートさせます。
     * @param nDevice AdOpen 関数でオープンしたデバイス番号を指定してください。
     * @param uSyncFlag サンプリング入力処理を同期で行うか非同期で行うかを指定します。
     * @return 成否
     */
    int AdStartSampling(int nDevice, int uSyncFlag);

    /**
     * トリガ（EXTRG IN）が入力されるたびに、アナログ入力デバイスから 1 件のアナログ入力を行います。トリガを複数回入力することにより連続サンプリングが可能となります。
     * @param nDevice AdOpen 関数でオープンしたデバイス番号を指定してください。
     * @param ulChNo アナログ入力を行うチャンネルを指定します。1 つのチャンネルのみ指定できます。
     * @param ulRange 予約：0 を指定してください。
     * @param ulSingleDiff 予約：0 を指定してください。
     * @param ulTriggerMode 予約：0 を指定してください。
     * @param ulTrigEdge 予約：0 を指定してください。
     * @param ulSmplNum サンプリングを行う件数を 1～1,073,741,824 件の範囲で指定します。
     * @return 成否
     */
    int AdTriggerSampling(int nDevice, long ulChNo, long ulRange, long ulSingleDiff, long ulTriggerMode, long ulTrigEdge, long ulSmplNum);

    /**
     * 外部トリガが入力されるたびに、アナログ入力デバイスから指定件数のサンプリングを行います。指定回数までサンプリングを繰り返す事が可能です。（PCI-3161,3163）
     * @param nDevice AdOpen 関数でオープンしたデバイス番号を指定してください。
     * @param ulChCount 予約：0 を指定してください。
     * @param SmplChReq 予約：NULL を指定してください。
     * @param ulSmplNum 1 回のサンプリング件数を指定します。メモリ方式のデバイスでは 1,024 の倍数で 1,024 件 ～ デバイス搭載メモリ容量 の間で指定できます。
     * @param ulRepeatCount 指定件数のサンプリングの繰り返し回数を指定します。繰り返し回数は 1 ～｛（1 回のサンプリング件数）×（繰り返し回数）≦（最大設定可能サンプリング件数）｝になる回数のみ指定できます。
     * @param ulTrigEdge 予約：0 を指定してください。
     * @param fSmplFreq 予約：0 を指定してください。
     * @param ulEClkEdge 予約：0 を指定してください。
     * @param ulFastMode 予約：0 を指定してください。
     * @return 成否
     */
    int AdMemTriggerSampling(int nDevice, long ulChCount, Pointer SmplChReq, long ulSmplNum, long ulRepeatCount, long ulTrigEdge, float fSmplFreq, long ulEClkEdge, long ulFastMode);

    /**
     * 複数枚同期サンプリング機能を使用したサンプリングを行います。
     * @param nDevice AdOpen 関数でオープンしたデバイス番号を指定してください。
     * @param ulMode マスタで使用するかスレーブで使用するかを指定します。
     * @return 成否
     */
    int AdSyncSampling(int nDevice, long ulMode);

    /**
     * AdStartSampling 関数を非同期処理でスタートさせたアナログ入力デバイスの連続サンプリング入力を停止させます。
     * @param nDevice AdOpen 関数でオープンしたデバイス番号を指定してください。
     * @return 成否
     */
    int AdStopSampling(int nDevice);

    /**
     * アナログ入力デバイスのサンプリング動作状態を取得します。
     * @param uDevice AdOpen 関数でオープンしたデバイス番号を指定してください。
     * @param ulAdSmplStatus AD サンプリング状態を格納する変数へのポインタです。
     * @param ulAdSMplCount サンプリング済み件数を格納する変数へのポインタです。アナログ入力サンプリングのサンプリング済み件数を格納します。
     * @param ulAdAvailCount サンプリング残件数を格納する変数へのポインタです。アナログ入力サンプリングのサンプリングデータの残件数を格納します。
     * @return 成否
     */
    int AdGetStatus(int uDevice, LongByReference ulAdSmplStatus, LongByReference ulAdSMplCount, LongByReference ulAdAvailCount);

    /**
     * アナログ入力デバイスから 1 件のアナログ入力（1 件サンプリング）を行います。AdStartSampling関数を使用した通常の連続サンプリング入力とは異なり、デバイスのアナログ入力機能のみを利用します。
     * @param nDevice AdOpen 関数でオープンしたデバイス番号を指定してください。
     * @param uCh アナログ入力を行うチャンネル数を指定します。設定可能範囲は 1～そのデバイスで利用可能なチャンネル数です。必ず 1 以上の値を指定してください。
     * @param ulSingleDiff アナログ入力を行うチャンネル数を指定します。設定可能範囲は 1～そのデバイスで利用可能なチャンネル数です。必ず 1 以上の値を指定してください。
     * @param pSmplChReq アナログ入力を行うチャンネル番号、レンジを指定するための構造体配列（ADSMPLCHREQ 構造体）へのポインタを指定します。
     * @param pData アナログ入力したデータを格納する位置へのポインタを指定します。pData が指す位置にアナログ入力データを格納します。
     * @return 成否
     */
    int AdInputAD(int nDevice, long uCh, long ulSingleDiff, Pointer pSmplChReq, Pointer pData);

    /**
     * アナログ入力デバイスの汎用入力端子を読み出します。
     * @param nDevice AdOpen 関数でオープンしたデバイス番号を指定してください。
     * @param uData 入力したデジタルデータを格納する変数へのポインタです。
     * @return 成否
     */
    int AdInputDI(int nDevice, LongByReference uData);

    /**
     * アナログ入力デバイスの汎用出力端子へデータを出力します。
     * @param nDevice AdOpen 関数でオープンしたデバイス番号を指定してください。
     * @param uData 出力するデジタルデータを指定します。
     * @return 成否
     */
    int AdOutputDO(int nDevice, long uData);

    /**
     * アナログデータの形式を変換します。形式の変換とともにデータに対し平均処理やスムージング処理を行うことができます。
     * また、ユーザ関数の指定により、任意のデータ変換式を設定することも可能です。
     * @param nSrcFormCode pSrcData が指すデータ領域に格納されているデータ形式をデータ指定識別子にて指定します。
     * @param pSrcData 変換元データへのポインタを指定します。
     * @param nSrcSmplDataNum 変換元データ数を指定します。
     * @param pSrcSmplReq 変換元データのサンプリング条件が格納されている構造体(ADSMPLREQ 構造体)へのポインタを指定します。
     * @param nDestFormCode pDestData が指すデータ領域に格納されているデータ形式をデータ指定識別子にて指定します。
     * @param pDestData 変換後データへのポインタを指定します。
     * @param pnDestSmplDataNum 変換後データ数のポインタを指定します。
     * @param pDestSmplReq 変換後データのサンプリング条件を格納するための構造体(ADSMPLREQ 構造体)へのポインタを指定します。
     * @param nEffect 変換後データのサンプリング条件を格納するための構造体(ADSMPLREQ 構造体)へのポインタを指定します。
     * @param nCount 平均、スムージングのデータ数です。ulEffect で平均、スムージングを行うように指定したとき、平均、スムージングのデータ数を指定します。ulEffect に 0 が指定されている場合は、ulCount は無効です。
     * @param lpfnConv ユーザ関数へのポインタです。一通りのデータ変換を終えた後、ユーザ関数により任意の計算を行ってデータに変換を加えることができます。pfnConv には変換を行うための関数へのポインタを指定します。(fnConv関数参照)
     * ユーザ関数によるデータ変換を行わない場合は、pfnConv に NULL を指定してください。
     * @return 成否
     */
    int AdDataConv(int nSrcFormCode, Pointer pSrcData, int nSrcSmplDataNum, Pointer pSrcSmplReq, int nDestFormCode, Pointer pDestData, IntByReference pnDestSmplDataNum, Pointer pDestSmplReq, int nEffect, int nCount, AdConversionProcedure lpfnConv);

    /**
     * アナログ入力デバイスのフルスケール検出イベント条件、過電圧保護入力時自動レンジ切換イベントの設定を行います。
     * @param nDevice AdOpen 関数でオープンしたデバイス番号を指定してください。
     * @param ulEventMask フルスケール検出時のイベント、過電圧保護入力時自動レンジ切換イベントの発生条件を指定します。
     * @param ulStopMode フルスケール検出、過電圧保護入力時自動レンジ切り換え時のサンプリング停止条件を指定します。
     * @return 成否
     */
    int AdSetRangeEvent(int nDevice, long ulEventMask, long ulStopMode);

    /**
     * フルスケール検出イベントをリセットします。
     * @param nDevice AdOpen 関数でオープンしたデバイス番号を指定してください。
     * @return 成否
     */
    int AdResetRangeEvent(int nDevice);

    /**
     * アナログ入力デバイスのフルスケールレンジ検出イベントのステータスを取得します。
     * @param nDevice AdOpen 関数でオープンしたデバイス番号を指定してください。
     * @param ulEventChNo フルスケールレンジ検出チャンネルを格納する変数へのポインタを指定します。
     * @param ulEventStatus 検出された状態を格納する変数へのポインタを指定します。
     * @return 成否
     */
    int AdGetRangeEventStatus(int nDevice, LongByReference ulEventChNo, LongByReference ulEventStatus);

    /**
     * アナログ入力デバイスからのサンプリングデータをデータファイルに書き込みながら連続サンプリング入力を行います。
     * @param nDevice AdOpen 関数でオープンしたデバイス番号を指定してください。
     * @param szPathName サンプリングしたデータを保存するデータファイルへのパスを指定します。
     * @param ulFileFlag データファイルの形式を指定します。
     * @return 成否
     */
    int AdStartFileSampling(int nDevice, String szPathName, long ulFileFlag);
}
