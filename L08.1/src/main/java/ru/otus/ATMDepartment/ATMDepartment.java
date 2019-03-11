package ru.otus.ATMDepartment;

import ru.otus.ATMDepartment.ATM.ATM;

public interface ATMDepartment {

    void addATM(ATM atm);

    void removeATM(ATM atm);

    void loadInitialState();

    Long getBalance();

    ATM getATMByIndex(int index);
}
