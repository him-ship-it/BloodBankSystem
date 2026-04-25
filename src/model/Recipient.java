package model;

public class Recipient {
    private int recipientId, hospitalId;
    private String name, bloodGroup, contact, organNeeded, urgencyLevel;
    private int age;

    public Recipient() {}

    public Recipient(String name, int age, String bloodGroup,
                     String contact, int hospitalId,
                     String organNeeded, String urgencyLevel) {
        this.name = name;
        this.age = age;
        this.bloodGroup = bloodGroup;
        this.contact = contact;
        this.hospitalId = hospitalId;
        this.organNeeded = organNeeded;
        this.urgencyLevel = urgencyLevel;
    }

    public int getRecipientId()           { return recipientId; }
    public void setRecipientId(int id)    { this.recipientId = id; }
    public int getHospitalId()            { return hospitalId; }
    public void setHospitalId(int id)     { this.hospitalId = id; }
    public String getName()               { return name; }
    public void setName(String n)         { this.name = n; }
    public int getAge()                   { return age; }
    public void setAge(int a)             { this.age = a; }
    public String getBloodGroup()         { return bloodGroup; }
    public void setBloodGroup(String bg)  { this.bloodGroup = bg; }
    public String getContact()            { return contact; }
    public void setContact(String c)      { this.contact = c; }
    public String getOrganNeeded()        { return organNeeded; }
    public void setOrganNeeded(String o)  { this.organNeeded = o; }
    public String getUrgencyLevel()       { return urgencyLevel; }
    public void setUrgencyLevel(String u) { this.urgencyLevel = u; }

    @Override
    public String toString() {
        return String.format("[%d] %s | Blood: %s | Organ Needed: %s | Urgency: %s | Hospital ID: %d",
                recipientId, name, bloodGroup, organNeeded, urgencyLevel, hospitalId);
    }
}
