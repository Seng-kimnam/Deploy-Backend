package _bbu.lawfirmapi.controllers;

import _bbu.lawfirmapi.models.DTO.shared.response.ApiResponse;
import _bbu.lawfirmapi.models.DTO.shared.response.BaseResponse;
import _bbu.lawfirmapi.models.DTO.task.request.TaskRequest;
import _bbu.lawfirmapi.models.DTO.task.response.TaskResponse;
import _bbu.lawfirmapi.models.Entity.Task;
import _bbu.lawfirmapi.models.Enumerations.TaskStatus;
import _bbu.lawfirmapi.services.admin.AdminService;
import _bbu.lawfirmapi.services.task.TaskService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tasks")
public class TaskController extends BaseResponse {

    private final TaskService taskService;
    private final AdminService adminService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<Task>>> getAllTaskList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "taskId") String sortBy,
            @RequestParam(defaultValue = "true") Boolean ascending
    ){
        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page -1 , size , sort);
        Page<Task> tasksList = taskService.getTaskList(pageable , page);
        return responseEntity(true,
                "Getting task list successfully.",
                HttpStatus.OK,
                tasksList);
     }

    @GetMapping("/my-tasks/{lawyerId}")
    public ResponseEntity<ApiResponse<Page<Task>>> getMyTasks(
            @PathVariable Long lawyerId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "taskId") String sortBy,
            @RequestParam(defaultValue = "true") Boolean ascending
    ) {
//        Long lawyerId = adminService.getCurrentAdminEntity().getAppUserId();
        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<Task> tasks = taskService.getTasksByCurrentLawyer(lawyerId, pageable);
        return responseEntity(true,
                "Get my tasks successfully.",
                HttpStatus.OK,
                tasks);
    }

     @GetMapping("/filter")
     public ResponseEntity<ApiResponse<List<Task>>> filterTask(

             @RequestParam TaskStatus taskStatus

             ){
         return responseEntity(true,
                 "Get task with status" + taskStatus +" successfully.",
                 HttpStatus.CREATED,
                 taskService.filterTaskByStatus(taskStatus));
     }
     @GetMapping("/{taskId}")
    public ResponseEntity<ApiResponse<Task>> getTaskById(

             @PathVariable @Positive @Valid Long taskId

             ){
        return responseEntity(true,
                "Get task with id " + taskId +" successfully.",
                HttpStatus.CREATED,
                taskService.getTaskById(taskId));
    }
     @PostMapping
    public ResponseEntity<ApiResponse<TaskResponse>> createNewTask(@RequestBody TaskRequest taskRequest){
        return responseEntity(true,
                "Create new task with title " + taskRequest.getTitle() + " successfully.",
                HttpStatus.CREATED,
                taskService.createNewTask(taskRequest));
     }
     @PutMapping("/{taskId}")
     public ResponseEntity<ApiResponse<TaskResponse>> updateExistingTaskById(
             @PathVariable @Positive @Valid Long taskId,
             @RequestBody TaskRequest taskRequest
     ){
         return responseEntity(true,
                 "Update task successfully.",
                 HttpStatus.ACCEPTED,
                 taskService.modifiedTaskById(taskId , taskRequest));
     }

     @DeleteMapping("/{taskId}")
    public ResponseEntity<ApiResponse<Void>> deleteTaskById(
             @PathVariable @Positive @Valid Long taskId
     ){
        return responseEntity(true,
                "Delete existing task successfully.",
                HttpStatus.ACCEPTED,
                taskService.removeTaskById(taskId));
    }
}
