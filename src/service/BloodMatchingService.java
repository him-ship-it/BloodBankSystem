package service;

import dao.BloodInventoryDAO;
import dao.DonorDAO;
import dao.TransactionLogDAO;
import model.Donor;
import model.TransactionLog;

import java.sql.SQLException;
import java.util.List;

public class BloodMatchingService {

    private final DonorDAO donorDAO = new DonorDAO();
    private final BloodInventoryDAO inventoryDAO = new BloodInventoryDAO();
    private final TransactionLogDAO logDAO = new TransactionLogDAO();

    public void donateBlood(int donorId, String bloodGroup, int units,
                            int recipientId, int hospitalId) throws SQLException {
        // Add to inventory first
        boolean added = inventoryDAO.addUnits(bloodGroup, units);
        if (!added) {
            System.out.println("Failed to add blood units.");
            return;
        }

        // Deduct immediately for transfusion
        boolean deducted = inventoryDAO.deductUnits(bloodGroup, units);
        if (!deducted) {
            System.out.println("Transfusion failed: not enough stock.");
            return;
        }

        // Log the transaction
        TransactionLog log = new TransactionLog();
        log.setTransactionType("BLOOD_TRANSFUSION");
        log.setDonorId(donorId);
        log.setRecipientId(recipientId);
        log.setHospitalId(hospitalId);
        log.setBloodGroup(bloodGroup);
        log.setUnits(units);
        log.setNotes("Blood transfusion completed successfully.");
        logDAO.addLog(log);
        System.out.println("Blood transfusion logged successfully.");
    }

    public List<Donor> findMatchingDonors(String bloodGroup) throws SQLException {
        return donorDAO.getDonorsByBloodGroup(bloodGroup);
    }
}