package _bbu.lawfirmapi.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public  class ChartConstants {

    public   List<String> MONTH_CATEGORIES = List.of(
            "Jan", "Feb", "Mar", "Apr", "May", "Jun",
            "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    );

    public   List<String> QUARTER_CATEGORIES = List.of(
            "Q1", "Q2", "Q3", "Q4"
    );
}
