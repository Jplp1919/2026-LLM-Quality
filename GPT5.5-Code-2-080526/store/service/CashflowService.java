package store.service;

import store.model.cashflow.CashflowEntry;
import store.model.cashflow.CashflowType;
import store.repository.DataStore;

public class CashflowService {

    private DataStore dataStore;

    public CashflowService(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    public void addEntry(CashflowEntry entry) {
        dataStore.getCashflows().add(entry);
    }

    public void addManualOutflow(String id, double amount, String description) {
        addEntry(new CashflowEntry(id, CashflowType.OUTFLOW, amount, description));
    }

    public double getTotalInflow() {
        double total = 0;
        int i;

        for (i = 0; i < dataStore.getCashflows().size(); i++) {
            CashflowEntry entry = (CashflowEntry) dataStore.getCashflows().get(i);

            if (entry.getType() == CashflowType.INFLOW) {
                total += entry.getAmount();
            }
        }

        return total;
    }

    public double getTotalOutflow() {
        double total = 0;
        int i;

        for (i = 0; i < dataStore.getCashflows().size(); i++) {
            CashflowEntry entry = (CashflowEntry) dataStore.getCashflows().get(i);

            if (entry.getType() == CashflowType.OUTFLOW) {
                total += entry.getAmount();
            }
        }

        return total;
    }

    public double getNetCashflow() {
        return getTotalInflow() - getTotalOutflow();
    }
}