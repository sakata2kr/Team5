package gmfd;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="FoodCatalogView_table")
public class FoodCatalogView {

        @Id
//        @GeneratedValue(strategy=GenerationType.AUTO)
        private Long foodcatalogid;
        private Integer stock;
        private String name;
        private Double price;


        public Long getFoodcatalogid() {
            return foodcatalogid;
        }

        public void setFoodcatalogid(Long foodcatalogid) {
            this.foodcatalogid = foodcatalogid;
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
