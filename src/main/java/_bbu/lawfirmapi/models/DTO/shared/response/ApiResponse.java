package _bbu.lawfirmapi.models.DTO.shared.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T>  {

    private Boolean success;
    private String message;
    private HttpStatus status;
    private int code;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T payload;
    @Builder.Default
    private LocalDateTime timestamps = LocalDateTime.now();

}
