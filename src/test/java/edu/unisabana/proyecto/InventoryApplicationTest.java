package edu.unisabana.proyecto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class InventoryApplicationTest {

    @Test
    void main_ShouldRunApplicationWithoutExceptions() {
        assertDoesNotThrow(() -> InventoryApplication.main(new String[]{"--spring.profiles.active=test"}));
    }
}
