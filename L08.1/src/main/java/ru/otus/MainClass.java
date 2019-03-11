package ru.otus;

import ru.otus.ATMDepartment.ATM.ATMImpl;
import ru.otus.ATMDepartment.ATM.ATM;
import ru.otus.ATMDepartment.ATM.Banknote.BanknoteNominal;
import ru.otus.ATMDepartment.ATM.Cell.Cell;
import ru.otus.ATMDepartment.ATMDepartment;
import ru.otus.ATMDepartment.ATMDepartmentImpl;

import java.util.Arrays;

public class MainClass {

    public static void main(String[] args) {

        ATM atm1 = new ATMImpl(Arrays.asList(
                new Cell(BanknoteNominal.RUB10, 10),
                new Cell(BanknoteNominal.RUB50, 10),
                new Cell(BanknoteNominal.RUB100, 10),
                new Cell(BanknoteNominal.RUB200, 10),
                new Cell(BanknoteNominal.RUB500, 10),
                new Cell(BanknoteNominal.RUB1000, 10),
                new Cell(BanknoteNominal.RUB2000, 10),
                new Cell(BanknoteNominal.RUB5000, 10)
        ));

        ATM atm2 = new ATMImpl(Arrays.asList(
                new Cell(BanknoteNominal.RUB10, 20),
                new Cell(BanknoteNominal.RUB50, 20),
                new Cell(BanknoteNominal.RUB100, 20),
                new Cell(BanknoteNominal.RUB200, 20),
                new Cell(BanknoteNominal.RUB500, 20),
                new Cell(BanknoteNominal.RUB1000, 20),
                new Cell(BanknoteNominal.RUB2000, 20),
                new Cell(BanknoteNominal.RUB5000, 20)
        ));

        ATM atm3 = new ATMImpl(Arrays.asList(
                new Cell(BanknoteNominal.RUB10, 30),
                new Cell(BanknoteNominal.RUB50, 30),
                new Cell(BanknoteNominal.RUB100, 30),
                new Cell(BanknoteNominal.RUB200, 30),
                new Cell(BanknoteNominal.RUB500, 30),
                new Cell(BanknoteNominal.RUB1000, 30),
                new Cell(BanknoteNominal.RUB2000, 30),
                new Cell(BanknoteNominal.RUB5000, 30)
        ));

        ATMDepartment atmDepartment = new ATMDepartmentImpl();

        atmDepartment.addATM(atm1);
        atmDepartment.addATM(atm2);
        atmDepartment.addATM(atm3);

        System.out.println("Состояние " + atmDepartment);
        System.out.println();

        moneyRequest(atmDepartment, 1, -10);
        moneyRequest(atmDepartment, 1, 123000);
        moneyRequest(atmDepartment, 1, 7800);
        moneyRequest(atmDepartment, 1, 123);
        moneyRequest(atmDepartment, 1, 50000);
        moneyRequest(atmDepartment, 2, 50000);

        System.out.println("Состояние " + atmDepartment);
        System.out.println();

        System.out.println("Восстановление первоначального состояния");
        atmDepartment.loadInitialState();
        System.out.println("Состояние " + atmDepartment);
        System.out.println();

    }

    private static void moneyRequest(ATMDepartment atmDepartment, int index, int sum) {
        System.out.println("Затребовано " + sum + " из АТМ " + (index + 1));
        System.out.println("Выдано " + atmDepartment.getATMByIndex(index).getSum(sum));
        System.out.println("Состояние " + atmDepartment.getATMByIndex(index));
        System.out.println();
    }
}
