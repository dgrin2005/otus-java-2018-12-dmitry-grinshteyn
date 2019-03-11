package ru.otus.ATMDepartment.ATM.Cell;

import ru.otus.ATMDepartment.ATM.Banknote.BanknoteNominal;

import static org.junit.Assert.*;

public class CellTest {

    private Cell testCell;

    @org.junit.Before
    public void setUp() throws Exception {
        testCell = new Cell(BanknoteNominal.RUB100, 10);
    }

    @org.junit.Test
    public void getBanknoteNominal() {
        assertEquals(BanknoteNominal.RUB100, testCell.getBanknoteNominal());
    }

    @org.junit.Test
    public void addOneBanknote() {
        testCell.addOneBanknote();
        assertEquals(new Integer(11), testCell.getAmount());
    }

    @org.junit.Test
    public void removeOneBanknote() {
        testCell.removeOneBanknote();
        assertEquals(new Integer(9), testCell.getAmount());
    }

    @org.junit.Test
    public void getCellBalance() {
        assertEquals(new Long(1000), testCell.getCellBalance());
    }
}