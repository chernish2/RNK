package ru.workmap.HeadHunter;

import org.xml.sax.SAXException;
import ru.workmap.util.PageFetcher;
import ru.workmap.yandex.YMaps;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: chernish2
 * Date: 31.10.11
 * Time: 11:25
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement(name = "regions")
public class Regions extends IHH{
        public static final String key = "AA4rnU4BAAAAe7NpVgIAtqf4KKBB1LupH041c7cZlHoKH-YAAAAAAAAAAAD688Dy35GTPUxy9dydUdPwSgTfcw==";

    @XmlElement(name = "region")
    public List<Region> regions = new ArrayList<Region>();

    public void setAllParentRegions() {
        for (Region region : regions) {
            setParentRegions(region);
        }
    }

    private void setParentRegions(Region region) {
//        System.out.println("searching for parent regions for " + region.toString());
        Region parentReg = region;
        while((parentReg = getParentRegion(region)) != null){
            region.setParentRegion(parentReg);
            region = parentReg;
        }

    }

    private Region getParentRegion(Region region) {
        int parentId = region.getParentId();
        if(parentId > 0){
            for (Region reg : regions) {
                if (reg.getId() == parentId) {
                    return reg;
                }
            }
        }
        return null;
    }

    public void save(String fileName) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Regions.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.marshal(this, new File(fileName));

    }

    @Override
    public Class getType() {
        return Regions.class;
    }

    public void locateAll() throws UnsupportedEncodingException {
        List<Region> actualRegions;
        while ((actualRegions = getRegionsWithoutCoords()).size() > 0){
            int i = 0;
            int l = actualRegions.size();
            for (Region region:actualRegions){
                String urlStr = "http://geocode-maps.yandex.ru/1.x/?format=xml&geocode=" + URLEncoder.encode(region.toString(), "UTF-8") + "&key=" + key;
                YMaps yMaps = null;
                try {
                    yMaps = new PageFetcher<YMaps>(urlStr, new YMaps()).call();
                    region.setLongitude(yMaps.getLongitude());
                    region.setLatitude(yMaps.getLatitude());

                    i++;
                    System.out.println("Processed " + i + " out of " + l);

                } catch (JAXBException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (IOException e) {
                    try {
                        System.out.println("Got exception 500, waiting...");
                        Thread.sleep(1000 * 30);

                    } catch (InterruptedException e1) {
                        e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }

                } catch (SAXException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        }
    }

    private List<Region> getRegionsWithoutCoords(){
        List<Region> regionsWithoutCoordsList = new ArrayList<Region>();
        for (Region region: regions){
            if (region.getLatitude() == null || region.getLongitude() == null){
                regionsWithoutCoordsList.add(region);
            }
        }
        return regionsWithoutCoordsList;
    }

    public void compact() {
        List<Region> deleteList = new ArrayList<Region>();
        for (Region region: regions){
            if (region.getName().length() == 0){
                deleteList.add(region);
            }
        }
        for (Region region: deleteList){
            regions.remove(region);
        }
    }
}
