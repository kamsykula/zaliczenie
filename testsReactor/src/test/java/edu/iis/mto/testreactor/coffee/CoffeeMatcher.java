package edu.iis.mto.testreactor.coffee;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class CoffeeMatcher extends TypeSafeMatcher<Coffee> {
	private Coffee coffee;
	
	CoffeeMatcher(Coffee coffee) {
		this.coffee = coffee;
	}
	
	@Override
	public void describeTo(Description description) {
		// ...
	}
	
	@Override
	protected boolean matchesSafely(Coffee coffee) {
		return this.coffee.getCoffeeWeigthGr().equals(coffee.getCoffeeWeigthGr())
				&& this.coffee.getMilkAmout().equals(coffee.getMilkAmout())
				&& this.coffee.getWaterAmount().equals(coffee.getWaterAmount())
				&& this.coffee.getWaterAmount().equals(coffee.getWaterAmount());
	}
	public static CoffeeMatcher isTheSameCoffee(Coffee coffee) {
		return new CoffeeMatcher(coffee);
	}
}
