package bean;

import java.io.Serializable;

/**
 *@ClassName 测试提交用户bean
 *@Author XiaoMing
 *@Date
 *@Vsersion V1.0
 */
public class UserBean implements Serializable {

    private  String userName;

    private  String passWord;

    private  boolean sex;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }
}
