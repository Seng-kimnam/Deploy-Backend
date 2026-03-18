package _bbu.lawfirmapi.services.task;


import _bbu.lawfirmapi.models.DTO.cases.request.CaseRequest;
import _bbu.lawfirmapi.models.DTO.cases.response.CaseResponse;
import _bbu.lawfirmapi.models.DTO.task.request.TaskRequest;
import _bbu.lawfirmapi.models.DTO.task.response.TaskResponse;
import _bbu.lawfirmapi.models.Entity.Case;
import _bbu.lawfirmapi.models.Entity.Task;
import _bbu.lawfirmapi.models.Enumerations.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TaskService {

    Page<Task> getTaskList(Pageable pageable , Integer requestedPage);
    List<Task> filterTaskByStatus(TaskStatus status);
    Task getTaskById(Long taskId);
    TaskResponse createNewTask(TaskRequest taskRequest);

    TaskResponse modifiedTaskById(Long taskId , TaskRequest taskRequest);

    Void removeTaskById(Long taskId);

    List<Task> getTasksByCurrentLawyer(Long lawyerId);
    Page<Task> getTasksByCurrentLawyer(Long lawyerId, Pageable pageable);
}
