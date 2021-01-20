package gmfd.external;

public class MyOrder {

    private Long id;
    private Long orderid;
    private Long foodcatlogid;
    private Integer qty;
    private Long deliveryid;
    private String status;
    private Long customerid;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getOrderid() {
        return orderid;
    }

    public void setOrderid(Long orderid) {
        this.orderid = orderid;
    }
    public Long getFoodcatlogid() {
        return foodcatlogid;
    }

    public void setFoodcatlogid(Long foodcatlogid) {
        this.foodcatlogid = foodcatlogid;
    }
    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }
    public Long getDeliveryid() {
        return deliveryid;
    }

    public void setDeliveryid(Long deliveryid) {
        this.deliveryid = deliveryid;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public Long getCustomerid() {
        return customerid;
    }

    public void setCustomerid(Long customerid) {
        this.customerid = customerid;
    }
}
