package ru.otus.ATMDepartment.ATM;

import org.junit.Before;
import org.junit.Test;
import ru.otus.ATMDepartment.ATM.Banknote.BanknoteNominal;
import ru.otus.ATMDepartment.ATM.Cell.Cell;

import java.util.Arrays;

import static org.junit.Assert.*;

public class ATMImplTest {

    private ATM atm;

    @Before
    public void setUp() throws Exception {
        atm = new ATMImpl(Arrays.asList(
                new Cell(BanknoteNominal.RUB10, 2),
                new Cell(BanknoteNominal.RUB50, 2),
                new Cell(BanknoteNominal.RUB100, 2),
                new Cell(BanknoteNominal.RUB200, 2),
                new Cell(BanknoteNominal.RUB500, 2),
                new Cell(BanknoteNominal.RUB1000, 2),
                new Cell(BanknoteNominal.RUB2000, 2),
                new Cell(BanknoteNominal.RUB5000, 2)
        ));
    }

    @Test
    public void putBanknote() {
        atm.putBanknote(100);
        assertEquals(new Long(17820), atm.getBalance());
    }

    @Test
    public void putBanknotesByNominal() {
        atm.putBanknotesByNominal(BanknoteNominal.RUB100, 1);
        assertEquals(new Long(17820), atm.getBalance());
    }

    @Test
    public void putOneBanknoteByNominal() {
        atm.putOneBanknoteByNominal(BanknoteNominal.RUB100);
        assertEquals(new Long(17820), atm.getBalance());
    }

    @Test
    public void getBalance() {
        assertEquals(new Long(17720), atm.getBalance());
    }

    @Test
    public void getSum() {
        atm.getSum(14000);
        assertEquals(new Long(3720), atm.getBalance());
    }

    @Test
    public void loadInitialState() {
        atm.putBanknote(100);
        atm.loadInitialState();
        assertEquals(new Long(17720), atm.getBalance());
    }

}