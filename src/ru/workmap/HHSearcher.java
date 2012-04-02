package ru.workmap;

import org.apache.log4j.Logger;
import ru.workmap.HeadHunter.HHSearchResult;
import ru.workmap.HeadHunter.Result;
import ru.workmap.HeadHunter.Vacancy;
import ru.workmap.cache.CacheStat;
import ru.workmap.cache.DBCache;
import ru.workmap.cache.ICache;
import ru.workmap.cache.RamCache;
import ru.workmap.util.PageFetcher;
import ru.workmap.util.Settings;
import ru.workmap.util.Statistics;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.*;

/**
 * Created by IntelliJ IDEA.
 * User: workmap
 * Date: 15.10.11
 * Time: 19:37
 * To change this template use File | Settings | File Templates.
 */
public class HHSearcher implements Serializable{
    private String text;
    private double x0, y0, boundX, boundY; // latitude широта - y; longitude долгота - x
    private boolean strictSearch;
    private static final Logger log = Logger.getLogger(HHSearcher.class);
        private static ICache cacheManager = new RamCache();
//    private static ICache cacheManager = DBCache.getInstance();
//    private static ICache cacheManager = new NullCache();
    private static final int ITEMS_PER_PAGE = 500;
    //    private static final int MAX_VACANCIES = 100;
    private static final int MAX_VACANCIE_DESCRIPTIONS_IN_ONE_PLACE = 7;
    private static final double RADIUS = 10; // percents!
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(Settings.getProperty(Settings.LOCALIZATION_BUNDLE));
    private static final String searchUrl = "http://api.hh.ru/1/xml/vacancy/search?";
//    private static Regions regions;

    public HHSearcher() {
        Statistics.hit();
        log.debug("Constructing new Searcher: " + this);
    }

    public static List<Vacancy> getVacancyList(String urlStr) {
        List<Vacancy> vacancyList = new ArrayList<Vacancy>();
        for (Vacancy vacancy : fetchVacancies(urlStr)) {
            if (vacancy.getAddress().hasCoordinates()) {
                vacancyList.add(vacancy);
            }
        }
        return vacancyList;
    }

    public HHSearchResult getVacancies() throws UnsupportedEncodingException {
        HHSearchResult searchResult = new HHSearchResult();
        Statistics.search();
        String key = makeUrlStr();
        List<Vacancy> unfilteredVacancies;
        if (cacheManager.contains(key)) {
            unfilteredVacancies = cacheManager.get(key);
        } else {
            unfilteredVacancies = getVacancyList(key);
            cacheManager.put(key, unfilteredVacancies);
        }
        searchResult.setFoundTotal(unfilteredVacancies.size());
        log.debug(unfilteredVacancies.size() + " unfiltered vacancies");
        List<Vacancy> filteredVacancies = filterVacancies(unfilteredVacancies);
        searchResult.setFoundForMap(filteredVacancies.size());
        searchResult.setVacancyList(compressVacancies(filteredVacancies));
        return searchResult;
    }

    public CacheStat getStat(){
        return cacheManager.getStat();
    }

    public void setMapCoords(double centerX, double centerY, double boundX, double boundY) {
        x0 = centerX;
        y0 = centerY;
        this.boundX = boundX;
        this.boundY = boundY;
        log.debug("setMapCoords: " + centerX + ":" + centerY + ", " + boundX + ":" + boundY + " for class=" + this);
    }

    public void setText(String text) {
        this.text = text;
        log.debug("setText:" + text + " for class=" + this);
    }

    public void setStrictSearch(boolean strictSearch) {
        this.strictSearch = strictSearch;
    }

    private String makeUrlStr() throws UnsupportedEncodingException {
        String urlStr = searchUrl;
        urlStr += "text=" + URLEncoder.encode(text, "UTF-8") + "&items=" + ITEMS_PER_PAGE;
        if(strictSearch){
            urlStr += "&vacancyNameField=true";
        }
        return urlStr;
    }

    private List<Vacancy> filterVacancies(List<Vacancy> unfilteredVacancies) {
        List<Vacancy> filteredVacancies = new ArrayList<Vacancy>();
        for (Vacancy vacancy : unfilteredVacancies) {
            if (isVacancyOnMap(vacancy)) {
                filteredVacancies.add(vacancy);
            }
        }
        log.debug(filteredVacancies.size() + " vacancies filtered out of " + unfilteredVacancies.size());
        return filteredVacancies;
    }

