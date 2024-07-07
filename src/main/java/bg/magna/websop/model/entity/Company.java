package bg.magna.websop.model.entity;


import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "companies")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(name = "vat_number", nullable = false, unique = true)
    private String VATNumber;

    @Column(name = "registered_address", nullable = false)
    private String RegisteredAddress;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String email;

    @OneToMany(mappedBy = "company")
    private Set<User> employees;


    public Company() {
        this.employees = new HashSet<>();
    }

    public Company(String name, String VATNumber, String registeredAddress, String phone, String email) {
        super();
        this.name = name;
        this.VATNumber = VATNumber;
        RegisteredAddress = registeredAddress;
        this.phone = phone;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVATNumber() {
        return VATNumber;
    }

    public void setVATNumber(String VATNumber) {
        this.VATNumber = VATNumber;
    }

    public String getRegisteredAddress() {
        return RegisteredAddress;
    }

    public void setRegisteredAddress(String registeredAddress) {
        RegisteredAddress = registeredAddress;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
