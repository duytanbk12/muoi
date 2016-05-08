package fs.tandat.soccernetwork.bean;

/**
 * Created by dracu on 24/04/2016.
 */
public class User {
    private int user_id;
    private String username;
    private String password;
    private String email;
    private String phone;
    private int status;
    private int district_id;
    private int user_type;
    private String last_login;
    private boolean is_verified;
    private String verification_code;
    private String created;
    private String updated;
    private String deleted;

    public User() {
    }

    public User(String username, String password, String email, String phone) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
    }

    public User(int user_id, String username, String password, String email, String phone, int status, int district_id, int user_type, String last_login, boolean is_verified, String verification_code, String created, String updated, String deleted) {
        this.user_id = user_id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.status = status;
        this.district_id = district_id;
        this.user_type = user_type;
        this.last_login = last_login;
        this.is_verified = is_verified;
        this.verification_code = verification_code;
        this.created = created;
        this.updated = updated;
        this.deleted = deleted;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
