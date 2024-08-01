package bg.magna.websop.model.entity;

import bg.magna.websop.model.enums.UserRole;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole userRole;

    @ElementCollection
    @MapKeyJoinColumn(name = "part_id")
    @JoinTable(name = "user_carts",
            joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "cart_quantity")
    private Map<Part, Integer> cart;

    @OneToMany(mappedBy = "user")
    private List<Order> orders;

    @ManyToOne
    private Company company;

    public UserEntity() {
        this.cart = new HashMap<>();
        this.orders = new ArrayList<>();
    }

    public UserEntity(String email, String password, String firstName, String lastName, UserRole userRole, Company company, String phone) {
        super();
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userRole = userRole;
        this.company = company;
        this.phone = phone;
    }

    public UserEntity(String id, String email, String password, String firstName, String lastName, String phone, UserRole userRole, Map<Part, Integer> cart, List<Order> orders, Company company) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.userRole = userRole;
        this.cart = cart;
        this.orders = orders;
        this.company = company;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public Map<Part, Integer> getCart() {
        return cart;
    }

    public void setCart(Map<Part, Integer> cart) {
        this.cart = cart;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getFullName() {
        return getFirstName() + " " + getLastName();
    }

    public int getCartSize() {
        return getCart().size();
    }

    public void emptyCart() {
        setCart(new HashMap<>());
    }
}
