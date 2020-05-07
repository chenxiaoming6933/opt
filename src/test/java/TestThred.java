/**
 * @title: TestThred
 * @projectName resolve
 * @description: TODO
 * @author ChenXiaoMing
 * @date 2019/10/2915:03
 */

/**
 * @ClassName 线程测试类
 * @Author XiaoMing
 * @Date
 * @Vsersion V1.0
 */
public class TestThred extends Thread {
    private TestService service;

    private String str;

    public TestThred(TestService service, String str) {
        this.service = service;
        this.str = str;
    }

    public void run() {
        for (int i = 0; i < 100; i++) {
            System.out.println("is str===" + this.str);
        }
    }
}
