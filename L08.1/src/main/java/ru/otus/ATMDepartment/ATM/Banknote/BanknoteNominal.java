package ru.otus.ATMDepartment.ATM.Banknote;

import static java.util.Arrays.stream;

public enum BanknoteNominal {

    RUB_ERROR(0),
    RUB10(10),
    RUB50(50),
    RUB100(100),
    RUB200(200),
    RUB500(500),
    RUB1000(1000),
    RUB2000(2000),
    RUB5000(5000);

    private final int nominal;

    BanknoteNominal(int nominal) {
        this.nominal = nominal;
    }

    public static BanknoteNominal getByNominal(int nominal) {
        return stream(values()).filter(e -> e.nominal == nominal).findFirst().orElse(RUB_ERROR);
    }

    public int getNominal() {
        return nominal;
    }

}
