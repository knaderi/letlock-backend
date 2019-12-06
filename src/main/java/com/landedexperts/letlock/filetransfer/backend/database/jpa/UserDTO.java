package com.landedexperts.letlock.filetransfer.backend.database.jpa;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import org.hibernate.annotations.Type;

import com.landedexperts.letlock.filetransfer.backend.database.jpa.types.PasswordEncodingAlgoType;
import com.landedexperts.letlock.filetransfer.backend.database.jpa.types.PasswordHashAlgoType;
import com.landedexperts.letlock.filetransfer.backend.database.jpa.types.UserStatusType;



@Entity
@Table(name = "users", schema="users")

public class UserDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false, updatable = false)
    private long id;


    @Column(name = "login_name", nullable = false, updatable = false)
    @NotEmpty(message = "Please provide a loginName")
    private String loginName;
      

    @Column(name = "email", nullable = false, unique = true)
    @Email(message = "Please provide a valid e-mail")
    @NotEmpty(message = "Please provide an e-mail")
    private String email;

    @Column(name = "encrypted_password",  nullable = false)
    private String password;
    
    @Column(name = "password_hashing_algo", nullable = false)
    @Type(type="com.landedexperts.letlock.filetransfer.backend.database.jpa.types.PGEnumUserType", parameters={@org.hibernate.annotations.Parameter(name = "enumClassName", value="com.landedexperts.letlock.filetransfer.backend.database.jpa.types.PasswordHashAlgoType")})
    private PasswordHashAlgoType passwordHashingAlgo;
    
    @Column(name = "password_encoding_algo", nullable = false)
    @Type(type="com.landedexperts.letlock.filetransfer.backend.database.jpa.types.PGEnumUserType", parameters={@org.hibernate.annotations.Parameter(name = "enumClassName", value="com.landedexperts.letlock.filetransfer.backend.database.jpa.types.PasswordEncodingAlgoType")})
    private PasswordEncodingAlgoType passwordEncodingAlgo;

    @Column(name = "status")
    @Type(type="com.landedexperts.letlock.filetransfer.backend.database.jpa.types.PGEnumUserType", parameters={@org.hibernate.annotations.Parameter(name = "enumClassName", value="com.landedexperts.letlock.filetransfer.backend.database.jpa.types.UserStatusType")})
    private UserStatusType status;

    @Column(name = "status_dt")
    private Date statusDate;

    @Column(name = "create_dt")
    private Date createdDate;

    @Column(name = "update_dt")
    private Date updatedDate;

    @Column(name = "last_login")
    private Date lastLogin;

    @Column(name = "reset_token")
    private String resetToken;

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public PasswordHashAlgoType getPasswordHashingAlgo() {
        return passwordHashingAlgo;
    }

    public void setPasswordHashingAlgo(PasswordHashAlgoType passwordHashingAlgo) {
        this.passwordHashingAlgo = passwordHashingAlgo;
    }

    public PasswordEncodingAlgoType getPasswordEncodingAlgo() {
        return passwordEncodingAlgo;
    }


    public void setPasswordEncodingAlgo(PasswordEncodingAlgoType passwordEncodingAlgo) {
        this.passwordEncodingAlgo = passwordEncodingAlgo;
    }

    public UserStatusType getStatus() {
        return status;
    }

    public void setStatus(UserStatusType status) {
        this.status = status;
    }

    public Date getStatusDate() {
        return statusDate;
    }

    public void setStatusDate(Date statusDate) {
        this.statusDate = statusDate;
    }

    public Date isCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }
}