    private boolean isVacancyOnMap(Vacancy vacancy) {
        double latitude = vacancy.getAddress().getLatitude();
        double longitude = vacancy.getAddress().getLongitude();
        if (longitude > x0 - boundX / 2 &&
                longitude < x0 + boundX / 2 &&
                latitude < y0 + boundY / 2 &&
                latitude > y0 - boundY / 2) {
            return true;
        } else {
            return false;
        }
    }

    private List<Vacancy> compressVacancies(List<Vacancy> vacancies) {
        List<Vacancy> compressedVacancies = new ArrayList<Vacancy>();
        List<Vacancy> skipList = new ArrayList<Vacancy>();
        for (Vacancy vacancy : vacancies) {
            if (!skipList.contains(vacancy)) {
                List<Vacancy> neighboringVacancies = getNeighboringVacancies(vacancy, vacancies);
                neighboringVacancies.removeAll(skipList);
                if (!neighboringVacancies.isEmpty()) {
                    skipList.addAll(neighboringVacancies);
                    neighboringVacancies.add(vacancy);
                    Vacancy compositeVacancy = makeCompositeVacancy(neighboringVacancies);
                    compressedVacancies.add(compositeVacancy);
                } else {
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
        for (Vacancy v : vacancies) {
            latitude += v.getAddress().getLatitude();
            longitude += v.getAddress().getLongitude();
            urlBuilder.append(v.getNameUrl());
        }
        latitude = latitude / vacancies.size();
        longitude = longitude / vacancies.size();
        compositeVacancy.getAddress().setLatitude(latitude);
        compositeVacancy.getAddress().setLongitude(longitude);
        if (vacancies.size() <= MAX_VACANCIE_DESCRIPTIONS_IN_ONE_PLACE) {
            compositeVacancy.setName(urlBuilder.toString());
        } else {
            compositeVacancy.setName(makeVacanciesString(vacancies.size()));
            compositeVacancy.getAddress().setStreet(RESOURCE_BUNDLE.getString("descr"));
        }
        return compositeVacancy;
    }

    private List<Vacancy> getNeighboringVacancies(Vacancy vacancy, List<Vacancy> allVacancies) {
        double r = boundY / 100 * RADIUS;
        List<Vacancy> neighboringVacancies = new ArrayList<Vacancy>();
        for (Vacancy v : allVacancies) {
            if (v != vacancy) {
                if (getDistance(v, vacancy) <= r) {
                    neighboringVacancies.add(v);
                }
            }
        }
        return neighboringVacancies;
    }

    private double getDistance(Vacancy v1, Vacancy v2) {
        double a = v1.getAddress().getLatitude() - v2.getAddress().getLatitude();
        double b = v1.getAddress().getLongitude() - v2.getAddress().getLongitude();
        return Math.sqrt(a * a + b * b);
    }

    private static List<Vacancy> fetchVacancies(String urlStr) {
        Result result = new PageFetcher<Result>(urlStr, new Result()).call();
        List<Vacancy> vacancyList = result.vacancies.vacancyList;
        if (result.found > ITEMS_PER_PAGE) {
            int pages = result.found / ITEMS_PER_PAGE;
            int maxThreadPools = Settings.getPropertyAsInt(Settings.MAX_THREAD_POOL_NUMBER);
            int threadNumber = pages > maxThreadPools ? maxThreadPools : pages;
            ExecutorService pool = Executors.newFixedThreadPool(threadNumber);
            Set<Future<Result>> futureSet = new HashSet<Future<Result>>();
            for (int i = 1; i < pages + 1; i++) {
                String pageUrlStr = urlStr + "&page=" + i;
                Callable<Result> callable = new PageFetcher(pageUrlStr, new Result());
                Future<Result> future = pool.submit(callable);
                futureSet.add(future);
            }
            for (Future<Result> future : futureSet) {
                try {
                    result = future.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (ExecutionException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                if (result != null) {
                    vacancyList.addAll(result.vacancies.vacancyList);
                }
            }
        }
        log.debug("got " + vacancyList.size() + " vacancies totally from " + urlStr);
        return vacancyList;
    }


    private String makeVacanciesString(int size) {
        StringBuilder vacanciesString = new StringBuilder(size + " ");
        if (size > 4 && size < 21) {
            vacanciesString.append(RESOURCE_BUNDLE.getString("vacancies2"));
        } else {
            int lastDigit = size % 10;
            if (lastDigit == 1) {
                vacanciesString.append(RESOURCE_BUNDLE.getString("vacancy"));
            } else if (lastDigit < 5 && lastDigit > 0) {
                vacanciesString.append(RESOURCE_BUNDLE.getString("vacancies1"));
            } else {
                vacanciesString.append(RESOURCE_BUNDLE.getString("vacancies2"));
            }
        }
        return vacanciesString.toString();
    }

}
