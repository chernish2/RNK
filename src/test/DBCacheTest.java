package test;

import org.junit.Test;
import ru.workmap.HeadHunter.Address;
import ru.workmap.HeadHunter.Salary;
import ru.workmap.HeadHunter.Vacancy;
import ru.workmap.cache.DBCache;
import ru.workmap.cache.QueryEntity;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


/**
 * Created by IntelliJ IDEA.
 * User: chernish2
 * Date: 06.02.12
 * Time: 10:40
 * To change this template use File | Settings | File Templates.
 */
public class DBCacheTest {
    private EntityManager entityManager;
    private DBCache cache = DBCache.getInstance();
    String s1 = "Все подряд";
    String s2 = "Все в порядке";
    private int vacancyId;

    public DBCacheTest() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("persistenceUnit");
        entityManager = factory.createEntityManager();
    }

    @Test
    public void putTest() {
        cache.clear();
        cache.put(s1, makeVacancyList());
        cache.put(s2, makeVacancyList());

    }

    @Test
    public void getTest() {
        String q = "http://api.hh.ru/1/xml/vacancy/search?text=%D1%81%D1%82%D0%B8%D0%BB%D0%B8%D1%81%D1%82&items=500";
        if (cache.contains(q)) {
            List<Vacancy> vacancyList = cache.get(q);
            assertTrue(vacancyList.size() > 0);
        }
    }

    @Test
    public void containsTest() {
        assertTrue(cache.contains("Стилист"));
        assertFalse(cache.contains("java"));
    }

    @Test
    public void clearTest() {
        cache.clear();
    }

    @Test
    public void deleteTest(){
        putTest();
        entityManager.getTransaction().begin();
        Query query = entityManager.createNamedQuery("getQueryEntityByQueryStr");
        query.setParameter("queryStr", s2);
        Object o = query.getResultList();
        QueryEntity queryEntity = (QueryEntity) query.getResultList().get(0);
        entityManager.remove(queryEntity);
        entityManager.getTransaction().commit();
    }

    @Test
    public void updateTest(){
        DBCache.getInstance().update();
    }

    @Test
    public void persistVacanciesTest() {
        cache.clear();
        entityManager.getTransaction().begin();
        for (Vacancy v : makeVacancyList()) {
            entityManager.persist(v);
        }
        entityManager.getTransaction().commit();
    }

    private List<Vacancy> makeVacancyList() {
        List<Vacancy> vacancyList = new ArrayList<Vacancy>();
        vacancyList.add(makeVacancy("Монтёр", "Москва", "Ленинский проспект", 100, 120));
        vacancyList.add(makeVacancy("Строитель", "Питер", "Невский проспект", 200, 220));
        vacancyList.add(makeVacancy("Проститутка", "Нальчик", "Фурманова", 40, 42));
        return vacancyList;
    }

    private Vacancy makeVacancy(String name, String city, String street, int from, int to) {
        return makeVacancy(++vacancyId, name, city, street, from, to);
    }


    private Vacancy makeVacancy(int id, String name, String city, String street, int from, int to) {
        Vacancy vacancy = new Vacancy();
        Salary salary = new Salary();
        salary.setFrom(from);
        salary.setTo(to);
        salary.setCurrency("$");
        Address address = new Address();
        address.setCity(city);
        address.setStreet(street);
        vacancy.setId(id);
        vacancy.setName(name);
        vacancy.setSalary(salary);
        vacancy.setAddress(address);
//        vacancy.setId(++vacancyId);
        return vacancy;
    }

}
