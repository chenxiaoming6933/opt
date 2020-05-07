package util; /**
 * @title: SpringContentUtil
 * @projectName resolve
 * @description: TODO
 * @author ChenXiaoMing
 * @date 2019/9/614:35
 */

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @ClassName spring工厂工具
 * @Author XiaoMing
 * @Date
 * @Vsersion V1.0
 */
public class SpringContentUtil {

    private static ClassPathXmlApplicationContext springContent;

    public static Object getBeans(Class obj) {
        if (null == springContent) {
            synchronized (SpringContentUtil.class) {
                springContent = new ClassPathXmlApplicationContext("spring.xml");
            }
        }
        return springContent.getBean(obj);
    }
}
