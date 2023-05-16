import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class Calculator {
    public int sum(int a, int b) {
        return a + b;
    }
}


public class ExampleTest {
    Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer();
    }

    @Test
    public void test() {
        //Arrange
        var sut = new Calculator();
        //Act;
        var result = sut.sum(2, 2);
        //Assert
        assertEquals(4, result);
    }

    @Test
    public void orderItem_NotEnough() {
        var storage = new Storage();
        storage.addItems(Item.WOOD, 1);

        assertThrows(NotEnoughItems.class, () -> customer.createOrder(storage, Item.WOOD, 2));

        assertEquals(1, storage.getQuantity(Item.WOOD));
    }

    @Test
    public void orderItem_Enough() {
        var storage = new Storage();
        storage.addItems(Item.WOOD, 3);

        var order = customer.createOrder(storage, Item.WOOD, 1);

        assertOrderCorrect(customer, storage, Item.WOOD, 1, order);
        assertEquals(2, storage.getQuantity(Item.WOOD));
    }

    @Test
    public void london_orderItem_Enough() {
        var mock = mock(Storage.class);
        when(mock.hasEnoughItems(Item.WOOD, 1)).thenReturn(true);

        var order = customer.createOrder(mock, Item.WOOD, 1);

        assertOrderCorrect(customer, mock, Item.WOOD, 1, order);
        verify(mock).removeQuantity(Item.WOOD, 1);
    }

    @Test
    public void london_orderItem_NotEnough() {
        var mock = mock(Storage.class);
        when(mock.hasEnoughItems(Item.WOOD, 1)).thenReturn(false);

        assertThrows(NotEnoughItems.class, () -> customer.createOrder(mock, Item.WOOD, 1));

        verify(mock, never()).removeQuantity(Item.WOOD, 1);
    }

    @Test
    public void test_inventory_implementation() {
        var storage = new Storage();
        storage.inventory.put(Item.WOOD, 10);

        Order order = customer.createOrder(storage, Item.WOOD, 5);

        assertTrue(storage.inventory instanceof HashMap<Item, Integer>);
        assertOrderCorrect(customer, storage, Item.WOOD, 5, order);
        assertEquals(5, storage.inventory.get(Item.WOOD));
    }

    private static void assertOrderCorrect(Customer customer, Storage storage, Item item, int quantity, Order order) {
        assertEquals(item, order.getItem());
        assertEquals(quantity, order.getQuantity());
        assertEquals(customer, order.getCustomer());
        assertEquals(storage, order.getStorage());
    }


    @Test
    public void send_invite_email() {
        var mock = mock(EmailPort.class);
        var sut = new SendInviteEmailUseCase(mock);

        sut.execute("any@mail.ru");

        verify(mock).sendInviteEmail("any@mail.ru");
    }

    @Test
    public void get_item_quantity() {
        var stub = mock(ItemPort.class);
        var sut = new GetItemQuantityUseCase(stub);
        when(stub.getItemQuantity(Item.WOOD)).thenReturn(10);

        var quantity = sut.execute(Item.WOOD);

        assertEquals(10, quantity);
    }

    @Test
    public void normalize_russian(){
        var sut = new NameNormalizer();

        var result = sut.normalize("Абв вбаф @ --112");

        assertEquals("Абв вбаф",result);
    }
    @Test
    public void normalize_english(){
        var sut = new NameNormalizer();

        var result = sut.normalize(":;14@ :D asf %212");

        assertEquals("D asf",result);
    }
    @Test
    public void storage_remove_inventory(){
        var storage = new Storage();
        storage.addItems(Item.WOOD, 500);

        storage.removeQuantity(Item.WOOD, 125);

        assertEquals(375, storage.getQuantity(Item.WOOD));
    }

}

class NameNormalizer {
    public String normalize(String name) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < name.length(); i++) {
            if (Character.isAlphabetic(name.charAt(i)) || name.charAt(i) == ' ')
                sb.append(name.charAt(i));
        }

        return sb.toString().trim();
    }
}

interface EmailPort {
    void sendInviteEmail(String email);
}

interface ItemPort {
    int getItemQuantity(Item item);
}

@RequiredArgsConstructor
class GetItemQuantityUseCase {
    private final ItemPort port;

    public int execute(Item item) {
        return port.getItemQuantity(item);
    }
}

@RequiredArgsConstructor
class SendInviteEmailUseCase {
    private final EmailPort port;

    public void execute(String email) {
        port.sendInviteEmail(email);
    }
}

class Customer {
    public Order createOrder(Storage storage, Item item, int quantity) {
        if (storage.hasEnoughItems(item, quantity)) {
            storage.removeQuantity(item, quantity);
            return new Order(storage, item, this, quantity);
        }
        throw new NotEnoughItems();
    }
}

class NotEnoughItems extends RuntimeException {
}

class Storage {
    Map<Item, Integer> inventory;

    public Storage() {
        inventory = new HashMap<>();
    }

    boolean hasEnoughItems(Item item, int quantity) {
        return inventory.get(item) >= quantity;
    }

    public void addItems(Item item, int quantity) {
        if (inventory.containsKey(item))
            inventory.put(item, inventory.get(item) + quantity);
        else
            inventory.put(item, quantity);
    }

    public int getQuantity(Item item) {
        return inventory.get(item);
    }

    public void removeQuantity(Item item, int quantity) {
        inventory.put(item, inventory.get(item) - quantity);
    }
}

@Getter
@AllArgsConstructor
class Order {
    private final Storage storage;
    private final Item item;
    private final Customer customer;
    private final int Quantity;

    public Storage getStorage() {
        return storage;
    }

}

enum Item {
    WOOD
}
