package dora.db.dao;

import dora.db.OrmTable;

import java.util.HashMap;
import java.util.Map;

public class DaoFactory {

    private static Map<Class<? extends OrmTable>, OrmDao> sDaoMap = new HashMap<>();
    private static Object sLock1 = new Object();
    private static Object sLock2 = new Object();
    private static Object sLock3 = new Object();

    public static <T extends OrmTable> void removeDao(Class<T> beanClass) {
        synchronized (DaoFactory.class) {
            if (sDaoMap.containsKey(beanClass)) {
                sDaoMap.remove(beanClass);
            }
        }
    }

    public static <T extends OrmTable> OrmDao<T> getDao(Class<T> beanClass) {
        synchronized (sLock1) {
            if (sDaoMap.containsKey(beanClass)) {
                return sDaoMap.get(beanClass);
            } else {
                OrmDao<T> dao = new OrmDao<>(beanClass);
                sDaoMap.put(beanClass, dao);
                return dao;
            }
        }
    }

    public static <T extends OrmTable> OrmDao<T> getDao(String className) {
        synchronized (sLock2) {
            try {
                return getDao((Class<T>) Class.forName(className));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static <T extends OrmTable> OrmDao<T> getDao(T bean) {
        synchronized (sLock3) {
            return (OrmDao<T>) getDao(bean.getClass());
        }
    }
}
