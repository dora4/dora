package dora.db;

import java.lang.reflect.Field;

public interface DataMatcher {

    Class<?>[] getTypes();

    boolean matches(Field field);
}
