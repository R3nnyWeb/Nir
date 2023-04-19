package nik.test;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class JustAssertionsTest {

    @Test
    public void whenThis_thenThat() {
        assertTrue(true);
    }

    @Test
    public void fail() {
        fail();
    }

    @Test
    public void whenSomethingElse_thenSomethingElse() {
        assertTrue(true);
    }
    @Test
    public void whenSomething_thenSomething() {
        assertTrue(true);
    }

    @Test
    public void whensomethingElse_thenSomethingElse() {
        assertTrue(true);
    }
}
