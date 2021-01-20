package gmfd;

import javax.persistence.*;

import org.springframework.beans.BeanUtils;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Entity
@Table(name="CustomerHist_table")
public class CustomerHist {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private Long customerid;
    private String name;
    private String phone;
    private String address;
    private Integer age;
    private String status;

    @PostPersist
    public void onPostPersist(){
        CheckValid checkValid = new CheckValid();
        BeanUtils.copyProperties(this, checkValid);

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCommit( ) {
                checkValid.publishAfterCommit();
            }
        });

        try {
            Thread.currentThread().sleep((long) (3 * 1000));  // 일괄적으로 3초간 Timeout 유도

        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getCustomerid() {
        return customerid;
    }

    public void setCustomerid(Long customerid) {
        this.customerid = customerid;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
