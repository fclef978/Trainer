package nitnc.kotanilab.trainer.fx.setting;

public class UserSetting {
    private int age;
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
