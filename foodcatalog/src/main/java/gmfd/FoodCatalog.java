package gmfd;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.List;

@Entity
@Table(name="FoodCatalog_table")
public class FoodCatalog {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private Integer stock;
    private String name;
    private Double price;

    @PostPersist
    public void onPostPersist(){
        CatalogRegistered catalogRegistered = new CatalogRegistered();
        BeanUtils.copyProperties(this, catalogRegistered);
        catalogRegistered.publishAfterCommit();


    }

    @PreUpdate
    public void onPreUpdate(){
        CatalogUpdated catalogUpdated = new CatalogUpdated();
        BeanUtils.copyProperties(this, catalogUpdated);
        catalogUpdated.publishAfterCommit();


    }

    @PreRemove
    public void onPreRemove(){
        CatalogDeleted catalogDeleted = new CatalogDeleted();
        BeanUtils.copyProperties(this, catalogDeleted);
        catalogDeleted.publishAfterCommit();


    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }




}
