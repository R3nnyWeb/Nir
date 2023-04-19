package nik.test;

import nik.Main;
import nik.SomeInterface;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@DisplayName("Main Test")

class MainTest {

    @Test
    @DisplayName("sum")
    void sum() {
        Assertions.assertEquals(4, Main.sum(2,2));
    }

    @Test
    @DisplayName("sum fail")
    void sumFail() {
        assertEquals(4, Main.sum(3,2));
    }

    @Test
    @DisplayName("Mock success test")
    void mockSuccess() {
        SomeInterface mock = mock(SomeInterface.class);
        mock.send(2);
        verify(mock).send(2);
    }

    @Test
    @DisplayName("Stub success test")
    void stubSuccess() {
        SomeInterface stub = mock(SomeInterface.class);
        when(stub.get()).thenReturn(2);
        assertEquals(2, stub.get());
    }

}