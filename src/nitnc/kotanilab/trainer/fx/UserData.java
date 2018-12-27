package nitnc.kotanilab.trainer.fx;

public class UserData {
    private int age;
    private String name;

    public UserData() {
    }

    public UserData(String name, int age) {
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
