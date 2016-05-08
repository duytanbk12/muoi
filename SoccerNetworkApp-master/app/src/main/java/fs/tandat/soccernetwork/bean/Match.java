package fs.tandat.soccernetwork.bean;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by dracu on 24/04/2016.
 */
public class Match {
    private int match_id;
    private int field_id;
    private int host_id;
    private int status;
    private int maximum_players;
    private int price;
    private String start_time;
    private String end_time;
    private boolean is_verified;
    private String verification_code;
    private String created;
    private String updated;
    private String deleted;

    private String field_name;
    private String username;

    public String getField_name() {
        return field_name;
    }

    public void setField_name(String field_name) {
        this.field_name = field_name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getMatch_id() {
        return match_id;
    }

    public void setMatch_id(int match_id) {
        this.match_id = match_id;
    }

    public int getField_id() {
        return field_id;
    }

    public void setField_id(int field_id) {
        this.field_id = field_id;
    }

    public int getHost_id() {
        return host_id;
    }

    public void setHost_id(int host_id) {
        this.host_id = host_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getMaximum_players() {
        return maximum_players;
    }

    public void setMaximum_players(int maximum_players) {
        this.maximum_players = maximum_players;
    }
    public int getPrice(){
        return price;
    }

    public String getPriceString() {
        return NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(price);
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public boolean is_verified() {
        return is_verified;
    }

    public void setIs_verified(boolean is_verified) {
        this.is_verified = is_verified;
    }

    public String getVerification_code() {
        return verification_code;
    }

    public void setVerification_code(String verification_code) {
        this.verification_code = verification_code;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }
}
