package ru.otus.ATMDepartment;

import ru.otus.ATMDepartment.ATM.ATM;

import java.util.ArrayList;
import java.util.List;

public class ATMDepartmentImpl implements ATMDepartment {

    List<ATM> atmList;

    public ATMDepartmentImpl() {
        atmList = new ArrayList<>();
    }

    @Override
    public ATM getATMByIndex(int index) {
        return atmList.get(index);
    }

    @Override
    public void addATM(ATM atm) {
        if (!atmList.contains(atm)) {
            atmList.add(atm);
        }
    }

    @Override
    public void removeATM(ATM atm) {
        if (atmList.contains(atm)) {
            atmList.remove(atm);
        }
    }

    @Override
    public void loadInitialState() {
        for(ATM atm :atmList) {
            atm.loadInitialState();
        }
    }

    @Override
    public Long getBalance() {
        Long result = 0L;
        for (ATM atm : atmList) {
            result += atm.getBalance();
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("ATMDepartment [\n");
        for (int i = 0; i < atmList.size(); i++) {
            result.append(i + 1).append(" ").append(atmList.get(i)).append("\n");
        }
        result.append("Общий баланс = ").append(getBalance()).append("]");
        return String.valueOf(result);
    }
}
