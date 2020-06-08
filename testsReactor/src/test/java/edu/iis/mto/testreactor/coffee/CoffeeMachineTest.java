package edu.iis.mto.testreactor.coffee;

import static edu.iis.mto.testreactor.coffee.CoffeeMatcher.isTheSameCoffee;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import edu.iis.mto.testreactor.coffee.milkprovider.MilkProvider;
import edu.iis.mto.testreactor.coffee.milkprovider.MilkProviderException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
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
	private int standardMilkAmout;
	private double coffeeWeigthGrForStandardSize;
	private int waterAmountForStandardSize;
	private Map<CoffeeSize, Integer> receipts;
	private CoffeOrder order;
	
	@BeforeEach
	void before() {
		standardMilkAmout = 10;
		waterAmountForStandardSize = 100;
		coffeeWeigthGrForStandardSize = 50d;
		
		order = CoffeOrder.builder()
				.withType(CoffeType.ESPRESSO)
				.withSize(CoffeeSize.STANDARD)
				.build();
		coffeeMachine = new CoffeeMachine(grinder, milkProvider, receipes);
	}
	
	@Test
	void makeCoffeeWithProperOrderWithMilkShouldReturnProperCoffeeWithMilk() {
		properPrepareGrinder();
		prepareSingleReceiptForCoffeeWithMilk(standardMilkAmout);
		
		Coffee resultCoffee = coffeeMachine.make(order);
		
		assertThat(resultCoffee, isTheSameCoffee(coffee(standardMilkAmout, coffeeWeigthGrForStandardSize, waterAmountForStandardSize)));
	}
	
	@Test
	void makeCoffeeWithProperOrderWithoutMilkShouldReturnProperCoffeeWithMilk() {
		properPrepareGrinder();
		prepareSingleReceiptForCoffeeWithoutMilk();
		
		Coffee resultCoffee = coffeeMachine.make(order);
		
		assertThat(resultCoffee, isTheSameCoffee(coffee(null, coffeeWeigthGrForStandardSize, waterAmountForStandardSize)));
	}
	
	
	@Test
	void whenCoffeeMachineCannotGrindBeansCoffeeMachineShouldThrowException() {
		when(grinder.canGrindFor(any(CoffeeSize.class))).thenReturn(false);
		
		assertThrows(NoCoffeeBeansException.class, () -> coffeeMachine.make(order));
	}
	
	
	@Test
	void makeCoffeeWithoutReceiptShouldThrowException() {
		properPrepareGrinder();

		assertThrows(UnsupportedCoffeeException.class, () -> coffeeMachine.make(order));
	}
	
	@Test
	void makeCoffeeWithMilkShouldUseMilkProvider() throws MilkProviderException {
		properPrepareGrinder();
		prepareSingleReceiptForCoffeeWithMilk(standardMilkAmout);
		
		coffeeMachine.make(order);
		
		InOrder milkProviderCallOrder = Mockito.inOrder(milkProvider);
		milkProviderCallOrder.verify(milkProvider).heat();
		milkProviderCallOrder.verify(milkProvider).pour(standardMilkAmout);
	}
	
	@Test
	void makeCoffeeWithMilkAndWithBrokenMilkProviderShouldReturnCoffeeWithoutMilk() throws MilkProviderException {
		properPrepareGrinder();
		prepareSingleReceiptForCoffeeWithMilk(standardMilkAmout);
		
		doThrow(MilkProviderException.class).when(milkProvider).heat();
		
		Coffee resultCoffee = coffeeMachine.make(order);
		
		assertThat(resultCoffee, isTheSameCoffee(coffee(0, coffeeWeigthGrForStandardSize, waterAmountForStandardSize)));
	}
	
	
	private void prepareSingleReceiptForCoffeeWithMilk(int milkAmout) {
		receipts = Map.of(CoffeeSize.STANDARD, waterAmountForStandardSize);
		when(receipes.getReceipe(any(CoffeType.class)))
				.thenReturn(Optional.of(CoffeeReceipe.builder()
						.withMilkAmount(milkAmout)
						.withWaterAmounts(receipts)
						.build()));
	}
	
	private void prepareSingleReceiptForCoffeeWithoutMilk() {
		prepareSingleReceiptForCoffeeWithMilk(0);
	}
	
	private void properPrepareGrinder() {
		when(grinder.canGrindFor(any(CoffeeSize.class))).thenReturn(true);
		when(grinder.grind(any(CoffeeSize.class))).thenReturn(coffeeWeigthGrForStandardSize);
	}
	
	private Coffee coffee(Integer standardMilkAmout, Double coffeeWeigthGrForStandardSize, Integer waterAmountForStandardSize) {
		Coffee coffeeToCompare = new Coffee();
		coffeeToCompare.setMilkAmout(standardMilkAmout);
		coffeeToCompare.setCoffeeWeigthGr(coffeeWeigthGrForStandardSize);
		coffeeToCompare.setWaterAmount(waterAmountForStandardSize);
		return coffeeToCompare;
	}
	
}
