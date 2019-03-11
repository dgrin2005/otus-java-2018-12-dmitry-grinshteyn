package ru.otus.ATMDepartment;

import org.junit.Before;
import org.junit.Test;
import ru.otus.ATMDepartment.ATM.ATM;
import ru.otus.ATMDepartment.ATM.ATMImpl;
import ru.otus.ATMDepartment.ATM.Banknote.BanknoteNominal;
import ru.otus.ATMDepartment.ATM.Cell.Cell;

import java.util.Arrays;

import static org.junit.Assert.*;

public class ATMDepartmentImplTest {

    private ATMDepartment atmDepartment;
    private ATM atm1;
    private ATM atm2;
    private ATM atm3;

    @Before
    public void setUp() throws Exception {
        atm1 = new ATMImpl(Arrays.asList(
                new Cell(BanknoteNominal.RUB10, 1),
                new Cell(BanknoteNominal.RUB50, 1),
                new Cell(BanknoteNominal.RUB100, 1),
                new Cell(BanknoteNominal.RUB200, 1),
                new Cell(BanknoteNominal.RUB500, 1),
                new Cell(BanknoteNominal.RUB1000, 1),
                new Cell(BanknoteNominal.RUB2000, 1),
                new Cell(BanknoteNominal.RUB5000, 1)
        ));

        atm2 = new ATMImpl(Arrays.asList(
                new Cell(BanknoteNominal.RUB10, 2),
                new Cell(BanknoteNominal.RUB50, 2),
                new Cell(BanknoteNominal.RUB100, 2),
                new Cell(BanknoteNominal.RUB200, 2),
                new Cell(BanknoteNominal.RUB500, 2),
                new Cell(BanknoteNominal.RUB1000, 2),
                new Cell(BanknoteNominal.RUB2000, 2),
                new Cell(BanknoteNominal.RUB5000, 2)
        ));

        atm3 = new ATMImpl(Arrays.asList(
                new Cell(BanknoteNominal.RUB10, 3),
                new Cell(BanknoteNominal.RUB50, 3),
                new Cell(BanknoteNominal.RUB100, 3),
                new Cell(BanknoteNominal.RUB200, 3),
                new Cell(BanknoteNominal.RUB500, 3),
                new Cell(BanknoteNominal.RUB1000, 3),
                new Cell(BanknoteNominal.RUB2000, 3),
                new Cell(BanknoteNominal.RUB5000, 3)
        ));


        atmDepartment = new ATMDepartmentImpl();

        atmDepartment.addATM(atm1);
        atmDepartment.addATM(atm2);
    }

    @Test
    public void getATMByIndex() {
        assertEquals(atm1, atmDepartment.getATMByIndex(0));
    }

    @Test
    public void addATM() {
        atmDepartment.addATM(atm3);
        assertEquals(new Long(53160), atmDepartment.getBalance());
    }

    @Test
    public void removeATM() {
        atmDepartment.removeATM(atm1);
        assertEquals(new Long(17720), atmDepartment.getBalance());
    }

    @Test
    public void loadInitialState() {
        atmDepartment.getATMByIndex(0).getSum(5000);
        atmDepartment.loadInitialState();
        assertEquals(new Long(26580), atmDepartment.getBalance());
    }

    @Test
    public void getBalance() {
        assertEquals(new Long(26580), atmDepartment.getBalance());
    }
}