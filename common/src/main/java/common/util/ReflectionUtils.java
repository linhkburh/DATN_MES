package common.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.NestedNullException;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.logging.log4j.LogManager;

import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

public class ReflectionUtils {
	public static final PropertyUtilsBean pub = new PropertyUtilsBean();

	/**
	 * Recursive method used to find all classes in a given directory and subdirs.
	 *
	 * @param directory   The base directory
	 * @param packageName The package name for classes found inside the base
	 *                    directory
	 * @return The classes
	 * @throws ClassNotFoundException
	 */
	private static List<Class<?>> findClasses(File directory, String packageName) throws ClassNotFoundException {
		List<Class<?>> classes = new ArrayList<Class<?>>();
		if (!directory.exists()) {
			return classes;
		}
		File[] files = directory.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				assert !file.getName().contains(".");
				classes.addAll(findClasses(file, packageName + "." + file.getName()));
			} else if (file.getName().endsWith(".class")) {
				classes.add(
						Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
			}
		}
		return classes;
	}

	public static Field getField(Class<?> cls, String fieldName) {
		while (!cls.equals(Object.class)) {
			try {
				return cls.getDeclaredField(fieldName);
			} catch (NoSuchFieldException e) {
				cls = cls.getSuperclass();
			}
		}
		return null;
	}

	public static Boolean isPrimitive(Class<?> c) {
		return isWrapperOrString(c) || c.isPrimitive();
	}

	private static boolean isWrapperOrString(Class<?> cls) {
		return cls.equals(String.class) || cls.equals(Boolean.class) || cls.equals(BigDecimal.class)
				|| cls.equals(Long.class) || cls.equals(Date.class) || cls.equals(Integer.class)
				|| cls.equals(Double.class) || cls.equals(Byte.class) || cls.equals(Short.class)
				|| cls.equals(Float.class) || cls.equals(Character.class);
	}

	public static Boolean isPrimitive(Class<?> c, String path) throws Exception {
		c = getClassByPath(c, path);
		return c.isPrimitive();
	}

	@SuppressWarnings("restriction")
	public static Class<?> getClassByPath(Class<?> c, String path) throws Exception {
		path = path.replaceAll("\\[\\d*\\]", "");
		String[] arrPath = path.split("[.]");
		for (int i = 0; i < arrPath.length; i++) {
			Field f = c.getDeclaredField(arrPath[i]);
			if (ParameterizedTypeImpl.class.equals(f.getGenericType().getClass())) {
				ParameterizedTypeImpl t1 = (ParameterizedTypeImpl) f.getGenericType();
				Type[] fieldType = t1.getActualTypeArguments();
				c = (Class<?>) fieldType[0];
			} else {
				c = f.getType();
			}
		}
		return c;
	}

	public static Class<?> getClassByAbsPath(Class<?> c, String absPath) throws Exception {
		String path = absPath.substring(absPath.indexOf(".") + 1);
		return getClassByPath(c, path);
	}

	public static List<Field> getAllCollectionFields(Class<?> clazz) {
		if (clazz == null)
			return Collections.emptyList();
		List<Field> result = new ArrayList<>(getAllCollectionFields(clazz.getSuperclass()));
		List<Field> filteredFields = Arrays.stream(clazz.getDeclaredFields())
				.filter(f -> f.getType().isAssignableFrom(Collection.class)).collect(Collectors.toList());
		result.addAll(filteredFields);
		return result;
	}

	public static void makeStructureByPath1(Object o, String path) throws Exception {
		if (Formater.isNull(path))
			return;
		path = path.replaceAll("\\[\\d*\\]", "");
		String[] arrPath = path.split("[.]");
		Class<?> c = o.getClass();
		for (int i = 0; i < arrPath.length; i++) {
			Field f = getField(c, arrPath[i]);
			if (ParameterizedTypeImpl.class.equals(f.getGenericType().getClass())) {
				ParameterizedTypeImpl t1 = (ParameterizedTypeImpl) f.getGenericType();
				Type[] fieldType = t1.getActualTypeArguments();
				c = (Class<?>) fieldType[0];
				Collection<Object> value = (Collection<Object>) f.get(o);
				if (value == null) {
					value = new ArrayList<Object>();
					f.set(o, value);
				}
				if (value.isEmpty())
					value.add(c.newInstance());
			} else {
				c = f.getType();
				Object value = f.get(o);
				if (value == null) {
					value = c.newInstance();
					f.set(o, value);
				}
			}

		}
	}

	@SuppressWarnings("unchecked")
	public static void makeStructureByPath(Object o, String path) throws Exception {
		try {
			Object objToMake;
			if (Formater.isNull(path))
				return;
			String fieldName = path.indexOf(".") > 0 ? path.substring(0, path.indexOf(".")) : path;
			Pattern p = Pattern.compile("((?s).*)\\[(\\d*)\\]$");
			Matcher m = p.matcher(fieldName);
			objToMake = null;
			if (m.matches()) {
				fieldName = m.group(1);
				int itemNum = Integer.parseInt(m.group(2));
				Field f = getField(o.getClass(), fieldName);
				if (!f.isAccessible())
					f.setAccessible(true);
				List<Object> lstValue = (List<Object>) f.get(o);
				if (lstValue == null)
					lstValue = new ArrayList<Object>();
				int iTotalCurrentItem = lstValue.size();
				for (int i = 0; i < itemNum - iTotalCurrentItem + 1; i++)
					lstValue.add(null);
				if (lstValue.get(itemNum) == null) {
					// Xac dinh kieu
					ParameterizedTypeImpl t1 = (ParameterizedTypeImpl) f.getGenericType();
					Type[] fieldType = t1.getActualTypeArguments();
					Class<?> c = (Class<?>) fieldType[0];
					objToMake = c.newInstance();
					lstValue.set(itemNum, objToMake);
				} else {
					objToMake = lstValue.get(itemNum);
				}
			} else {
				Field f = getField(o.getClass(), fieldName);
				// Truong hop nhap sai
				if (f == null || f.getType().isArray())
					return;
				if (!f.getType().isPrimitive() && !f.getType().equals(String.class)
						&& !f.getType().equals(Boolean.class)) {
					if (!f.isAccessible())
						f.setAccessible(true);
					Object value = f.get(o);
					if (value == null) {
						objToMake = f.getType().newInstance();
						f.set(o, objToMake);
					}
				}
			}
			if (objToMake != null)
				makeStructureByPath(objToMake, path.substring(path.indexOf(".") + 1));
		} catch (Exception e) {
			try {
				logger.error(String.format("Noi dung loi, %s :%s",
						new Object[] { o.getClass().getName(), JsonUtils.writeToString(o) }));
				logger.error(path);
			} catch (Exception e1) {
			}
			throw e;
		}

	}

	private static final org.apache.logging.log4j.Logger logger = LogManager.getRootLogger();

	public static void setValueByPath(Object obj, String path, Object value) throws Exception {
		try {
			pub.setProperty(obj, path, value);
		} catch (IndexOutOfBoundsException | NestedNullException e) {
			makeStructureByPath(obj, path);
			pub.setProperty(obj, path, value);
		}

	}

	public static Object getValueByPath(Object obj, String path) throws Exception {
		return pub.getProperty(obj, path);
	}

	public static Method getAccMethod(Class<?> cls, Field f, Boolean isGetter) throws Exception {
		BeanInfo beanInf = Introspector.getBeanInfo(cls);
		PropertyDescriptor[] props = beanInf.getPropertyDescriptors();
		for (int i = 0; i < props.length; i++) {
			if (props[i].getName().equals(f.getName()))
				return isGetter ? props[i].getReadMethod() : props[i].getWriteMethod();
		}
		return null;
	}

	public static Method getSeter(Class<?> cls, Field f) throws Exception {
		return getAccMethod(cls, f, Boolean.FALSE);
	}

	public static Method getGeter(Class<?> cls, Field f) throws Exception {
		return getAccMethod(cls, f, Boolean.TRUE);
	}

	public static Method getAccMethod(Class<?> cls, String fielName, Boolean isGetter) throws Exception {
		BeanInfo beanInf = Introspector.getBeanInfo(cls);
		PropertyDescriptor[] props = beanInf.getPropertyDescriptors();
		for (int i = 0; i < props.length; i++) {
			if (props[i].getName().equals(fielName))
				return isGetter ? props[i].getReadMethod() : props[i].getWriteMethod();
		}
		return null;
	}

	public static Method getSeter(Class<?> cls, String fielName) throws Exception {
		return getAccMethod(cls, fielName, Boolean.FALSE);
	}

	public static Method getGeter(Class<?> cls, String fielName) throws Exception {
		return getAccMethod(cls, fielName, Boolean.TRUE);
	}

	public static boolean isPrimitive(Object pojoObject) {
		return pojoObject.getClass().isPrimitive() || pojoObject.getClass().equals(String.class)
				|| pojoObject.getClass().equals(Boolean.class);
	}

	public static boolean isPrimitiveClass(Class<?> cls) {
		return cls.isPrimitive() || cls.equals(String.class) || cls.equals(Boolean.class);
	}

	public static boolean isPrimitiveArr(Object pojoObject) {
		if (!pojoObject.getClass().isArray())
			return false;
		Object[] os = (Object[]) pojoObject;
		Class<?> componentType = os.getClass().getComponentType();
		return isPrimitiveClass(componentType);
	}

	public static boolean isPrimitiveArr(Class<?> cls) {
		if (!cls.isArray())
			return false;
		Class<?> componentType = cls.getComponentType();
		return isPrimitive(componentType);
	}

	public static Method getSetterMethod(Class<?> cls, Field fld) throws IntrospectionException {
		BeanInfo beanInf = Introspector.getBeanInfo(cls);
		PropertyDescriptor[] props = beanInf.getPropertyDescriptors();
		for (PropertyDescriptor prop : props) {
			if (prop.getName().equals(fld.getName()))
				return prop.getWriteMethod();
		}
		return null;
	}

	public static Method getGetterMethod(Class<?> cls, Field fld) throws IntrospectionException {
		BeanInfo beanInf = Introspector.getBeanInfo(cls);
		PropertyDescriptor[] props = beanInf.getPropertyDescriptors();
		for (PropertyDescriptor prop : props) {
			if (prop.getName().equals(fld.getName()))
				return prop.getReadMethod();
		}
		return null;
	}

	/**
	 * Dong bo object
	 * 
	 * @author nguye
	 *
	 * @param <T>
	 */
	public interface ReconcileObj<T> {
		/**
		 * Ham dong bo mac dinh, chi dong bo cac thuoc tinh primitive, khong dong bo id
		 * 
		 * @param desObj
		 * @param sourceObj
		 * @param ignoreList
		 * @throws Exception
		 */
		public default void reconcile(T desObj, T sourceObj, List<String> lstIgnoreFields) throws Exception {
			if (sourceObj == null || desObj == null)
				return;
			for (Field f : sourceObj.getClass().getDeclaredFields()) {
				if (!ReflectionUtils.isPrimitive(f.getType()) && !ReflectionUtils.isPrimitiveArr(f.getType()))
					continue;
				if (lstIgnoreFields != null && lstIgnoreFields.contains(f.getName()))
					continue;
				Method getMt = ReflectionUtils.getGeter(sourceObj.getClass(), f);
				if (getMt == null)
					continue;
				Object sourceVal = getMt.invoke(sourceObj);
				Method setMt = ReflectionUtils.getSeter(sourceObj.getClass(), f);
				if (setMt == null)
					continue;
				setMt.invoke(desObj, sourceVal);
			}
		}
	}
}
