package _bbu.lawfirmapi.models.DTO.category.response;


import _bbu.lawfirmapi.utils.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class CateResponse   {

    private Long categoryId;

    private String categoryName;

    private LocalDateTime createAt;

    private LocalDateTime updatedAt;


}
