package _bbu.lawfirmapi.models.DTO.task.request;

import _bbu.lawfirmapi.models.Entity.Task;
import _bbu.lawfirmapi.models.Enumerations.TaskPriority;
import _bbu.lawfirmapi.models.Enumerations.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskRequest {
    private Long caseId;
    private Long lawyerId;
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority taskPriority;
    private LocalDateTime startedDate;
    private LocalDateTime dueDate;
    public Task toEntity(){
        return new Task(
                null ,
                this.caseId,
                this.lawyerId,
                this.title,
                this.description,
                this.status,
                this.taskPriority,
                this.startedDate,
                this.dueDate
        );
    }

}
