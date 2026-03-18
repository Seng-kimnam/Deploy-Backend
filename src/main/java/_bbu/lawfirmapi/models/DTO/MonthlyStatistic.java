package _bbu.lawfirmapi.models.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class MonthlyStatistic {
    private Integer month;
    private Long total;

}
