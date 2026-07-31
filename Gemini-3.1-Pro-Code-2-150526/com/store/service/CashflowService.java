package com.store.service;

import java.util.ArrayList;
import java.util.List;
import com.store.domain.CashflowEntry;
import com.store.domain.enums.CashflowType;

public class CashflowService {
    private List<CashflowEntry> entries;

    public CashflowService() {
        this.entries = new ArrayList<CashflowEntry>();
    }

    public void registerInflow(String id, double amount, String description) {
        entries.add(new CashflowEntry(id, CashflowType.INFLOW, amount, description));
    }

    public void registerOutflow(String id, double amount, String description) {
        entries.add(new CashflowEntry(id, CashflowType.OUTFLOW, amount, description));
    }

    public List<CashflowEntry> getEntries() {
        return entries;
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
}