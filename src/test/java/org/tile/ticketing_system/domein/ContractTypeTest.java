package org.tile.ticketing_system.domein;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import lombok.extern.java.Log;

@Log
class ContractTypeTest {

	static ContractType contractType;

	@BeforeEach

	public void init() {
		contractType = new ContractType();
	}

	@ParameterizedTest
	@DisplayName("Name Empty throws exception")
	@ValueSource(strings = { "", " ", "     " })
	void SetName_Empty_ThrowsException(String value) {
		Exception exception = assertThrows(IllegalArgumentException.class, () -> contractType.setName(value));
		assertTrue(exception.getMessage().contains("Naam mag niet leeg zijn"));
	}

	@ParameterizedTest
	@DisplayName("Negative Maximum lead time throws exception")
	@ValueSource(ints = { -1, -100, Integer.MIN_VALUE })
	void SetMaximaleAfhandeltijd_Negative_ThrowsException(int value) {
		Exception exception = assertThrows(IllegalArgumentException.class,
				() -> contractType.setMaximaleAfhandeltijd(value));
		assertTrue(exception.getMessage().contains("Maximale afhandeltijd mag niet negatief of leeg zijn"));
	}

	@ParameterizedTest
	@DisplayName("Negative Minimum lead time throws exception")
	@ValueSource(ints = { -1, -100, Integer.MIN_VALUE })
	void SetMinimaleDoorlooptijd_Negative_ThrowsException(int value) {
		Exception exception = assertThrows(IllegalArgumentException.class,
				() -> contractType.setMinimaleDoorlooptijd(value));
		assertTrue(exception.getMessage().contains("Minimale doorlooptijd mag niet negatief of leeg zijn"));
	}

	@ParameterizedTest
	@DisplayName("Negative price throws exception")
	@ValueSource(ints = { -1, -100, Integer.MIN_VALUE })
	void SetPrice_Negative_ThrowsException(int value) {
		Exception exception = assertThrows(IllegalArgumentException.class, () -> contractType.setPrijs(value));
		assertTrue(exception.getMessage().contains("Prijs mag niet negatief of leeg zijn"));
	}

	@ParameterizedTest
	@DisplayName("Set negative amount of contracts throws exception")
	@ValueSource(ints = { -1, -100, Integer.MIN_VALUE })
	void SetContractAmount_Negative_ThrowsException(int value) {
		Exception exception = assertThrows(IllegalArgumentException.class,
				() -> contractType.setAantalContracten(value));
		assertTrue(exception.getMessage().contains("Aantal contracten kan niet kleiner zijn dan 0"));
	}

	@Test
	@DisplayName("ToString test")
	 void testToString() {
		contractType.setName("testName");
		contractType.setContractTypeId(1);
		contractType.setManierAanmakenTicket(ContractType.ManierAanmakenTicket.APPLICATIE);
		contractType.setGedekteTijdstippen(ContractType.GedekteTijdstippen.ALTIJD);
		contractType.setMaximaleAfhandeltijd(30);
		contractType.setMinimaleDoorlooptijd(1);
		contractType.setPrijs(100);
		contractType.setAantalContracten(10);
		contractType.setStatus(ContractType.ContractTypeStatus.ACTIEF);
		

		String expected = "ContractType [" + "name=testName, contractTypeId=1," + " manierAanmakenTicket=APPLICATIE,"
				+ " gedekteTijdstippen=ALTIJD," + " maximaleAfhandeltijd=30.0," + " minimaleDoorlooptijd=1.0,"
				+ " prijs=100.0," + " aantalContracten=10," + " status=ACTIEF" + "]";

		assertEquals(expected, contractType.toString());
	}

}
