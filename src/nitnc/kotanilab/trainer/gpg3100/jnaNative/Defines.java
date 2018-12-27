package nitnc.kotanilab.trainer.gpg3100.jnaNative;

public class Defines {
    /**
     * sampling mode constants
     */
    public static final int FLAG_SYNC = 1;
    public static final int FLAG_ASYNC = 2;
    public static final int FLAG_TRIGGER = 4;
    public static final int FLAG_MASTER_MODE = 5;
    public static final int FLAG_SLAVE_MODE = 6;

    /**
     * ----------------------------------------------*
     * file type constants                       *
     * ----------------------------------------------
     */
    public static final int FLAG_BIN = 1;
    public static final int FLAG_CSV = 2;

    /**
     * ----------------------------------------------*
     * data formate type constants               *
     * ----------------------------------------------
     */
    public static final int CONV_BIN = 1;
    public static final int CONV_PHYS = 2;

    /**
     * ----------------------------------------------*
     * sampling status constants                 *
     * ----------------------------------------------
     */
    public static final int AD_STATUS_STOP_SAMPLING = 1;
    public static final int AD_STATUS_WAIT_TRIGGER = 2;
    public static final int AD_STATUS_NOW_SAMPLING = 3;

    /**
     * ----------------------------------------------*
     * sampling event constants                  *
     * ----------------------------------------------
     */
    public static final int AD_EVENT_SMPLNUM = 1;
    public static final int AD_EVENT_STOP_TRIGGER = 2;
    public static final int AD_EVENT_STOP_FUNCTION = 3;
    public static final int AD_EVENT_STOP_TIMEOUT = 4;
    public static final int AD_EVENT_STOP_SAMPLING = 5;
    public static final int AD_EVENT_STOP_SCER = 6;
    public static final int AD_EVENT_STOP_ORER = 7;
    public static final int AD_EVENT_SCER = 8;
    public static final int AD_EVENT_ORER = 9;
    public static final int AD_EVENT_STOP_LV_1 = 10;
    public static final int AD_EVENT_STOP_LV_2 = 11;
    public static final int AD_EVENT_STOP_LV_3 = 12;
    public static final int AD_EVENT_STOP_LV_4 = 13;
    public static final int AD_EVENT_RANGE = 14;
    public static final int AD_EVENT_STOP_RANGE = 15;
    public static final int AD_EVENT_OVPM = 16;
    public static final int AD_EVENT_STOP_OVPM = 17;

    /**
     * -----------------------------------------------*
     * sampling method constants                  *
     * -----------------------------------------------
     */
    public static final int AD_INPUT_SINGLE = 1;
    public static final int AD_INPUT_DIFF = 2;
    public static final int AD_INPUT_GND = 3;
    public static final int AD_INPUT_REFP5V = 4;
    public static final int AD_INPUT_REFM5V = 5;
    public static final int AD_INPUT_DACONV = 6;
    public static final int AD_INPUT_REFP3V = 7;
    public static final int AD_INPUT_REFM3V = 8;
    public static final int AD_INPUT_DAC1 = 9;
    public static final int AD_INPUT_DAC2 = 10;
    public static final int AD_INPUT_DAC3 = 11;
    public static final int AD_INPUT_DAC4 = 12;
    public static final int AD_INPUT_DAC5 = 13;
    public static final int AD_INPUT_DAC6 = 14;

    /**
     * ------------------------------------------------*
     * volume adjust constans                      *
     * ------------------------------------------------
     */
    public static final int AD_ADJUST_BIOFFSET = 1;
    public static final int AD_ADJUST_UNIOFFSET = 2;
    public static final int AD_ADJUST_BIGAIN = 3;
    public static final int AD_ADJUST_UNIGAIN = 4;

    /**
     * ------------------------------------------------*
     * volume adjust constants                     *
     * ------------------------------------------------
     */
    public static final int AD_ADJUST_UP = 1;
    public static final int AD_ADJUST_DOWN = 2;
    public static final int AD_ADJUST_STORE = 3;
    public static final int AD_ADJUST_STANDBY = 4;
    public static final int AD_ADJUST_NOT_STORE = 5;
    public static final int AD_ADJUST_STORE_INITAREA = 6;
    public static final int AD_ADJUST_READ_FACTORY = 1;
    public static final int AD_ADJUST_READ_USER = 2;

