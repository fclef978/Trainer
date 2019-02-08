package nitnc.kotanilab.trainer.fx.setting;

/**
 * ユーザ設定クラスです。
 * JavaBeanに対応していて、Saverクラスによる保存・復帰ができます。
 */
public class UserSetting {
    /**
     * 年齢
     */
    private int age;
    /**
     * ユーザ名
     */
    private String name;

    public UserSetting() {
    }

    public UserSetting(String name, int age) {
        this.age = age;
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
