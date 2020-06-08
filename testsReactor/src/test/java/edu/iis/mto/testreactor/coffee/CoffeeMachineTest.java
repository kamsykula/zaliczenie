package edu.iis.mto.testreactor.coffee;

import static edu.iis.mto.testreactor.coffee.CoffeeMatcher.isTheSameCoffee;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import edu.iis.mto.testreactor.coffee.milkprovider.MilkProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class CoffeeMachineTest {
	
	@Mock
	private CoffeeReceipes receipes;
	@Mock
	private Grinder grinder;
	@Mock
	private MilkProvider milkProvider;
	
	private CoffeeMachine coffeeMachine;
	
	@BeforeEach
	void before() {
		coffeeMachine = new CoffeeMachine(grinder, milkProvider, receipes);
	}
	
	@Test
	void makeCoffeeWithProperOrderShouldReturnProperCoffee() {
		var order = CoffeOrder.builder()
				.withType(CoffeType.ESPRESSO)
				.withSize(CoffeeSize.STANDARD)
				.build();
		
		when(grinder.canGrindFor(any(CoffeeSize.class))).thenReturn(true);
		when(grinder.grind(any(CoffeeSize.class))).thenReturn(50d);
		when(receipes.getReceipe(any(CoffeType.class))).thenReturn(Optional.of(CoffeeReceipe.builder().withMilkAmount(10).withWaterAmounts(Map.of(CoffeeSize.STANDARD, 100)).build()));
		
		Coffee resultCoffee = coffeeMachine.make(order);
		Coffee coffeeToCompare = new Coffee();
		coffeeToCompare.setMilkAmout(10);
		coffeeToCompare.setCoffeeWeigthGr(50d);
		coffeeToCompare.setWaterAmount(100);
		
		assertThat(resultCoffee, isTheSameCoffee(coffeeToCompare));
	}
	
}
