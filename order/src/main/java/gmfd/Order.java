package gmfd;

import javax.persistence.*;
import javax.swing.text.html.Option;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Entity
@Table(name="Order_table")
public class Order {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private Integer qty;
    private String status;
    private Long foodcaltalogid;
    private Long customerid;

    @PostPersist
    public void onPostPersist() throws InterruptedException {

        Ordered ordered = new Ordered();
        BeanUtils.copyProperties(this, ordered);

        ordered.setId(this.getId());
        ordered.setCustomerid(this.getCustomerid());
        ordered.setFoodcaltalogid(this.getFoodcaltalogid());
        ordered.setQty(this.getQty());
        ordered.setStatus("Ordered");

        ordered.publish();


        //TimeUnit.SECONDS.sleep(1);

        //Following code causes dependency to external APIs
        // it is NOT A GOOD PRACTICE. instead, Event-Policy mapping is recommended.

        gmfd.external.Pay pay = new gmfd.external.Pay();
        // mappings goes here
        pay.setOrderid(this.getId());
        pay.setStatus("Ordered");
        pay.setAmout(this.getQty());


        OrderApplication.applicationContext.getBean(gmfd.external.PayService.class)
                .pay(pay);

    }



    @PostUpdate
    public void onPostUpdate(){

    }


    @PreRemove
    public void onPreRemove(){
        OrderCancelled orderCancelled = new OrderCancelled();
        BeanUtils.copyProperties(this, orderCancelled);
        orderCancelled.publishAfterCommit();

        //Following code causes dependency to external APIs
        // it is NOT A GOOD PRACTICE. instead, Event-Policy mapping is recommended.

        gmfd.external.Cancellation cancellation = new gmfd.external.Cancellation();
        // mappings goes here
        cancellation.setOrderId(this.getId());
        cancellation.setStatus("Cancelled");



        OrderApplication.applicationContext.getBean(gmfd.external.CancellationService.class)
                .cancelShip(cancellation);


    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public Long getFoodcaltalogid() {
        return foodcaltalogid;
    }

    public void setFoodcaltalogid(Long foodcaltalogid) {
        this.foodcaltalogid = foodcaltalogid;
    }
    public Long getCustomerid() {
        return customerid;
    }

    public void setCustomerid(Long customerid) {
        this.customerid = customerid;
    }




}