    /**
     * ------------------------------------------------*
     * data conversion constants                   *
     * ------------------------------------------------
     */
    public static final int AD_DATA_PHYSICAL = 1;
    public static final int AD_DATA_BIN8 = 2;
    public static final int AD_DATA_BIN12 = 3;
    public static final int AD_DATA_BIN16 = 4;
    public static final int AD_DATA_BIN24 = 5;
    public static final int AD_DATA_BIN10 = 6;

    /**
     * -----------------------------------------------*
     * data conversion constants                   *
     * ------------------------------------------------
     */
    public static final int AD_CONV_SMOOTH = 1;
    public static final int AD_CONV_AVERAGE1 = 2;
    public static final int AD_CONV_AVERAGE2 = 3;

    /**
     * -----------------------------------------------*
     * sampling formula constants                  *
     * ------------------------------------------------
     */
    public static final int AD_IO_SAMPLING = 1;
    public static final int AD_FIFO_SAMPLING = 2;
    public static final int AD_MEM_SAMPLING = 4;
    public static final int AD_BM_SAMPLING = 8;

    /**
     * -----------------------------------------------*
     * trigger point constants                     *
     * ------------------------------------------------
     */
    public static final int AD_TRIG_START = 1;
    public static final int AD_TRIG_STOP = 2;
    public static final int AD_TRIG_START_STOP = 3;

    /**
     * -----------------------------------------------*
     * trigger constants                           *
     * ------------------------------------------------
     */
    public static final int AD_FREERUN = 1;
    public static final int AD_EXTTRG = 2;
    public static final int AD_EXTTRG_DI = 3;
    public static final int AD_LEVEL_P = 4;
    public static final int AD_LEVEL_M = 5;
    public static final int AD_LEVEL_D = 6;
    public static final int AD_INRANGE = 7;
    public static final int AD_OUTRANGE = 8;
    public static final int AD_ETERNITY = 9;
    public static final int AD_SMPLNUM = 10;

    public static final int AD_START_SIGTIMER = 11;
    public static final int AD_START_DA_START = 12;
    public static final int AD_START_DA_STOP = 13;
    public static final int AD_START_DA_IO = 14;
    public static final int AD_START_DA_SMPLNUM = 15;
    public static final int AD_STOP_SIGTIMER = 11;
    public static final int AD_STOP_DA_START = 12;
    public static final int AD_STOP_DA_STOP = 13;
    public static final int AD_STOP_DA_IO = 14;
    public static final int AD_STOP_DA_SMPLNUM = 15;

    public static final int AD_START_P1 = 0x00000010;
    public static final int AD_START_M1 = 0x00000020;
    public static final int AD_START_D1 = 0x00000040;
    public static final int AD_START_P2 = 0x00000080;
    public static final int AD_START_M2 = 0x00000100;
    public static final int AD_START_D2 = 0x00000200;
    public static final int AD_STOP_P1 = 0x00000400;
    public static final int AD_STOP_M1 = 0x00000800;
    public static final int AD_STOP_D1 = 0x00001000;
    ;
    public static final int AD_STOP_P2 = 0x00002000;
    public static final int AD_STOP_M2 = 0x00004000;
    public static final int AD_STOP_D2 = 0x00008000;
    public static final int AD_ANALOG_FILTER = 0x00010000;
    public static final int AD_START_CNT_EQ = 0x00020000;
    public static final int AD_STOP_CNT_EQ = 0x00040000;
    public static final int AD_START_DI_EQ = 0x00080000;
    public static final int AD_STOP_DI_EQ = 0x00100000;
    public static final int AD_STOP_SOFT = 0x00200000;
    public static final int AD_START_Z_CLR = 0x00400000;
    public static final int AD_STOP_Z_CLR = 0x00800000;
    public static final int AD_START_SYNC1 = 0x10000000;    // Start-trigger: Internal Sync1 trigger
    public static final int AD_START_SYNC2 = 0x20000000;    // Start-trigger: Internal Sync2 trigger
    public static final int AD_STOP_SYNC1 = 0x40000000;    // Stop-trigger: Internal Sync1 trigger
    public static final int AD_STOP_SYNC2 = 0x80000000;    // Stop-trigger: Internal Sync2 trigger

