package ru.workmap.util;

import org.apache.log4j.Logger;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;
import ru.workmap.HeadHunter.IHH;
import ru.workmap.NamespaceFilter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.sax.SAXSource;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.Callable;

/**
 * Created by IntelliJ IDEA.
 * User: chernish2
 * Date: 30.10.11
 * Time: 18:46
 * To change this template use File | Settings | File Templates.
 */
public class PageFetcher<T extends IHH> implements Callable<T> {
    private String urlStr;
    private Unmarshaller unmarshaller;
    private Logger logger = Logger.getLogger(PageFetcher.class);
    private IHH t;

    public PageFetcher(String urlStr, IHH t) {
        this.urlStr = urlStr;
        this.t = t;
    }

    @Override
    public T call() {
        logger.debug("Fetching page " + urlStr);
        T result = null;
        int count = 0;
        while(result == null && count < 2){
            try {
                SAXSource source = createSource(urlStr);
                JAXBContext context = JAXBContext.newInstance(t.getType());
                unmarshaller = context.createUnmarshaller();
                result = (T) unmarshaller.unmarshal(source);
            } catch (Exception e) {
                count++;
                try {
                    Thread.sleep(5000);
                    logger.debug("connection error, retrying " + urlStr);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        }
        if(result == null){
            logger.error("error fetching " + urlStr);
        }
        return result;
    }

    private SAXSource createSource(String urlStr) throws IOException, JAXBException, SAXException {
        URL url = new URL(urlStr);
        NamespaceFilter filter = new NamespaceFilter(null, false);
        XMLReader reader = XMLReaderFactory.createXMLReader();
        filter.setParent(reader);
        InputSource is = new InputSource(url.openStream());
        SAXSource source = new SAXSource(filter, is);
        return source;
    }

    public void setUrlStr(String urlStr) {
        this.urlStr = urlStr;
    }

//    public Class getEntityClass() {
//        return getGenericParameterClass(this.getClass(), 0);
//    }
//
//
//    public static Class getGenericParameterClass(Class actualClass, int parameterIndex) {
//        return (Class) ((ParameterizedType) actualClass.getGenericSuperclass()).getActualTypeArguments()[parameterIndex];
//    }
}
