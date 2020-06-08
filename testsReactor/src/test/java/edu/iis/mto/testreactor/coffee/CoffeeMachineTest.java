package edu.iis.mto.testreactor.coffee;

import static org.hamcrest.MatcherAssert.assertThat;

import edu.iis.mto.testreactor.coffee.milkprovider.MilkProvider;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
	
	
}