    /**
     * ---------------------------------------------*
     * trigger edge constants                    *
     * ----------------------------------------------
     */
    public static final int AD_DOWN_EDGE = 1;
    public static final int AD_UP_EDGE = 2;
    public static final int AD_EXTRG_IN = 3;
    public static final int AD_EXCLK_IN = 4;
    public static final int AD_LOW_LEVEL = 5;
    public static final int AD_HIGH_LEVEL = 6;

    public static final int AD_EDGE_P1 = 0x0010;
    public static final int AD_EDGE_M1 = 0x0020;
    public static final int AD_EDGE_D1 = 0x0040;
    public static final int AD_EDGE_P2 = 0x0080;
    public static final int AD_EDGE_M2 = 0x0100;
    public static final int AD_EDGE_D2 = 0x0200;
    public static final int AD_DISABLE = 0x80000000;

    public static final int AD_TRIG_MODE = 2;
    public static final int AD_BUSY_MODE = 3;
    public static final int AD_POST_MODE = 4;
    public static final int AD_ENABLE = 5;
    public static final int AD_SMP1_MODE = 6;
    public static final int AD_SMP2_MODE = 7;
    public static final int AD_ATRIG_MODE = 8;

    /**
     * -----------------------------------------------*
     * analog trigger edge constants               *
     * ------------------------------------------------
     */
    public static final int AD_LOW_PULSE = 1;
    public static final int AD_HIGH_PULSE = 2;

    /**
     * -----------------------------------------------*
     * fast mode constants                         *
     * ------------------------------------------------
     */
    public static final int AD_NORMAL_MODE = 1;
    public static final int AD_FAST_MODE = 2;

    /**
     * -----------------------------------------------*
     * status                                      *
     * ------------------------------------------------
     */
    public static final int AD_NO_STATUS = 1;
    public static final int AD_ADD_STATUS = 2;

    /**
     * -----------------------------------------------*
     * error of busmaster clock                    *
     * ------------------------------------------------
     */
    public static final int AD_STOP_SCER = 2;
    public static final int AD_STOP_ORER = 4;

    /**
     * -----------------------------------------------*
     * write way of busmaster data                 *
     * ------------------------------------------------
     */
    public static final int AD_APPEND = 1;
    public static final int AD_OVERWRITE = 2;

    /**
     * -----------------------------------------------*
     * Degital Filter                              *
     * ------------------------------------------------
     */
    public static final int AD_DF_8 = 0;    // 8 (default setting)
    public static final int AD_DF_16 = 1;    // 16
    public static final int AD_DF_32 = 2;    // 32
    public static final int AD_DF_64 = 3;    // 64
    public static final int AD_DF_128 = 4;    // 128
    public static final int AD_DF_256 = 5;    // 256

