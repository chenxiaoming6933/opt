/**
 * @title: TestService
 * @projectName resolve
 * @description: TODO
 * @author ChenXiaoMing
 * @date 2019/10/2915:01
 */

/**
 * @ClassName 测试同步方法
 * @Author XiaoMing
 * @Date
 * @Vsersion V1.0
 */
public class TestService {

    public void printStr(String str) {
        try {
            for (int i = 0; i < 100; i++) {
                System.out.println("is str===" + str);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
