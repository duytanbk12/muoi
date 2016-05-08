package fs.tandat.soccernetwork.bean;

/**
 * Created by dracu on 27/04/2016.
 */
public class Field {
    private int field_id;
    private String field_name;
    private int district_id;
    private String address;
    private double latitude;
    private double longitude;
    private String phone_number;
    private String created;
    private String updated;
    private String deleted;

    public int getField_id() {
        return field_id;
    }


    public Field() {
    }

    public Field(int field_id, String field_name, int district_id, String address, double latitude, double longitude,
                 String phone_number, String created, String updated, String deleted) {
        this.field_id = field_id;
        this.field_name = field_name;
        this.district_id = district_id;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.phone_number = phone_number;
        this.created = created;
        this.updated = updated;
        this.deleted = deleted;
    }

    public void setField_id(int field_id) {
        this.field_id = field_id;
    }

    public String getField_name() {
        return field_name;
    }

    public void setField_name(String field_name) {
        this.field_name = field_name;
    }

    public int getDistrict_id() {
        return district_id;
    }

    public void setDistrict_id(int district_id) {
        this.district_id = district_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
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
