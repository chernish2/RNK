package ru.workmap;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;
import ru.workmap.HeadHunter.Regions;
import ru.workmap.HeadHunter.Result;
import ru.workmap.HeadHunter.Vacancy;
import ru.workmap.cache.DBCache;
import ru.workmap.cache.ICache;
import ru.workmap.util.PageFetcher;
import ru.workmap.util.Statistics;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
    private double x0, y0, boundX, boundY; // latitude широта - y; longitude долгота - x
    private static final Logger log = Logger.getLogger(HHSearcher.class);
//    private static ICache cacheManager = new RamCache();
    private static ICache cacheManager = DBCache.getInstance();
    private static final int ITEMS_PER_PAGE = 500;
    private static final int MAX_VACANCIES = 100;
    private static final int MAX_VACANCIE_DESCRIPTIONS_IN_ONE_PLACE = 7;
    private static final double RADIUS = 10; // percents!
    private static Regions regions;

    public HHSearcher() {
        Statistics.hit();
    }

    public List<Vacancy> getVacancies() throws JAXBException, IOException, SAXException, ExecutionException, InterruptedException {
        Statistics.search();
        String key = "http://api.hh.ru/1/xml/vacancy/search?text=" + URLEncoder.encode(text, "UTF-8") + "&items=" + ITEMS_PER_PAGE;
        List<Vacancy> unfilteredVacancies;
        if (cacheManager.contains(key)) {
            unfilteredVacancies = cacheManager.get(key);
        }else{
            unfilteredVacancies = new ArrayList<Vacancy>();
            for (Vacancy vacancy : fetchVacancies(key)) {
                if (vacancy.getAddress().hasCoordinates()) {
                    unfilteredVacancies.add(vacancy);
                }
            }
            cacheManager.put(key, unfilteredVacancies);
        }
        return compressVacancies(filterVacancies(unfilteredVacancies));
    }

    private List<Vacancy> filterVacancies(List<Vacancy> unfilteredVacancies){
        List<Vacancy> filteredVacancies = new ArrayList<Vacancy>();
        for(Vacancy vacancy: unfilteredVacancies){
            if(isVacancyOnMap(vacancy)){
                filteredVacancies.add(vacancy);
            }
        }
        log.debug(filteredVacancies.size() + " vacancies filtered out of " + unfilteredVacancies.size());
        return filteredVacancies;
    }

    private boolean isVacancyOnMap(Vacancy vacancy){
        double latitude = vacancy.getAddress().getLatitude();
        double longitude = vacancy.getAddress().getLongitude();
        if(longitude > x0 - boundX / 2 &&
                longitude < x0 + boundX / 2 &&
                    latitude < y0 + boundY / 2 &&
                        latitude > y0 - boundY / 2){
            return true;
        }else{
            return false;
        }
    }
    
    private List<Vacancy> compressVacancies(List<Vacancy> vacancies){
        List<Vacancy> compressedVacancies = new ArrayList<Vacancy>();
        List<Vacancy> skipList = new ArrayList<Vacancy>();
        for(Vacancy vacancy:vacancies){
            if(!skipList.contains(vacancy)){
                List<Vacancy> neighboringVacancies = getNeighboringVacancies(vacancy, vacancies);
                neighboringVacancies.removeAll(skipList);
                if(!neighboringVacancies.isEmpty()){
                    skipList.addAll(neighboringVacancies);
                    neighboringVacancies.add(vacancy);
                    Vacancy compositeVacancy = makeCompositeVacancy(neighboringVacancies);
                    compressedVacancies.add(compositeVacancy);
                }else{
                    compressedVacancies.add(vacancy);
                }
            }
        }
        log.debug(compressedVacancies.size() + " vacancies after compression");
        return compressedVacancies;
    }

    private Vacancy makeCompositeVacancy(List<Vacancy> vacancies) {
        double latitude = 0, longitude = 0;
        StringBuilder urlBuilder = new StringBuilder();
        Vacancy compositeVacancy = new Vacancy();
        for(Vacancy v: vacancies){
            latitude += v.getAddress().getLatitude();
            longitude += v.getAddress().getLongitude();
            urlBuilder.append(v.getNameUrl());
        }
        latitude = latitude / vacancies.size();
        longitude = longitude / vacancies.size();
        compositeVacancy.getAddress().setLatitude(latitude);
        compositeVacancy.getAddress().setLongitude(longitude);
        if(vacancies.size() <= MAX_VACANCIE_DESCRIPTIONS_IN_ONE_PLACE){
            compositeVacancy.setName (urlBuilder.toString());
        }else{
            compositeVacancy.setName(makeVacanciesString(vacancies.size()));
            compositeVacancy.getAddress().setStreet("увеличьте масштаб,<br>чтобы увидеть<br>подробнее");
        }
        return compositeVacancy;
    }

    private List<Vacancy> getNeighboringVacancies(Vacancy vacancy, List<Vacancy> allVacancies){
        double r = boundY / 100 * RADIUS;
        List<Vacancy> neighboringVacancies = new ArrayList<Vacancy>();
        for(Vacancy v: allVacancies){
            if(v != vacancy){
                if(getDistance(v, vacancy) <= r){
                    neighboringVacancies.add(v);
                }
            }
        }
        return neighboringVacancies;
    }

    private double getDistance(Vacancy v1, Vacancy v2){
        double a = v1.getAddress().getLatitude() - v2.getAddress().getLatitude();
        double b = v1.getAddress().getLongitude() - v2.getAddress().getLongitude();
        return Math.sqrt(a * a + b * b);
    }

    private List<Vacancy> fetchVacancies(String urlStr) throws JAXBException, IOException, SAXException, ExecutionException, InterruptedException {
        Result result = new PageFetcher<Result>(urlStr, new Result()).call();
        List<Vacancy> vacancyList = result.vacancies.vacancyList;
        if (result.found > ITEMS_PER_PAGE) {
            int pages = result.found / ITEMS_PER_PAGE;
            int reminder = result.found % ITEMS_PER_PAGE;
            if (reminder > 0){
                pages++;
            }
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
        log.debug("setMapCoords: " + centerX + ":" + centerY + ", " + boundX + ":" + boundY);
    }

    public void setText(String text) {
        this.text = text;
        log.debug("setText:" + text);
    }

    private String makeVacanciesString(int size){
        String vacanciesString = "нонсенс";
        if(size > 4 && size < 21){
            vacanciesString = size + " вакансий";
        }else {
            int lastDigit = size % 10;
            if(lastDigit == 1){
                vacanciesString = size + " вакансия";
            }else if(lastDigit < 5 && lastDigit > 0){
                vacanciesString = size + " вакансии";
            }else{
                vacanciesString = size + " вакансий";
            }
        }
        return vacanciesString;
    }

}
