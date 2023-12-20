package model;

import annotations.Autowired;
import annotations.Component;

@Component("Man")
public class Man {
	private Car car;
	private Body body;

	@Autowired
	public Man(Car car, Body body) {
		this.car = car;
		this.body = body;
	}

	public Car getCar() {
		return car;
	}

	public void setCar(Car car) {
		this.car = car;
	}

	@Override
	public String toString() {
		return "Man{" +
				"car=" + car +
				", body=" + body +
				'}';
	}
}
