package org.tile.ticketing_system.domein;

import java.time.LocalDateTime;

import lombok.extern.java.Log;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Log
class ContractTest {

	private Contract contract = new Contract();

	@BeforeEach
	public void init() {
		contract = new Contract();
		contract.setStatus(Contract.ContractStatus.IN_BEHANDELING);
		contract.setStartDatum(LocalDateTime.now());
		contract.setEindDatum(LocalDateTime.now());
	}

	@Test
	@DisplayName("Constructor mandatory fields")
	void constructor_WithMandatoryFields_SetsPropertiesCorrectly() {
		ContractType contractType = new ContractType("SLA1", ContractType.ManierAanmakenTicket.APPLICATIE, ContractType.GedekteTijdstippen.ALTIJD,
				5, 1, 1000);

		Contract mandatoryFieldContract = new Contract(contractType, Contract.ContractStatus.IN_BEHANDELING,
				LocalDateTime.of(1996, 12, 8, 10, 30), LocalDateTime.of(2030, 12, 8, 10, 30));

		assertEquals(contractType, mandatoryFieldContract.getType());
		assertEquals(Contract.ContractStatus.IN_BEHANDELING, mandatoryFieldContract.getStatus());
		assertEquals(LocalDateTime.of(1996, 12, 8, 10, 30), mandatoryFieldContract.getStartDatum());
		assertEquals(LocalDateTime.of(2030, 12, 8, 10, 30), mandatoryFieldContract.getEindDatum());

	}

	@Test
	@DisplayName("Contract expires within 30 days")
	void eindDatumMinus30Days_Before_CurrentTime_Returns_VervaltBinnenkort_True() {

		contract.setEindDatum(contract.getEindDatum().minusDays(30).minusSeconds(10));

		assertTrue(contract.vervaltBinnenkort());
	}

	@Test
	@DisplayName("Contract does not expire within 30 days")
	void eindDatumMinus30Days_After_CurrentTime_Returns_VervaltBinnenkort_False() {

		contract.setEindDatum(contract.getEindDatum().plusDays(30).plusSeconds(10));
		assertFalse(contract.vervaltBinnenkort());

	}

	@Test
	@DisplayName("Finished contracts do not return expiration warning")
	void contractStatusBeeindigd_Returns_VervaltBinnenkort_False() {

		contract.setEindDatum(LocalDateTime.now().minusDays(30).minusSeconds(10));
		contract.setStatus(Contract.ContractStatus.BEEINDIGD);
		assertFalse(contract.vervaltBinnenkort());

		contract.setEindDatum(LocalDateTime.now().plusDays(30).plusSeconds(10));
		contract.setStatus(Contract.ContractStatus.BEEINDIGD);
		assertFalse(contract.vervaltBinnenkort());

	}

	@Test
	@DisplayName("ToString test")
	void testToString() {

		contract.setStartDatum(LocalDateTime.of(1996, 12, 8, 10, 30));
		contract.setEindDatum(LocalDateTime.of(2030, 12, 8, 10, 30));

		String expected = "Contract [contractId=0, startDatum=" + LocalDateTime.of(1996, 12, 8, 10, 30) + ", eindDatum="
				+ LocalDateTime.of(2030, 12, 8, 10, 30)
				+ ", type=null, status=IN_BEHANDELING, company=null, vervaltBinnenkort=false]";

		log.info(contract.toString());

		assertEquals(expected, contract.toString());
	}

}
