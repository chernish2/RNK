package test;

import junit.framework.Assert;
import org.junit.Test;
import org.xml.sax.SAXException;
import ru.workmap.HeadHunter.Result;
import ru.workmap.util.PageFetcher;

import javax.xml.bind.JAXBException;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: chernish2
 * Date: 30.10.11
 * Time: 19:06
 * To change this template use File | Settings | File Templates.
 */
public class PageFetcherTest {
    @Test
    public void test() throws Exception, IOException, SAXException {
        PageFetcher<Result> pageFetcher = new PageFetcher<Result>("http://api.hh.ru/1/xml/vacancy/search/", new Result());
        Result result = pageFetcher.call();
        Assert.assertTrue(result.found > 0);

    }
}
