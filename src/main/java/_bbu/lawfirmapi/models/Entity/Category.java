package _bbu.lawfirmapi.models.Entity;

import _bbu.lawfirmapi.models.DTO.category.response.CateResponse;
import _bbu.lawfirmapi.utils.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "categories")
@JsonPropertyOrder({"categoryId" , "categoryName" , "createdAt" , "updatedAt"})
public class Category extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;
    @Column(name = "category_name" , unique = true)
    private String categoryName;
    
    @OneToMany(mappedBy = "category" , cascade = CascadeType.ALL)
//    @JsonIgnore
    @JsonManagedReference
    private List<Document> documents;

    public Category(Object o, String categoryName) {
    }

    public CateResponse toResponse(){
       return  new CateResponse(
                this.categoryId,
               this.categoryName,
               this.getCreatedAt(),
               this.getUpdatedAt()
        );
    }
    

}
