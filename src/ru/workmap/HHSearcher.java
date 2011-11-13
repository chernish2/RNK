package ru.workmap;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;
import ru.workmap.HeadHunter.*;
import ru.workmap.cache.CacheManager;
import ru.workmap.util.PageFetcher;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by IntelliJ IDEA.
 * User: workmap
 * Date: 15.10.11
 * Time: 19:37
 * To change this template use File | Settings | File Templates.
 */
public class HHSearcher {
    private String text;
//    private double x1, y1, x2, y2;
    private double x0, y0, boundX, boundY;
    private static final Logger log = Logger.getLogger(HHSearcher.class);
    private CacheManager cacheManager = new CacheManager();
    private static final int ITEMS_PER_PAGE = 500;
    private static Regions regions;

    public HHSearcher() {
        log.setLevel(Level.ERROR);
        JAXBContext context = null;
        try {
            context = JAXBContext.newInstance(Regions.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            regions = (Regions) unmarshaller.unmarshal(new URL("http://workmap.ru/allregions.xml"));
//            regions = (Regions) unmarshaller.unmarshal(new URL("http://localhost:8080/allregions.xml"));
//            log.debug("Unmarshalled " + regions.regions.size() + " regions");
        } catch (JAXBException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (MalformedURLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public HHResult getVacancies() throws JAXBException, IOException, SAXException, ExecutionException, InterruptedException {
        String key = "http://api.hh.ru/1/xml/vacancy/search?text=" + URLEncoder.encode(text, "UTF-8") + "&items=" + ITEMS_PER_PAGE;
        List<Integer> neighboringRegions = getNeighboringRegions();
        if (neighboringRegions.size() == 0){
            return new HHResult();
        }
        for (Integer integer : neighboringRegions) {
            key += "&region=" + integer;
        }
        if (cacheManager.containsKey(key)) {
            return cacheManager.get(key);
        }

        HHResult hhResult = new HHResult();

        for (Vacancy vacancy : fetchVacancies(key)) {
            if (vacancy.getAddress().hasCoordinates()) {
                hhResult.getCoordinatesList().add(vacancy);
            }else if(vacancy.getAddress().hasCityAddress()){
                hhResult.getStreetList().add(vacancy);
            }else{
                hhResult.otherVacancies++;
            }
        }
        cacheManager.put(key, hhResult);
        return hhResult;
    }

    private List<Integer> getNeighboringRegions() {
        double radius = boundX;
        List<Integer> regIdList = new ArrayList<Integer>();
        for (Region region: regions.regions){
            double x = region.getLatitude();
            double y = region.getLongitude();
            if (x >= x0 - boundX / 2 && x <= x0 + boundX /2 && y >= y0 - boundY / 2 && y <= y0 + boundY / 2){
                regIdList.add(region.getId());
            }
//            if( ((x - x0) * (x - x0) + (y - y0) * (y - y0)) <= radius * radius ){
//                regIdList.add(region.getId());
//            }
        }
        log.debug("Found " + regIdList.size() + " regions:\n" + regIdList);
        if (regIdList.size() > 99){
            regIdList = regIdList.subList(0, 98);
        }
        return regIdList;
    }

    private List<Vacancy> fetchVacancies(String urlStr) throws JAXBException, IOException, SAXException, ExecutionException, InterruptedException {
        Result result = new PageFetcher<Result>(urlStr, new Result()).call();
        List<Vacancy> vacancyList = result.vacancies.vacancyList;
        if (result.found > ITEMS_PER_PAGE) {
            int pages = result.found / ITEMS_PER_PAGE;   //Fix!
            ExecutorService pool = Executors.newFixedThreadPool(pages);
            Set<Future<Result>> futureSet = new HashSet<Future<Result>>();
            for (int i = 0; i < pages; i++) {
                String pageUrlStr = urlStr + "&page=" + i;
                Callable<Result> callable = new PageFetcher(pageUrlStr, new Result());
                Future<Result> future = pool.submit(callable);
                futureSet.add(future);
            }
            for (Future<Result> future : futureSet) {
                result = future.get();
                vacancyList.addAll(result.vacancies.vacancyList);
            }
        }
        log.debug("got " + vacancyList.size() + " vacancies totally from " + urlStr);
        return vacancyList;
    }

    public void setMapCoords(double centerX, double centerY, double boundX, double boundY) {
        x0 = centerX;
        y0 = centerY;
        this.boundX = boundX;
        this.boundY = boundY;
//        x1 = centerX - boundX / 2;
//        y1 = centerY + boundY / 2;
//        x2 = centerX + boundX / 2;
//        y2 = centerY - boundY / 2;
        log.debug("setMapCoords: " + centerX + ":" + centerY + ", " + boundX + ":" + boundY);
    }

    public void setText(String text) {
        this.text = text;
        log.debug("setText:" + text);
    }

}
