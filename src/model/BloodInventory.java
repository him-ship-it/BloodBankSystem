package model;

public class BloodInventory {
    private int inventoryId;
    private String bloodGroup;
    private int unitsAvailable;

    public BloodInventory() {}

    public BloodInventory(String bloodGroup, int unitsAvailable) {
        this.bloodGroup = bloodGroup;
        this.unitsAvailable = unitsAvailable;
    }

    // Getters & Setters
    public int getInventoryId()              { return inventoryId; }
    public void setInventoryId(int id)       { this.inventoryId = id; }

    public String getBloodGroup()            { return bloodGroup; }
    public void setBloodGroup(String bg)     { this.bloodGroup = bg; }

    public int getUnitsAvailable()           { return unitsAvailable; }
    public void setUnitsAvailable(int units) { this.unitsAvailable = units; }

    @Override
    public String toString() {
        return String.format("Blood Group: %-4s | Units Available: %d",
                bloodGroup, unitsAvailable);
    }
}