package _bbu.lawfirmapi.models.DTO.shared.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChartResponse {
    private String period;
    private Integer year ;
    private List<String> categories;
    private List<Integer> data;
}
