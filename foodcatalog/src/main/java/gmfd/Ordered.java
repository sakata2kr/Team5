package gmfd;

public class Ordered extends AbstractEvent {

    private Long id;
    private Integer qty;
    private String status;
    private Long foodcaltalogid;
    private Long customerid;

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