package gmfd;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.List;

@Entity
@Table(name="Customer_table")
public class Customer {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String name;
    private String phone;
    private String address;
    private Integer age;

    @PostPersist
    public void onPostPersist(){
        Registerd registerd = new Registerd();
        BeanUtils.copyProperties(this, registerd);
        registerd.publishAfterCommit();


        gmfd.external.CustomerHist customerHist = new gmfd.external.CustomerHist();
        // mappings goes here
        customerHist.setCustomerid(this.getId());
        customerHist.setName(this.getName());
        customerHist.setAddress(this.getAddress());
        customerHist.setAge(this.getAge());
        customerHist.setPhone(this.getPhone());
        customerHist.setStatus("NEW");

        CustomerApplication.applicationContext.getBean(gmfd.external.CustomerHistService.class)
                .checkValid(customerHist);


    }

    @PreUpdate
    public void onPreUpdate(){
        Updated updated = new Updated();
        BeanUtils.copyProperties(this, updated);
        updated.publishAfterCommit();


    }

    @PreRemove
    public void onPreRemove(){
        Withdrawed withdrawed = new Withdrawed();
        BeanUtils.copyProperties(this, withdrawed);
        withdrawed.publishAfterCommit();


    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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




}
