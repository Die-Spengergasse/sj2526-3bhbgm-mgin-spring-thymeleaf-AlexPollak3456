package at.spengergasse.spring_thymeleaf.entities;

import jakarta.persistence.*;

@Entity
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // Bezeichnung (ID)
    @Column(nullable = false, unique = true)
    private String designation;

    // Art des Gerätes
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeviceType type;

    // Standort (Raumnummer)
    @Column(nullable = false)
    private String room;

    public int getId() {
        return id;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public DeviceType getType() {
        return type;
    }

    public void setType(DeviceType type) {
        this.type = type;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }
}
