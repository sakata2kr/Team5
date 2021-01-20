package gmfd;

public class DeliveryCanceled extends AbstractEvent {

    private Long id;
    private Long orderID;
    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getOrderId() {
        return orderID;
    }

    public void setOrderId(Long orderID) {
        this.orderID = orderID;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}