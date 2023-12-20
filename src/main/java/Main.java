import annotations.Component;
import framework.Context;
import model.Car;
import model.Engine;
import model.Man;

import java.lang.reflect.InvocationTargetException;

public class Main {
	public static void main(String[] args) throws InvocationTargetException,
			NoSuchMethodException, InstantiationException, IllegalAccessException {
		Context context = Context.load("model");
		System.out.println("Проверка работы @Component()");
		System.out.println(context.getLoadedClasses());


		System.out.println("Проверка работы @Autowired над полями");
		try {
			Car car = (Car) context.get("Car");
			System.out.println(car.toString());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		System.out.println("Проверка работы @Autowired над конструктором");
		try {
			Man man = (Man) context.get("Man");
			System.out.println(man.toString());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
