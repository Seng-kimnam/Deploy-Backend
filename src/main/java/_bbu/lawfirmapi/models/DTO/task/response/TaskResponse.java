package _bbu.lawfirmapi.models.DTO.task.response;

import _bbu.lawfirmapi.models.Entity.AppUser;
import _bbu.lawfirmapi.models.Entity.Case;
import _bbu.lawfirmapi.models.Enumerations.TaskPriority;
import _bbu.lawfirmapi.models.Enumerations.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TaskResponse {
    private Long taskId;
    private Case legalCase;
    private AppUser lawyer ;
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private LocalDateTime  startedDate;
    private LocalDateTime dueDate;
}
