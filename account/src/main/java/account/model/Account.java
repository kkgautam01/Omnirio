package account.model;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "account")
public class Account {
    @Id
    private String account_id;
    private String account_type;
    private String open_date;
    private String customer_id;
    private String customer_name;
    private String branch;
    private String minor_indicator;

    public Account(){}

    public Account(String account_id, String account_type, String open_date, String customer_id, String customer_name, String branch, String minor_indicator) {
        this.account_id = account_id;
        this.account_type = account_type;
        this.open_date = open_date;
        this.customer_id = customer_id;
        this.customer_name = customer_name;
        this.branch = branch;
        this.minor_indicator = minor_indicator;
    }

    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }

    public String getAccount_type() {
        return account_type;
    }

    public void setAccount_type(String account_type) {
        this.account_type = account_type;
    }

    public String getOpen_date() {
        return open_date;
    }

    public void setOpen_date(String open_date) {
        this.open_date = open_date;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getMinor_indicator() {
        return minor_indicator;
    }

    public void setMinor_indicator(String minor_indicator) {
        this.minor_indicator = minor_indicator;
    }
}


