package customer.model;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "customer")
public class Customer {
    @Id
    private String user_id;
    private String user_name;
    private String date_of_birth;
    private String gender;
    private String phone_number;

    public Customer(){}

    public Customer(String user_id, String user_name, String date_of_birth, String gender, String phone_number) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.date_of_birth = date_of_birth;
        this.gender = gender;
        this.phone_number = phone_number;
     }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }
}


