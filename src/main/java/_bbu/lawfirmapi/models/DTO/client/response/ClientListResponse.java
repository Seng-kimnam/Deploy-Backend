package _bbu.lawfirmapi.models.DTO.client.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ClientListResponse {
    private String email;
    private String clientName;
    private Long requestCount;
}
