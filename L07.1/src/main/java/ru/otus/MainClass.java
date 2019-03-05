package ru.otus;

import ru.otus.ATM.ATM;
import ru.otus.ATM.ATMImpl;

public class MainClass {

    public static void main(String[] args) {

        ATM atm = new ATMImpl();
        fillAtm(atm);
        System.out.println("Состояние " + atm);
        System.out.println();

        moneyRequest(atm, -10);
        moneyRequest(atm, 123000);
        moneyRequest(atm, 7800);
        moneyRequest(atm, 123);
        moneyRequest(atm, 50000);

    }

    static void fillAtm(ATM atm) {
        for(int i = 0; i < 20; i++) {
            atm.putBanknote(10);
            atm.putBanknote(50);
            atm.putBanknote(100);
            atm.putBanknote(200);
            atm.putBanknote(500);
            atm.putBanknote(1000);
            atm.putBanknote(2000);
            atm.putBanknote(5000);
        }
    }

    static void moneyRequest(ATM atm, int sum) {
        System.out.println("Затребовано " + sum);
        System.out.println("Выдано " + atm.getSum(sum));
        System.out.println("Состояние " + atm);
        System.out.println();
    }
}
