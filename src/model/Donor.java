package model;

public class Donor {
    private int donorId;
    private String name, bloodGroup, contact, city;
    private int age;
    private boolean isOrganDonor;

    public Donor() {}

    public Donor(String name, int age, String bloodGroup,
                 String contact, String city, boolean isOrganDonor) {
        this.name = name;
        this.age = age;
        this.bloodGroup = bloodGroup;
        this.contact = contact;
        this.city = city;
        this.isOrganDonor = isOrganDonor;
    }

    // Getters & Setters
    public int getDonorId()          { return donorId; }
    public void setDonorId(int id)   { this.donorId = id; }
    public String getName()          { return name; }
    public void setName(String n)    { this.name = n; }
    public String getBloodGroup()    { return bloodGroup; }
    public void setBloodGroup(String bg) { this.bloodGroup = bg; }
    public String getContact()       { return contact; }
    public void setContact(String c) { this.contact = c; }
    public String getCity()          { return city; }
    public void setCity(String c)    { this.city = c; }
    public int getAge()              { return age; }
    public void setAge(int a)        { this.age = a; }
    public boolean isOrganDonor()    { return isOrganDonor; }
    public void setOrganDonor(boolean b) { this.isOrganDonor = b; }

    @Override
    public String toString() {
        return String.format("[%d] %s | Age: %d | Blood: %s | Organ Donor: %s | City: %s",
                donorId, name, age, bloodGroup, isOrganDonor ? "Yes" : "No", city);
    }
}