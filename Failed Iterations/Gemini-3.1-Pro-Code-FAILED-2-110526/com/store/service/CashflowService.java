package com.store.service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.store.domain.CashflowEntry;
import com.store.domain.CashflowType;
import com.store.exception.StoreBusinessException;

public class CashflowService {
private List entries = new ArrayList();

public void recordEntry(String id, double amount, CashflowType type, String description) {
    if (amount < 0) {
        throw new StoreBusinessException("Cashflow amount cannot be negative");
    }
    entries.add(new CashflowEntry(id, amount, type, description));
}

public double getTotalInflow() {
    double total = 0.0;
    for (CashflowEntry e : entries) {
        if (e.getType() == CashflowType.INFLOW) {
            total += e.getAmount();
        }
    }
    return total;
}

public double getTotalOutflow() {
    double total = 0.0;
    for (CashflowEntry e : entries) {
        if (e.getType() == CashflowType.OUTFLOW) {
            total += e.getAmount();
        }
    }
    return total;
}

public double getNetCashflow() {
    return getTotalInflow() - getTotalOutflow();
}

public void exportToCsv(String filename) {
    FileWriter writer = null;
    try {
        writer = new FileWriter(filename);
        writer.write("EntryId,Type,Amount,Description\n");
        for (CashflowEntry e : entries) {
            writer.write(e.getId() + "," + e.getType() + "," + e.getAmount() + "," + e.getDescription() + "\n");
        }
    } catch (IOException e) {
        throw new StoreBusinessException("Error exporting cashflow to CSV");
    } finally {
        if (writer != null) {
            try {
                writer.close();
            } catch (IOException e) {
            }
        }
    }
}
}