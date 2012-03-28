package test;

import org.junit.Test;
import ru.workmap.HeadHunter.Address;
import ru.workmap.HeadHunter.Vacancy;
import ru.workmap.util.Settings;

/**
 * Created by IntelliJ IDEA.
 * User: chernish2
 * Date: 17.03.12
 * Time: 16:04
 * To change this template use File | Settings | File Templates.
 */
public class EncodingTest {

    @Test
    public void encodingTest(){
        String s = "Тест";
        Vacancy v = new Vacancy();
        v.setName(s);
        Address a = new Address();
        a.setCity(s);
        v.setAddress(a);

        String s2 = Settings.getProperty("v");
        System.out.println(s2);
    }
}
