package dora.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 反射工具，方便使用Java的反射。
 */
public final class ReflectionUtils {

    private ReflectionUtils() {
    }

    // <editor-folder desc="Java类、方法、属性的基本操作">

    /**
     * 获取一个类的字节码。
     *
     * @param className 带包类名
     */
    public static Class<?> findClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 是否能找到某个类的字节码。
     *
     * @param className 带包类名
     */
    public static boolean hasClass(String className) {
        boolean hasClass;
        try {
            Class.forName(className);
            hasClass = true;
        } catch (ClassNotFoundException e) {
            hasClass = false;
        }
        return hasClass;
    }

    /**
     * 创建一个类的实例。
     *
     * @param className 带包类名
     */
    public static Object newInstance(String className) {
        Class<?> clazz = findClass(className);
        if (clazz != null) {
            return newInstance(clazz);
        }
        return null;
    }

    /**
     * 创建一个类的实例。
     *
     * @param clazz 类的class对象
     * @param <T> 要创建类的类型
     */
    public static <T> T newInstance(Class<T> clazz) {
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        for (Constructor<?> c : constructors) {
            c.setAccessible(true);
            Class<?>[] cls = c.getParameterTypes();
            if (cls.length == 0) {
                try {
                    return (T) c.newInstance();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            } else {
                Object[] objs = new Object[cls.length];
                for (int i = 0; i < cls.length; i++) {
                    objs[i] = getPrimitiveDefaultValue(cls[i]);
                }
                try {
                    return (T) c.newInstance(objs);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 获取某个类的方法。
     *
     * @param clazz 类的class对象
     * @param isDeclared 是否是该类自身定义的，如果是则传true。如果是继承自基类的，则传false
     * @param methodName 方法名称
     * @param parameterTypes 方法的参数列表的类型数组
     */
    public static Method findMethod(Class<?> clazz, boolean isDeclared, String methodName, Class<?>... parameterTypes) {
        try {
            if (isDeclared) {
                return clazz.getDeclaredMethod(methodName, parameterTypes);
            } else {
                return clazz.getMethod(methodName, parameterTypes);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 调用方法。
     *
     * @param obj 该方法属于的对象
     * @param method 方法
     * @param objects 方法的传参的所有值
     */
    public static Object invokeMethod(Object obj, Method method, Object... objects) {
        method.setAccessible(true);
        try {
            return method.invoke(obj, objects);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取某个类的属性。
     *
     * @param clazz 类的class对象
     * @param isDeclared 是否是该类自身定义的，如果是则传true。如果是继承自基类的，则传false
     * @param fieldName 属性名称
     */
    public static Field findField(Class<?> clazz, boolean isDeclared, String fieldName) {
        try {
            if (isDeclared) {
                return clazz.getDeclaredField(fieldName);
            } else {
                return clazz.getField(fieldName);
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取非静态属性的值。
     *
     * @param field 属性
     * @param obj 属性所在的对象
     */
    public static Object getFieldValue(Field field, Object obj) {
        field.setAccessible(true);
        try {
            return field.get(obj);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 修改非静态属性的值。
     *
     * @param field 属性
     * @param obj 属性所在的对象
     * @param value 要修改成的值
     */
    public static void setFieldValue(Field field, Object obj, Object value) {
        field.setAccessible(true);
        try {
            field.set(obj, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取静态属性的值。
     *
     * @param field 静态属性
     */
    public static Object getStaticFieldValue(Field field) {
        field.setAccessible(true);
        if (isStatic(field)) {
            try {
                return field.get(null);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        throw new RuntimeException("Field is not static.");
    }

    /**
     * 修改静态属性的值。
     *
     * @param field 静态属性
     * @param value 要修改成的值
     */
    public static void setStaticFieldValue(Field field, Object value) {
        field.setAccessible(true);
        if (isStatic(field)) {
            try {
                field.set(null, value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        throw new RuntimeException("Field is not static.");
    }

    // </editor-folder>

    // <editor-folder desc="判断属性和方法是否有某关键字">

    /**
     * 检测一个属性是否是static属性。
     *
     * @param field 属性
     */
    public static boolean isStatic(Field field) {
        return Modifier.isStatic(field.getModifiers());
    }

    /**
     * 检测一个方法是否是static方法。
     *
     * @param method 方法
     */
    public static boolean isStatic(Method method) {
        return Modifier.isStatic(method.getModifiers());
    }

    /**
     * 检测一个属性是否是final属性。
     *
     * @param field 属性
     */
    public static boolean isFinal(Field field) {
        return Modifier.isFinal(field.getModifiers());
    }

    /**
     * 检测一个方法是否是final方法。
     *
     * @param method 方法
     */
    public static boolean isFinal(Method method) {
        return Modifier.isFinal(method.getModifiers());
    }

    /**
     * 检测一个方法是否是synchronized方法。
     *
     * @param method 方法
     */
    public static boolean isSynchronized(Method method) {
        return Modifier.isSynchronized(method.getModifiers());
    }

    /**
     * 检测一个属性是否是abstract方法。
     *
     * @param method 方法
     */
    public static boolean isAbstract(Method method) {
        return Modifier.isAbstract(method.getModifiers());
    }

    /**
     * 检测一个属性是否是native属性。
     *
     * @param field 属性
     */
    public static boolean isNative(Field field) {
        return Modifier.isNative(field.getModifiers());
    }

    /**
     * 检测一个属性是否是native方法。
     *
     * @param method 方法
     */
    public static boolean isNative(Method method) {
        return Modifier.isNative(method.getModifiers());
    }

    /**
     * 检测一个属性是否是volatile属性。
     *
     * @param field 属性
     */
    public static boolean isVolatile(Field field) {
        return Modifier.isVolatile(field.getModifiers());
    }

    /**
     * 检测一个属性是否是transient属性。
     *
     * @param field 属性
     */
    public static boolean isTransient(Field field) {
        return Modifier.isTransient(field.getModifiers());
    }

    // </editor-folder>

    // <editor-folder desc="获取泛型">

    /**
     * 获取一个对象的泛型类型，这个对象需要实现带泛型的接口或继承带泛型的类。在这个对象指定具体的泛型，可通过这个方法
     * 获取到。
     *
     * @param obj 这个对象的类必须实现带泛型的接口或继承带泛型的类
     * @return 这个对象指定给实现的接口或基类的泛型
     */
    public static Class<?> getGenericType(Object obj) {
        if (obj.getClass().getGenericSuperclass() instanceof ParameterizedType &&
                ((ParameterizedType) obj.getClass().getGenericSuperclass()).getActualTypeArguments().length > 0) {
            Class<?> tClass = (Class<?>) ((ParameterizedType) obj.getClass()
                    .getGenericSuperclass()).getActualTypeArguments()[0];
            return tClass;
        }
        return (Class<?>) ((ParameterizedType) obj.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    /**
     * 获取一个属性的泛型类型。
     *
     * @param field 带泛型的属性
     * @return 泛型的类型
     */
    public static Class<?> getGenericType(Field field) {
        Type type = field.getGenericType();
        if (type instanceof ParameterizedType) {
            type = ((ParameterizedType) type).getActualTypeArguments()[0];
            if (type instanceof Class<?>) {
                return (Class<?>) type;
            }
        } else if (type instanceof Class<?>) {
            return (Class<?>) type;
        }
        return null;
    }

    /**
     * 获取一个属性的嵌套泛型类型。
     *
     * @param field 带泛型的属性
     * @param genericTypeIndex 泛型的索引，0代表第一个泛型类型，如class A&lt;B,C&gt;中0代表泛型B，1代表泛型C
     * @return 泛型的类型
     */
    public static Class<?> getNestedGenericType(Field field, int genericTypeIndex) {
        Type type = field.getGenericType();
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            type = parameterizedType.getActualTypeArguments()[genericTypeIndex];
            return (Class<?>) type;
        }
        if (type instanceof Class<?>) {
            return (Class<?>) type;
        }
        return null;
    }

    // </editor-folder>

    /**
     * 检测一个类是否是数字类型。
     */
    public static boolean isNumber(Class<?> numberClazz) {
        return numberClazz == long.class
                || numberClazz == Long.class
                || numberClazz == int.class
                || numberClazz == Integer.class
                || numberClazz == short.class
                || numberClazz == Short.class
                || numberClazz == byte.class
                || numberClazz == Byte.class;
    }

    /**
     * 获取基本数据类型的默认值。
     */
    public static Object getPrimitiveDefaultValue(Class<?> primitiveClazz) {
        if (primitiveClazz.isPrimitive()) {
            return primitiveClazz == boolean.class ? false : 0;
        }
        return null;
    }
}
