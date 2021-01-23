package customer.model;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class CustomerRole {
    @Id
    private String role_id;
    private String role_name;
    private String role_code;

    public CustomerRole(){}

    public CustomerRole(String role_id, String role_name, String role_code) {
        this.role_id = role_id;
        this.role_name = role_name;
        this.role_code = role_code;
    }

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    public String getRole_name() {
        return role_name;
    }

    public void setRole_name(String role_name) {
        this.role_name = role_name;
    }

    public String getRole_code() {
        return role_code;
    }

    public void setRole_code(String role_code) {
        this.role_code = role_code;
    }
}


