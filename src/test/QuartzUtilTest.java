package test;

import org.junit.Test;
import ru.workmap.util.QuartzUtil;

/**
 * Created by IntelliJ IDEA.
 * User: chernish2
 * Date: 22.03.12
 * Time: 9:58
 * To change this template use File | Settings | File Templates.
 */
public class QuartzUtilTest {

    @Test
    public void test() throws InterruptedException {
        QuartzUtil quartzUtil = QuartzUtil.getInstance();
        Thread.sleep(60000);
        quartzUtil.destroy();
    }
}
