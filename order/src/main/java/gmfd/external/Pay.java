package gmfd.external;

public class Pay {

    private Long id;
    private Integer amout;
    private String status;
    private Long orderid;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Integer getAmout() {
        return amout;
    }
    public void setAmout(Integer amout) {
        this.amout = amout;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public Long getOrderid() {
        return orderid;
    }
    public void setOrderid(Long orderid) {
        this.orderid = orderid;
    }

}
