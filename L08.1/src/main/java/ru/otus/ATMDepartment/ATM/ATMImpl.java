package ru.otus.ATMDepartment.ATM;

import ru.otus.ATMDepartment.ATM.Banknote.BanknoteNominal;
import ru.otus.ATMDepartment.ATM.Cell.Cell;

import java.util.*;

public class ATMImpl implements ATM {

    private List<Cell> banknoteCells;

    private Memento state;

    private Memento initialState;

    public ATMImpl(List<Cell> banknoteCells) {
        this.banknoteCells = banknoteCells;
        saveInitialState();
    }

    @Override
    public void putBanknote(Integer banknote) {
        BanknoteNominal banknoteNominal = BanknoteNominal.getByNominal(banknote);
        if (banknoteNominal != BanknoteNominal.RUB_ERROR) {
            if (!putOneBanknoteByNominal(banknoteNominal)) {
                System.out.println("Ошибка. Нет ячейки для такого номинала банкнот");
            }
        } else {
            System.out.println("Ошибка. Нет такого номинала банкнот");
        }
    }

    @Override
    public void putBanknotesByNominal(BanknoteNominal banknoteNominal, Integer amount) {
        if (banknoteNominal != BanknoteNominal.RUB_ERROR) {
            boolean success = true;
            for (int i = 0; i < amount; i++) {
                success &= putOneBanknoteByNominal(banknoteNominal);
            }
            if (!success) {
                System.out.println("Ошибка. Нет ячейки для такого номинала банкнот");
            }
        } else {
            System.out.println("Ошибка. Нет такого номинала банкнот");
        }
    }

    public boolean putOneBanknoteByNominal(BanknoteNominal banknoteNominal) {
        Optional<Cell> cell = getCellByNominal(banknoteNominal);
        if (cell.isPresent()) {
            cell.get().addOneBanknote();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Long getBalance() {
        Long balance= 0L;
        for (Cell cell: banknoteCells) {
            balance += cell.getCellBalance();
        }
        return balance;
    }

    @Override
    public String toString() {
        return "ATM {" +
                banknoteCells +
                ",\nБаланс=" + getBalance() +
                '}';
    }

    @Override
    public ATM getSum(Integer sum) {
        ATM recievedFromATM = new ATMImpl(Arrays.asList(
                new Cell(BanknoteNominal.RUB10, 0),
                new Cell(BanknoteNominal.RUB50, 0),
                new Cell(BanknoteNominal.RUB100, 0),
                new Cell(BanknoteNominal.RUB200, 0),
                new Cell(BanknoteNominal.RUB500, 0),
                new Cell(BanknoteNominal.RUB1000, 0),
                new Cell(BanknoteNominal.RUB2000, 0),
                new Cell(BanknoteNominal.RUB5000, 0)
        ));
        if (sum <= 0) {
            System.out.println("Затребованная сумма должна быть больше нуля");
        } else {
            if (sum <= getBalance()) {
                saveState();
                Set<BanknoteNominal> banknoteNominalList = new TreeSet<>(Collections.reverseOrder());
                for(Cell cell : banknoteCells) {
                    banknoteNominalList.add(cell.getBanknoteNominal());
                }
                for(BanknoteNominal banknoteNominal: banknoteNominalList) {
                    int nominal = banknoteNominal.getNominal();
                    Optional<Cell> cell = getCellByNominalAndBalanceGreaterThanZero(banknoteNominal);
                    while (cell.isPresent() && sum >= nominal) {
                        cell.get().removeOneBanknote();
                        recievedFromATM.putOneBanknoteByNominal(banknoteNominal);
                        sum -= nominal;
                        cell = getCellByNominalAndBalanceGreaterThanZero(banknoteNominal);
                    }
                }
                if (sum > 0) {
                    loadState();
                    recievedFromATM.loadInitialState();
                    System.out.println("Невозможно выдать затребованную сумму");
                }
            } else {
                System.out.println("Недостаточно средств в банкомате");
            }
        }
        return recievedFromATM;
    }


    private Optional<Cell> getCellByNominal (BanknoteNominal banknoteNominal) {
        return banknoteCells.stream()
                .filter(x -> (x.getBanknoteNominal() == banknoteNominal))
                .findFirst();
    }

    private Optional<Cell> getCellByNominalAndBalanceGreaterThanZero (BanknoteNominal banknoteNominal) {
        return banknoteCells.stream()
                .filter(x -> (x.getBanknoteNominal() == banknoteNominal && x.getCellBalance() > 0))
                .findFirst();
    }

    private void saveState() {
        state = new Memento(banknoteCells);
    }

    private void loadState() {
        banknoteCells = state.undo();
    }

    private void saveInitialState() {
        initialState = new Memento(banknoteCells);
    }

    @Override
    public void loadInitialState() {
        banknoteCells = initialState.undo();
    }

    private class Memento {
        private List<Cell> banknoteCellsState;

        private Memento(List<Cell> banknoteCells) {
            this.banknoteCellsState = new ArrayList<>();
            for (Cell cell : banknoteCells) {
                this.banknoteCellsState.add(cell.clone());
            }
        }

        private List<Cell> undo() {
            return this.banknoteCellsState;
        }
    }

}

