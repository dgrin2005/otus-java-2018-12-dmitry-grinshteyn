package ru.otus.ATMDepartment.ATM.Cell;

import ru.otus.ATMDepartment.ATM.Banknote.BanknoteNominal;

public class Cell {
    private BanknoteNominal banknoteNominal;
    private Integer amount;

    public Cell(BanknoteNominal banknoteNominal, Integer amount) {
        this.banknoteNominal = banknoteNominal;
        this.amount = amount;
    }

    public BanknoteNominal getBanknoteNominal() {
        return banknoteNominal;
    }

    public void addOneBanknote() {
        amount++;
    }

    public boolean removeOneBanknote() {
        if (amount > 0) {
            amount--;
            return true;
        } else {
            return false;
        }
    }

    public Long getCellBalance() {
        return (long) (banknoteNominal.getNominal() * amount);
    }

    @Override
    public String toString() {
        return "\nЯчейка {" +
                "Номинал = " + banknoteNominal +
                ", Количество = " + amount +
                '}';
    }

    private void setBanknoteNominal(BanknoteNominal banknoteNominal) {
        this.banknoteNominal = banknoteNominal;
    }

    private void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Cell clone() {
        return new Cell(this.banknoteNominal, this.amount);
    }
}
