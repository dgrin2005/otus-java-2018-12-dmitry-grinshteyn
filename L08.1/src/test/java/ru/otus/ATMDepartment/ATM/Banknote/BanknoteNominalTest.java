package ru.otus.ATMDepartment.ATM.Banknote;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BanknoteNominalTest {

    BanknoteNominal banknoteNominal;

    @Before
    public void setUp() throws Exception {
        banknoteNominal = BanknoteNominal.RUB100;
    }

    @Test
    public void getByNominal() {
        assertEquals(BanknoteNominal.RUB100, BanknoteNominal.getByNominal(100));
    }

    @Test
    public void getNominal() {
        assertEquals(100, banknoteNominal.getNominal());
    }
}