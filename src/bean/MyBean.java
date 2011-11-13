package bean;

import org.apache.log4j.Logger;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.xml.sax.SAXException;
import ru.workmap.HHSearcher;
import ru.workmap.HeadHunter.HHResult;
import ru.workmap.HeadHunter.Vacancy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

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

    public HHResult getVacancies() throws JAXBException, IOException, SAXException, ExecutionException, InterruptedException {
        return getSearcher().getVacancies();
    }

    public void setMapCoords(double centerX, double centerY, double boundX, double boundY) {
        getSearcher().setMapCoords(centerX, centerY, boundX, boundY);
    }

    public void setText(String text) {
        getSearcher().setText(text);
    }


    private HHSearcher getSearcher(){
        WebContext context = WebContextFactory.get();
        HttpServletRequest request = context.getHttpServletRequest();
        HttpSession session = request.getSession(true);
        Object o = session.getAttribute(KEY);
        HHSearcher searcher;
        if (o == null){
            searcher = new HHSearcher();
            session.setAttribute(KEY, searcher);
        }else{
            searcher = (HHSearcher)o;
        }
        log.debug("Searcher=" + searcher + "; Session=" + session);
        return searcher;
    }
}
