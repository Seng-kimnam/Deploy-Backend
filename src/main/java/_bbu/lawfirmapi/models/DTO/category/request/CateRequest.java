package _bbu.lawfirmapi.models.DTO.category.request;

import _bbu.lawfirmapi.models.Entity.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CateRequest {

    private String categoryName;

    public Category toEntity(){
        return new Category(
                null,
                this.categoryName
        );
    }
}
