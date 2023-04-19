package nik;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Main Test")
class MainTest {

    @Test
    @DisplayName("sum")
    void sum() {
        assertEquals(4, Main.sum(2,2));
    }
}