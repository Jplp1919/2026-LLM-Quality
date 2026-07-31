package com.store.finance;

import java.util.ArrayList;
import java.util.List;

public class CashflowService {
    private List<CashflowEntry> entries = new ArrayList<CashflowEntry>();

    public void recordEntry(CashflowEntry entry) {
        entries.add(entry);
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

    public List<CashflowEntry> getAllEntries() {
        return new ArrayList<CashflowEntry>(entries);
    }
}