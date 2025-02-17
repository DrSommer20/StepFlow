package de.sommer.stepflowBackend.models;

import java.util.Set;

import de.sommer.stepflowBackend.dto.UserDTO;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int user_id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TeamMembership> memberships;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    @Lob
    @Column(name = "profile_picture")
    private byte[] profilePicture;

    public User() {
    }

    public User(UserDTO userDTO){
        this.name = userDTO.getName();
        this.email = userDTO.getEmail();
        this.password = userDTO.getPassword();
        this.active = userDTO.getActive();
    }

    // Getters and Setters
    public int getId() {
        return user_id;
    }

    public void setId(int id) {
        this.user_id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Set<TeamMembership> getMemberships() {
        return memberships;
    }

    public void setMemberships(Set<TeamMembership> memberships) {
        this.memberships = memberships;
    }

    public boolean addMembership(TeamMembership membership){
        return this.memberships.add(membership);
    }

}