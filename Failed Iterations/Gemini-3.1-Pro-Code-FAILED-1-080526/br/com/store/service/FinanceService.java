package br.com.store.service;

import br.com.store.domain.CashflowEntry;
import br.com.store.domain.CashflowType;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class FinanceService {
private List entries;
private int entryCounter;

public FinanceService() {
    this.entries = new ArrayList<CashflowEntry>();
    this.entryCounter = 1;
}

public void registerInflow(double amount, String description) {
    entries.add(new CashflowEntry("CF" + entryCounter++, CashflowType.INFLOW, amount, description));
}

public void registerOutflow(double amount, String description) {
    entries.add(new CashflowEntry("CF" + entryCounter++, CashflowType.OUTFLOW, amount, description));
}

public double getTotalInflow() {
    double total = 0.0;
    for (CashflowEntry entry : entries) {
        if (entry.getType() == CashflowType.INFLOW) {
            total += entry.getAmount();
        }
    }
    return total;
}

public double getTotalOutflow() {
    double total = 0.0;
    for (CashflowEntry entry : entries) {
        if (entry.getType() == CashflowType.OUTFLOW) {
            total += entry.getAmount();
        }
    }
    return total;
}

public double getNetCashflow() {
    return getTotalInflow() - getTotalOutflow();
}

public void exportCashflowCsv(String filename) {
    PrintWriter pw = null;
    try {
        pw = new PrintWriter(new FileWriter(filename));
        pw.println("ID,Type,Amount,Description");
        for (CashflowEntry entry : entries) {
            pw.println(entry.getId() + "," + entry.getType() + "," + entry.getAmount() + "," + entry.getDescription());
        }
    } catch (IOException e) {
        throw new RuntimeException(e);
    } finally {
        if (pw != null) {
            pw.close();
        }
    }
}
}