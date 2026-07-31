package store.service;

import java.util.Iterator;

import store.model.cashflow.CashflowEntry;
import store.model.cashflow.CashflowType;
import store.repository.DataStore;

public class CashflowService {

    private DataStore dataStore;

    public CashflowService(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    public void registerManualOutflow(String id, double amount, String description) {
        dataStore.getCashflowEntries().add(
                new CashflowEntry(id, CashflowType.OUTFLOW, amount, description));
    }

    public double totalInflow() {
        double total = 0;

        Iterator iterator = dataStore.getCashflowEntries().iterator();

        while (iterator.hasNext()) {
            CashflowEntry entry = (CashflowEntry) iterator.next();

            if (entry.getType() == CashflowType.INFLOW) {
                total += entry.getAmount();
            }
        }

        return total;
    }

    public double totalOutflow() {
        double total = 0;

        Iterator iterator = dataStore.getCashflowEntries().iterator();

        while (iterator.hasNext()) {
            CashflowEntry entry = (CashflowEntry) iterator.next();

            if (entry.getType() == CashflowType.OUTFLOW) {
                total += entry.getAmount();
            }
        }

        return total;
    }

    public double netCashflow() {
        return totalInflow() - totalOutflow();
    }
}