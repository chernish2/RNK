package ru.workmap.bean;

import org.apache.log4j.Logger;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import ru.workmap.HHSearcher;
import ru.workmap.HeadHunter.HHSearchResult;
import ru.workmap.HeadHunter.Vacancy;
import ru.workmap.cache.CacheStat;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: workmap
 * Date: 15.10.11
 * Time: 22:51
 * To change this template use File | Settings | File Templates.
 */
public class MyBean {
    private static final String KEY = "Searcher";
    private static final Logger log = Logger.getLogger(MyBean.class);
    private static final Object monitor = new Object();

    public MyBean() {
    }

    public HHSearchResult getVacancies(ServletContext context) {
        HHSearchResult hhSearchResult = new HHSearchResult();
        try {
            hhSearchResult = getSearcher(context).getVacancies();
        } catch (Exception e) {
            log.error(e);
        }
        return hhSearchResult;
    }

    public CacheStat getStat(){
        return HHSearcher.getStat();
    }

    public void setMapCoords(double centerX, double centerY, double boundX, double boundY, ServletContext context) {
        getSearcher(context).setMapCoords(centerX, centerY, boundX, boundY);
    }

    public void setText(String text, ServletContext context) {
        getSearcher(context).setText(text.toLowerCase());
    }

    public void setStrictSearch(boolean strictSearch){
        getSearcher().setStrictSearch(strictSearch);
    }

    public List<String> getSuggestions(String input){
        return HHSearcher.getSuggestions(input);
    }


    private synchronized HHSearcher getSearcher() {
        synchronized (monitor) {
            log.debug("Entering getSearcher(), " + this);
            WebContext context = WebContextFactory.get();
            HttpSession session = context.getSession(true);
            Object o = session.getAttribute(KEY);
            HHSearcher searcher;
            if (o == null) {
//            log.debug("creating HHSearcher");
                searcher = new HHSearcher();
//            log.debug("created " + searcher);
                session.setAttribute(KEY, searcher);
            } else {
                searcher = (HHSearcher) o;
            }
            log.debug("Finishing getSearcher(), Searcher " + searcher + " for Session " + session + " from MyBean " + this);
            return searcher;
        }
    }

    private HHSearcher getSearcher(ServletContext context) {
//        synchronized (monitor) {
//            log.debug("Entering getSearcher(), " + this);
//            Object o = context.getAttribute(KEY);
//            HHSearcher searcher;
//            if (o == null) {
////            log.debug("creating HHSearcher");
//                searcher = new HHSearcher();
////            log.debug("created " + searcher);
//                context.setAttribute(KEY, searcher);
//            } else {
//                searcher = (HHSearcher) o;
//            }
//            log.debug("Finishing getSearcher(), Searcher " + searcher + " for Context " + context + " from MyBean " + this);
//            return searcher;
//        }
        return getSearcher();
    }


}
