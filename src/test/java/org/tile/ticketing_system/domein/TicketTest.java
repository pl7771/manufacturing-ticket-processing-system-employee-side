package org.tile.ticketing_system.domein;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import lombok.extern.java.Log;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Log
class TicketTest {

	static Ticket ticket;

	@BeforeEach
	public void init() {
		ticket = new Ticket(LocalDateTime.now(), "Titel", "Omschrijving");
	}

	@ParameterizedTest
	@DisplayName("Titel Empty Throws")
	@ValueSource(strings = { "", " ", "     " })
	void SetTitel_Empty_ThrowsException(String emptyTitel) {
		assertThrows(IllegalArgumentException.class, () -> new Ticket(LocalDateTime.now(), emptyTitel, "Omschrijving"));
	}

	@ParameterizedTest
	@DisplayName("Titel Filled Passes")
	@ValueSource(strings = { "@)($*!", "testTitel", "titel met spaties" })
	void SetTitel_Filled_Passes(String titel) {
		ticket.setTitel(titel);
		assertEquals(titel, ticket.getTitel());
	}

	@ParameterizedTest
	@DisplayName("Omschrijving Empty Throws")
	@ValueSource(strings = { "", " ", "     " })
	void SetOmschrijving_Empty_ThrowsException(String emptyOmschrijving) {
		assertThrows(IllegalArgumentException.class, () -> new Ticket(LocalDateTime.now(), "Titel", emptyOmschrijving));
	}

	@Test
	@DisplayName("Titel > 100 chars Throws")
	void SetTitel_MoreThan100Characters_ThrowsException() {
		final String teLangeTitel = "a".repeat(101);
		Exception exception = assertThrows(IllegalArgumentException.class, () -> ticket.setTitel(teLangeTitel));
		assertTrue(exception.getMessage().contains("Titel mag niet leeg zijn of langer dan 100 karakters"));
	}

	@Test
	@DisplayName("DatumAangemaakt in future Throws")
	void SetDatumAangemaakt_InFuture_ThrowsException() {
		assertThrows(IllegalArgumentException.class,
				() -> new Ticket(LocalDateTime.now().plusDays(2), "titel", "omschrijving"));
	}

	@Test
	@DisplayName("DatumAfgesloten < DatumAangemaakt Throws")
	void SetDatumAfgesloten_Before_DatumAangemaakt_ThrowsException() {
		Exception exception = assertThrows(IllegalArgumentException.class,
				() -> ticket.setDatumAfgesloten(LocalDateTime.now().minusYears(30)));
		assertTrue(exception.getMessage().contains("Ongeldige afsluitdatum"));
	}

}
