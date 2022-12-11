package com.mongoencrypt.EncMongoDemo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author Uday Shankar
 */

@Document(collection = "DcUsers")
public class DcUsers {

    @Id
    private String id;
    private String user;
    private String role;
    private String [] decryptPermissions;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String[] getDecryptPermissions() {
        return decryptPermissions;
    }

    public void setDecryptPermissions(String[] decryptPermissions) {
        this.decryptPermissions = decryptPermissions;
    }
}