    /**
     * -----------------------------------------------*
     * sampling range constants                    *
     * ------------------------------------------------
     */
    public static final int AD_0_1V = 0x00000001;
    public static final int AD_0_2P5V = 0x00000002;
    public static final int AD_0_5V = 0x00000004;
    public static final int AD_0_10V = 0x00000008;
    public static final int AD_1_5V = 0x00000010;
    public static final int AD_0_2V = 0x00000020;
    public static final int AD_0_0P125V = 0x00000040;
    public static final int AD_0_1P25V = 0x00000080;
    public static final int AD_0_0P625V = 0x00000100;
    public static final int AD_0_0P156V = 0x00000200;
    public static final int AD_0_20mA = 0x00001000;
    public static final int AD_4_20mA = 0x00002000;
    public static final int AD_20mA = 0x00004000;
    public static final int AD_1V = 0x00010000;
    public static final int AD_2P5V = 0x00020000;
    public static final int AD_5V = 0x00040000;
    public static final int AD_10V = 0x00080000;
    public static final int AD_20V = 0x00100000;
    public static final int AD_50V = 0x00200000;
    public static final int AD_0P125V = 0x00400000;
    public static final int AD_1P25V = 0x00800000;
    public static final int AD_0P625V = 0x01000000;
    public static final int AD_0P156V = 0x02000000;
    public static final int AD_1P25V_AC = 0x04000000;
    public static final int AD_0P625V_AC = 0x08000000;
    public static final int AD_0P156V_AC = 0x10000000;
    public static final int AD_AC_COUPLING = 0x40000000;
    public static final int AD_GND = 0x80000000;
    ;
    /**
     * ------------------------------------------------*
     * isolation constants                          *
     * -------------------------------------------------
     */
    public static final int AD_ISOLATION = 1;
    public static final int AD_NOT_ISOLATION = 2;

    /**
     * ------------------------------------------------*
     * sync sampling constants                      *
     * -------------------------------------------------
     */
    public static final int AD_MASTER_MODE = 1;
    public static final int AD_SLAVE_MODE = 2;

    /**
     * ------------------------------------------------*
     * sync Number constants                        *
     * -------------------------------------------------
     */
    public static final int AD_SYNC_NUM_1 = 0x0100;
    public static final int AD_SYNC_NUM_2 = 0x0200;
    public static final int AD_SYNC_NUM_3 = 0x0400;
    public static final int AD_SYNC_NUM_4 = 0x0800;
    public static final int AD_SYNC_NUM_5 = 0x1000;
    public static final int AD_SYNC_NUM_6 = 0x2000;
    public static final int AD_SYNC_NUM_7 = 0x4000;

    /**
     * ------------------------------------------------*
     * calibration                                  *
     * -------------------------------------------------
     */
    public static final int AD_SELF_CALIBRATION = 1;
    public static final int AD_ZEROSCALE_CALIBRATION = 2;
    public static final int AD_FULLSCALE_CALIBRATION = 3;

    /**
     * ------------------------------------------------*
     * over/under range status                      *
     * -------------------------------------------------
     */
    public static final int AD_STATUS_UNDER_RANGE = 1;
    public static final int AD_STATUS_OVER_RANGE = 2;

    /**
     * ------------------------------------------------*
     * compare data status                          *
     * -------------------------------------------------
     */
    public static final int AD_STATUS_UP_DATA = 1;
    public static final int AD_STATUS_DOWN_DATA = 2;

    /**
     * ------------------------------------------------*
     * PCI-3525 CN3,4 function                      *
     * -------------------------------------------------
     */
    public static final int AD_CN_FREE = 0;
    public static final int AD_CN_EXTRG_IN = 1;
    public static final int AD_CN_EXTRG_OUT = 2;
    public static final int AD_CN_EXCLK_IN = 3;
    public static final int AD_CN_EXCLK_OUT = 4;
    public static final int AD_CN_EXINT_IN = 5;
    public static final int AD_CN_ATRG_OUT = 6;
    public static final int AD_CN_DI = 7;
    public static final int AD_CN_DO = 8;
    public static final int AD_CN_EXSMP1_OUT = 12;
    public static final int AD_CN_EXSMP2_OUT = 13;
    public static final int AD_CN_DIO = 1;    // DIO
    public static final int AD_CN_CONTROL = 2;    // Control used
    public static final int AD_CN_CNT = 3;    // Counter used

    /**
     * ------------------------------------------------*
     * CPZ-360810 function                          *
     * -------------------------------------------------
     */
    public static final int AD_EX_DIO1 = 1;
    public static final int AD_EX_DIO2 = 2;
    public static final int AD_EX_DIO3 = 3;
    public static final int AD_EX_DIO4 = 4;
    public static final int AD_EX_DIO5 = 5;
    public static final int AD_EX_DIO6 = 6;
    public static final int AD_EX_DIO7 = 7;
    public static final int AD_EX_DIO8 = 8;
}
