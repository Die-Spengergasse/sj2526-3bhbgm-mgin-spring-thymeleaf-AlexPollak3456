package at.spengergasse.spring_thymeleaf.entities;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @Column(nullable = false, unique = true)
    private String ssn;


    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;


    @Column(nullable = false)
    private LocalDate birthDate;

    public int getId() {
        return id;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
        //error if ssn is not 10 characters long and has letters
        if(ssn.length() != 10 || !ssn.matches("\\d{10}")){
            throw new IllegalArgumentException("SSN must be exactly 10 digits long and contain only numbers");
        }
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        if(firstName.equalsIgnoreCase("admin")){
            throw new IllegalArgumentException("First name cannot be 'admin'");
        }
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        if(lastName.equalsIgnoreCase("admin")){
            throw new IllegalArgumentException("First name cannot be 'admin'");
        }
        this.lastName = lastName;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        if(birthDate.isAfter(LocalDate.now())){
            throw new IllegalArgumentException("Birth date cannot be in the future");
        }
        this.birthDate = birthDate;

    }
}
