import org.xml.sax.SAXException;
import ru.workmap.HeadHunter.Region;
import ru.workmap.HeadHunter.Regions;
import ru.workmap.util.PageFetcher;

import javax.xml.bind.JAXBException;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: chernish2
 * Date: 31.10.11
 * Time: 11:49
 * To change this template use File | Settings | File Templates.
 */
public class Main {
    public static void main(String[] args) throws JAXBException, IOException, SAXException {
        Regions regions = new PageFetcher<Regions>("http://api.hh.ru/1/xml/region/all", new Regions()).call();
        System.out.println("Fetched " + regions.regions.size() + " regions");
        regions.compact();
        regions.setAllParentRegions();
        regions.locateAll();
        regions.save("allregions.xml");
    }
}
