package framework;

import annotations.Autowired;
import annotations.Component;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

public class Context {
	private Map<String, Class<?>> loadedClasses;

	private Context(Map<String, Class<?>> loadedClasses) {
		this.loadedClasses = loadedClasses;
	}

	public static Context load(String packageName) {
		Reflections reflections = new Reflections(packageName, new SubTypesScanner(false));
		Map<String,  Class<?>> map = reflections.getSubTypesOf(Object.class)
				.stream()
				.filter(clazz -> clazz.isAnnotationPresent(Component.class))
				.collect(Collectors.toMap(
						clazz -> clazz.getAnnotation(Component.class).value(),
						clazz -> clazz
				));
		return new Context(map);

	}

	public Map<String, Class<?>> getLoadedClasses() {
		return loadedClasses;
	}

	public Object get(String className) throws NoSuchMethodException, InvocationTargetException,
			InstantiationException, IllegalAccessException, NoSuchFieldException {
		if (!loadedClasses.containsKey(className)) {
			throw  new RuntimeException("Нет объекта");
		}
		Class<?> clazz = loadedClasses.get(className);
		Optional<Constructor<?>> firstConstructor = Arrays.stream(clazz.getDeclaredConstructors())
				.filter(constructor -> constructor.isAnnotationPresent(Autowired.class))
				.findFirst();
		if (firstConstructor.isPresent()) {
			return generateObjectFromParamConstructor(firstConstructor.get());
		}
		Constructor<?> constructor = clazz.getConstructor();
		Object newObject = constructor.newInstance();
		List<String> fieldsAutowired = Arrays.stream(clazz.getDeclaredFields())
				.filter(field -> field.isAnnotationPresent(Autowired.class))
				.map(Field::getName)
				.collect(Collectors.toList());
		if (fieldsAutowired.size() != 0) {
			for (String fieldName : fieldsAutowired) {
				Field field = clazz.getDeclaredField(fieldName);
				clazz.getDeclaredField(fieldName).setAccessible(true);
				field.setAccessible(true);
				field.set(newObject, get(field.getType().getSimpleName()));
			}
		}
			return newObject;

	}


	private Object generateObjectFromParamConstructor(Constructor<?> constructor) throws InvocationTargetException,
			InstantiationException, IllegalAccessException {
		var parameterTypes = constructor.getParameterTypes();
		var params = Arrays.stream(parameterTypes)
				.map(
						cl -> {
							try {
								return get(cl.getAnnotation(Component.class).value());
							} catch (Exception e) {
								throw new RuntimeException("Такой тип нельзя подсавлять как параметр");
							}
						}

				).collect(Collectors.toList());
		return constructor.newInstance(params.toArray());
	}
}
