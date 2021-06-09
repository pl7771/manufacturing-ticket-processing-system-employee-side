package org.tile.ticketing_system.domein;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;


class CompanyTest {

    public static Company company;

    @BeforeEach
    public void init() {
        company = new Company("Company", "Straat naam", "Phone Number");
    }

    @Test
    void getName() {
        String name = company.getName();
        assertThat(name).isEqualTo("Company");
    }

}
