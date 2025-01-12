package gestionAudits.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements Serializable {
    private int id;
    private String username;
    private String name;
    private String email;
    private String domain;
    private int groupId;
    private int roleId;
    private String uid;
    private transient String password;
    private String photoUrl;

    public User(int id, String username, String storedPassword) {
        this.id = id;
        this.username = username;
        this.password = storedPassword;
    }
    public User(String username, String email,String name,int roleId,String photoUrl) {
        this.username = username;
        this.email = email;
        this.name = name;
        this.roleId = roleId;
    }
    public User(int id,String username, String email,String name,int roleId) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.name = name;
        this.roleId = roleId;
    }
    public User(String username, String email,int role) {
        this.username = username;
        this.email = email;
        this.roleId = role;
    }

    public User() {}

    public User(int id) {
        this.id = id;
    }


    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setPassword(String password) {this.password = password;}

    public String getPassword() {return password;}


    public String string(){
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", domain='" + domain + '\'' +
                ", groupId=" + groupId +
                ", roleId=" + roleId +
                ", uid='" + uid + '\'' +
                ", password='" + password + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                '}';
    }


    @Override
    public String toString() {
        return name;
    }
}