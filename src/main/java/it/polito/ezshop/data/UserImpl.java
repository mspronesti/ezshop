package it.polito.ezshop.data;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class UserImpl implements User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
   
    @Column(unique = true)
    private String username;
    
    private String password = "";
    private String role = Role.Cashier.name();

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getRole() {
        return this.role;
    }

    @Override
    public void setRole(String role) {
        this.role = role;
    }
}
