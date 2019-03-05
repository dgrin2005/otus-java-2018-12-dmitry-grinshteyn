package ru.otus.ATM;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class ATMImpl implements ATM {

    private Map<Integer, Integer> banknoteCells;

    public ATMImpl() {
        banknoteCells = new TreeMap<>(Collections.reverseOrder());
        banknoteCells.put(10, 0);
        banknoteCells.put(50, 0);
        banknoteCells.put(100, 0);
        banknoteCells.put(200, 0);
        banknoteCells.put(500, 0);
        banknoteCells.put(1000, 0);
        banknoteCells.put(2000, 0);
        banknoteCells.put(5000, 0);
    }

    @Override
    public Map<Integer, Integer> getBanknoteCells() {
        return banknoteCells;
    }

    @Override
    public void setBanknoteCells(Map<Integer, Integer> banknoteCells) {
        this.banknoteCells = banknoteCells;
    }

    @Override
    public void putBanknote(Integer banknote) {
        if (banknoteCells.containsKey(banknote)) {
            banknoteCells.replace(banknote, banknoteCells.get(banknote) + 1);
        } else {
            System.out.println("Ошибка. Нет такого номинала банкнот");
        }

    }

    @Override
    public void getBanknote(Integer banknote) {
        if (banknoteCells.containsKey(banknote)) {
            if (getBanknoteAmount(banknote) > 0) {
                banknoteCells.replace(banknote, banknoteCells.get(banknote) - 1);
            } else {
                System.out.println("Недостаточно средств в банкомате");
            }
        } else {
            System.out.println("Ошибка. Нет такого номинала банкнот");
        }
    }

    @Override
    public Long getBalance() {
        Long balance= 0L;
        for (Map.Entry<Integer, Integer> cell : banknoteCells.entrySet()) {
            balance += cell.getKey() * cell.getValue();
        }
        return balance;
    }

    @Override
    public Integer getBanknoteAmount(Integer banknote) {
        Integer amount = 0;
        if (banknoteCells.containsKey(banknote)) {
            amount = banknoteCells.get(banknote);
        } else {
            System.out.println("Ошибка. Нет такого номинала банкнот");
        }
        return amount;
    }

    @Override
    public ATM getSum(Integer sum) {
        Map<Integer, Integer> banknoteCellsCurrentState = new TreeMap<>(Collections.reverseOrder());
        for (Map.Entry<Integer, Integer> cell : banknoteCells.entrySet()) {
            banknoteCellsCurrentState.put(cell.getKey(), cell.getValue());
        }
        ATM banknotes = new ATMImpl();
        if (sum <= 0) {
            System.out.println("Затребованная сумма должна быть больше нуля");
        } else {
            if (sum <= getBalance()) {
                for (Integer key : banknoteCells.keySet()) {
                    while (getBanknoteAmount(key) > 0 && sum >= key) {
                        getBanknote(key);
                        banknotes.putBanknote(key);
                        sum -= key;
                    }
                }
                if (sum > 0) {
                    banknoteCells = banknoteCellsCurrentState;
                    banknotes = new ATMImpl();
                    System.out.println("Невозможно выдать затребованную сумму");
                }
            } else {
                System.out.println("Недостаточно средств в банкомате");
            }
        }
        return  banknotes;
    }

    @Override
    public String toString() {
        return "ATM {" +
                "Банкноты = " + banknoteCells +
                ", Баланс=" + getBalance() +
                '}';
    }
}

