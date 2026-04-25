package model;

public class TransactionLog {
    private int logId;
    private String transactionType;
    private int donorId;
    private int recipientId;
    private int hospitalId;
    private String bloodGroup;
    private String organType;
    private int units;
    private String notes;

    public TransactionLog() {}

    public TransactionLog(String transactionType, int donorId, int recipientId,
                          int hospitalId, String bloodGroup,
                          String organType, int units, String notes) {
        this.transactionType = transactionType;
        this.donorId = donorId;
        this.recipientId = recipientId;
        this.hospitalId = hospitalId;
        this.bloodGroup = bloodGroup;
        this.organType = organType;
        this.units = units;
        this.notes = notes;
    }

    // Getters & Setters
    public int getLogId()                      { return logId; }
    public void setLogId(int id)               { this.logId = id; }

    public String getTransactionType()         { return transactionType; }
    public void setTransactionType(String t)   { this.transactionType = t; }

    public int getDonorId()                    { return donorId; }
    public void setDonorId(int id)             { this.donorId = id; }

    public int getRecipientId()                { return recipientId; }
    public void setRecipientId(int id)         { this.recipientId = id; }

    public int getHospitalId()                 { return hospitalId; }
    public void setHospitalId(int id)          { this.hospitalId = id; }

    public String getBloodGroup()              { return bloodGroup; }
    public void setBloodGroup(String bg)       { this.bloodGroup = bg; }

    public String getOrganType()               { return organType; }
    public void setOrganType(String o)         { this.organType = o; }

    public int getUnits()                      { return units; }
    public void setUnits(int u)                { this.units = u; }

    public String getNotes()                   { return notes; }
    public void setNotes(String n)             { this.notes = n; }

    @Override
    public String toString() {
        return String.format(
                "[Log #%d] Type: %-18s | Donor: %d → Recipient: %d | Blood: %-4s | Organ: %-10s | Units: %d | Note: %s",
                logId, transactionType, donorId, recipientId,
                bloodGroup != null ? bloodGroup : "N/A",
                organType  != null ? organType  : "N/A",
                units, notes
        );
    }
}