package ru.otus.ATMDepartment.ATM;

import ru.otus.ATMDepartment.ATM.Banknote.BanknoteNominal;

import java.util.Map;

public interface ATM {

    void putBanknote(Integer banknote);

    boolean putOneBanknoteByNominal(BanknoteNominal banknoteNominal);

    void putBanknotesByNominal(BanknoteNominal nominal, Integer amount);

    Long getBalance();

    ATM getSum(Integer sum);

    void loadInitialState();
}
