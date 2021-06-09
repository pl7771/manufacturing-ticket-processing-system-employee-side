package org.tile.ticketing_system.domein.controllers;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LoginControllerIT {

    //IT = Integration Test -> Dus geen mocks maar echte DB

    private final LoginController loginController = new LoginController();

    @Test
    void shouldReturnAdministratorController() {
        DomainControllerAbstraction dc = this.loginController.login("administrator@gmail.com", "administrator");

        assertThat(dc.getClass()).isEqualTo(AdministratorController.class);
    }

    @Test
    void shouldReturnSupportManagerController() {
        DomainControllerAbstraction dc = this.loginController.login("supportmanager@gmail.com", "supportmanager");

        assertThat(dc.getClass()).isEqualTo(SupportManagerController.class);
    }

    @Test
    void shouldReturnTechnicianController() {
        DomainControllerAbstraction dc = this.loginController.login("technician@gmail.com", "technician");

        assertThat(dc.getClass()).isEqualTo(TechnicianController.class);
    }

    @Test
    void shouldThrowExceptionIfLoggedInUserIsClient() {
        assertThatThrownBy(() -> this.loginController.login("client@gmail.com", "client"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Gebruiker heeft geen rechten om deze applicatie te gebruiken");
    }
}
