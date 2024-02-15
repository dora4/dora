/*
 * Copyright (C) 2023 The Dora Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dora.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Reflection tool, facilitating the use of Java reflection.
 * 简体中文：反射工具，方便使用Java的反射。
 */
public final class ReflectionUtils {

    private ReflectionUtils() {
    }

    // <editor-folder desc="Basic operations for Java classes, methods, and properties.">

    /**
     * Retrieve the bytecode of a class.
     * 简体中文：获取一个类的字节码。
     *
     * @param className Fully qualified class name.
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
     * Whether the bytecode of a certain class can be located.
     * 简体中文：是否能找到某个类的字节码。
     *
     * @param className Fully qualified class name.
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
     * Instantiate a class.
     * 简体中文：创建一个类的实例。
     *
     * @param className Fully qualified class name.
     */
    public static Object newInstance(String className) {
        Class<?> clazz = findClass(className);
        if (clazz != null) {
            return newInstance(clazz);
        }
        return null;
    }

    /**
     * Create an instance of a class.
     * 简体中文：创建一个类的实例。
     *
     * @param clazz Class object of a class.
     * @param <T> To create the type of a class.
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
     * Retrieve methods of a certain class.
     * 简体中文：获取某个类的方法。
     *
     * @param clazz Class object of a class.
     * @param isDeclared Whether it is self-defined by the class, if so, pass true. If it is
     *                   inherited from a base class, pass false.
     * @param methodName Method name.
     * @param parameterTypes Array of parameter types in a method's parameter list.
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
     * Invoke a method.
     * 简体中文：调用方法。
     *
     * @param obj The object to which the method belongs.
     * @param method Method
     * @param objects All values passed as arguments to a method.
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
     * Retrieve properties/fields of a certain class.
     * 简体中文：获取某个类的属性。
     *
     * @param clazz Class object of a class.
     * @param isDeclared Whether it is self-defined by the class, if so, pass true. If it is
     *                   inherited from a base class, pass false.
     * @param fieldName Property/Field name.
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
     * Retrieve the value of a non-static property/field.
     * 简体中文：获取非静态属性的值。
     *
     * @param field Property/Field
     * @param obj The object containing the property/field.
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
     * Modify the value of a non-static property/field.
     * 简体中文：修改非静态属性的值。
     *
     * @param field Attribute/Property/Field
     * @param obj The object in which the attribute/property/field is located.
     * @param value The value to be modified to.
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
     * Retrieve the value of a static property/field.
     * 简体中文：获取静态属性的值。
     *
     * @param field Static property/field.
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
     * Modify the value of a static property/field.
     * 简体中文：修改静态属性的值。
     *
     * @param field Static property/field.
     * @param value The value to be modified to.
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

    // <editor-folder desc="Determine if an attribute or method has a certain keyword.">

    /**
     * Check if an attribute is a static property/field.
     * 简体中文：检测一个属性是否是static属性。
     *
     * @param field Attribute/Property/Field
     */
    public static boolean isStatic(Field field) {
        return Modifier.isStatic(field.getModifiers());
    }

    /**
     * Check if a method is a static method.
     * 简体中文：检测一个方法是否是static方法。
     *
     * @param method Method
     */
    public static boolean isStatic(Method method) {
        return Modifier.isStatic(method.getModifiers());
    }

    /**
     * Check if an attribute is a final property/field.
     * 简体中文：检测一个属性是否是final属性。
     *
     * @param field Attribute/Property/Field
     */
    public static boolean isFinal(Field field) {
        return Modifier.isFinal(field.getModifiers());
    }

    /**
     * Check if a method is a final method.
     * 简体中文：检测一个方法是否是final方法。
     *
     * @param method Method
     */
    public static boolean isFinal(Method method) {
        return Modifier.isFinal(method.getModifiers());
    }

    /**
     * Check if a method is a synchronized method.
     * 简体中文：检测一个方法是否是synchronized方法。
     *
     * @param method Method
     */
    public static boolean isSynchronized(Method method) {
        return Modifier.isSynchronized(method.getModifiers());
    }

    /**
     * Check if a method is an abstract method.
     * 简体中文：检测一个方法是否是abstract方法。
     *
     * @param method Method
     */
    public static boolean isAbstract(Method method) {
        return Modifier.isAbstract(method.getModifiers());
    }

    /**
     * Check if an attribute is a native property/field.
     * 简体中文：检测一个属性是否是native属性。
     *
     * @param field Attribute/Property/Field
     */
    public static boolean isNative(Field field) {
        return Modifier.isNative(field.getModifiers());
    }

    /**
     * Check if an attribute is a native method.
     * 简体中文：检测一个属性是否是native方法。
     *
     * @param method Method
     */
    public static boolean isNative(Method method) {
        return Modifier.isNative(method.getModifiers());
    }

    /**
     * Check if an attribute is a volatile property/field.
     * 简体中文：检测一个属性是否是volatile属性。
     *
     * @param field Attribute/Property/Field
     */
    public static boolean isVolatile(Field field) {
        return Modifier.isVolatile(field.getModifiers());
    }

    /**
     * Check if an attribute is a transient property/field.
     * 简体中文：检测一个属性是否是transient属性。
     *
     * @param field Attribute/Property/Field
     */
    public static boolean isTransient(Field field) {
        return Modifier.isTransient(field.getModifiers());
    }

    // </editor-folder>

    // <editor-folder desc="Retrieve generics.">

    /**
     * Retrieve the generic type of an object, where the object needs to implement an interface or
     * inherit from a class with generics. By specifying the concrete generic type for this object,
     * it can be obtained using this method.
     * 简体中文：获取一个对象的泛型类型，这个对象需要实现带泛型的接口或继承带泛型的类。在这个对象指定具体的泛型，
     * 可通过这个方法获取到。
     *
     * @param obj The class of this object must implement an interface with generics or inherit
     *            from a class with generics.
     * @return The generics specified for the implemented interface or base class of this object.
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
     * Retrieve the generic type of a property.
     * 简体中文：获取一个属性的泛型类型。
     *
     * @param field Property with generics.
     * @return Type of generics.
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
     * Retrieve the nested generic type of a property.
     * 简体中文：获取一个属性的嵌套泛型类型。
     *
     * @param field Property with nested generics.
     * @param genericTypeIndex Generic index, where 0 represents the first generic type. For example,
     *                        in the class A&lt;B,C&gt;, 0 represents the generic type B,
     *                         and 1 represents the generic type C.
     * @return Type of the generic.
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
     * Check if a class is a numeric type.
     * 简体中文：检测一个类是否是数字类型。
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
     * Retrieve the default value of a primitive data type.
     * 简体中文：获取基本数据类型的默认值。
     */
    public static Object getPrimitiveDefaultValue(Class<?> primitiveClazz) {
        if (primitiveClazz.isPrimitive()) {
            return primitiveClazz == boolean.class ? false : 0;
        }
        return null;
    }
}
