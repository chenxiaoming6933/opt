/**
 * @title: TestThreadMain
 * @projectName resolve
 * @description: TODO
 * @author ChenXiaoMing
 * @date 2019/10/2915:10
 */

/**
 *@ClassName
 *@Author XiaoMing
 *@Date
 *@Vsersion V1.0
 */
public class TestThreadMain {
    public static void main(String[] args) {
        try {
            TestService testService = new TestService();
            TestThred testThred = new TestThred(testService,"A线程");
            TestThred testThred1 = new TestThred(testService,"B线程");

            testThred.start();
            testThred.join(300);
            testThred1.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
