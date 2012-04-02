package test;

import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.SAXException;
import ru.workmap.HHSearcher;
import ru.workmap.HeadHunter.HHSearchResult;
import ru.workmap.HeadHunter.Vacancy;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by IntelliJ IDEA.
 * User: chernish2
 * Date: 22.03.12
 * Time: 10:31
 * To change this template use File | Settings | File Templates.
 */
public class HHSearcherTest {

    @Test
    public void searchVacancyTest() throws JAXBException, IOException, SAXException, ExecutionException, InterruptedException {
        HHSearcher hhSearcher = new HHSearcher();
        String key = "стилист";
        hhSearcher.setText(key);
        hhSearcher.setMapCoords(0, 0, 180, 180);
        HHSearchResult searchResult = hhSearcher.getVacancies();
        for (Vacancy vacancy : searchResult.getVacancyList()) {
            Assert.assertTrue(vacancy.getDescription().contains(key) || vacancy.getName().contains(key));
        }
    }
}
