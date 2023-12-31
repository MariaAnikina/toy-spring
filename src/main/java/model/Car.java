package model;

import annotations.Autowired;
import annotations.Component;
import model.Body;
import model.Engine;

@Component("Car")
public class Car {
	@Autowired
	private Body body;

	private Engine engine;

	public Car(Body body, Engine engine) {
		this.body = body;
		this.engine = engine;
	}

	public Car() {
	}

	public Body getBody() {
		return body;
	}

	public void setBody(Body body) {
		this.body = body;
	}

	public Engine getEngine() {
		return engine;
	}

	public void setEngine(Engine engine) {
		this.engine = engine;
	}

	@Override
	public String toString() {
		return "Car{" +
				"body=" + body +
				", engine=" + engine +
				'}';
	}
}
