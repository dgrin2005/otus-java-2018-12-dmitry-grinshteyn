package ru.otus.ATM;

import java.util.Map;

public interface ATM {

    Map<Integer, Integer> getBanknoteCells();

    void setBanknoteCells(Map<Integer, Integer> banknoteCells);

    void putBanknote(Integer banknote);

    void getBanknote(Integer banknote);

    Long getBalance();

    Integer getBanknoteAmount(Integer banknote);

    ATM getSum(Integer sum);
}
