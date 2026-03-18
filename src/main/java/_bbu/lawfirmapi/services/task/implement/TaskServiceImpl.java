package _bbu.lawfirmapi.services.task.implement;

import _bbu.lawfirmapi.exceptions.NotFoundException;
import _bbu.lawfirmapi.exceptions.ResponseStatusException;
import _bbu.lawfirmapi.models.DTO.task.request.TaskRequest;
import _bbu.lawfirmapi.models.DTO.task.response.TaskResponse;
import _bbu.lawfirmapi.models.Entity.AppUser;
import _bbu.lawfirmapi.models.Entity.Case;
import _bbu.lawfirmapi.models.Entity.Task;
import _bbu.lawfirmapi.models.Enumerations.TaskStatus;
import _bbu.lawfirmapi.repositories.AppUserRepository;
import _bbu.lawfirmapi.repositories.CaseRepository;
import _bbu.lawfirmapi.repositories.TaskRepository;
import _bbu.lawfirmapi.services.task.TaskService;
import _bbu.lawfirmapi.utils.MethodHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepo;
    private final MethodHelper checkIsOutOfPage;
    private final CaseRepository caseRepo;
    private final AppUserRepository appUserRepo;


    @Override
    public Page<Task> getTaskList(Pageable pageable, Integer requestedPage) {

        Page<Task> tasks = taskRepo.findAll(pageable);
        if(tasks.isEmpty()){
            throw new NotFoundException("List Of Task not found.");
        }
        checkIsOutOfPage.isInvalidPage(tasks.getTotalPages() , requestedPage);
        return tasks;
    }
    @Override
    public Task getTaskById(Long taskId){
        Task task = taskRepo.findById(taskId)
                .orElseThrow(
                        () -> new NotFoundException("Task with id " + taskId + " not found.")
                );
        return task;
    }

    @Override
    public List<Task> filterTaskByStatus(TaskStatus status){

        List<Task> filteredTask = taskRepo.findTaskByStatus(status);
        return filteredTask;
    }
    @Override
    public TaskResponse createNewTask(TaskRequest taskRequest) {
        boolean isExisting = taskRepo.existsByLawyer_AppUserIdAndLegalCase_CaseId(
                taskRequest.getLawyerId(),
                taskRequest.getCaseId()
        );
        if(isExisting){
            throw new ResponseStatusException("cannot create new task. This task is already exist in the list.");
        }
        Task newTask = taskRequest.toEntity();
        Case caseForTask = caseRepo.findById(taskRequest.getCaseId())
                        .orElseThrow(
                                () -> new  NotFoundException("Cannot create new task because case id not found.")
                        );
        AppUser lawyerForTask = appUserRepo.findLawyerByAppUserId(taskRequest.getLawyerId())
                        .orElseThrow(
                                () -> new NotFoundException("Cannot create new task due lawyer with id " +
                                        taskRequest.getLawyerId() + " not found." )
                        );
        newTask.setLegalCase(caseForTask);
        newTask.setLawyer(lawyerForTask);
        newTask.setTitle(taskRequest.getTitle());
        newTask.setDescription(taskRequest.getDescription());
        newTask.setPriority(taskRequest.getTaskPriority());
        newTask.setStatus(taskRequest.getStatus());
        newTask.setStartedDate(taskRequest.getStartedDate());
        newTask.setDueDate(taskRequest.getDueDate());

        return taskRepo.save(newTask).toResponse();
    }

    @Override
    public TaskResponse modifiedTaskById(Long taskId, TaskRequest taskRequest) {

        boolean isExisting = taskRepo.existsByLawyer_AppUserIdAndLegalCase_CaseId(
                taskRequest.getLawyerId(),
                taskRequest.getCaseId()
        );
        if(isExisting){
            throw new ResponseStatusException("cannot edit this task. The task is already exist in the list.");

        }
        Task currentTask = getTaskById(taskId);
        Case caseForTask = caseRepo.findById(taskRequest.getCaseId())
                .orElseThrow(
                        () -> new  NotFoundException("Cannot create new task because case id not found.")
                );
        AppUser lawyerForTask = appUserRepo.findLawyerByAppUserId(taskRequest.getLawyerId())
                .orElseThrow(
                        () -> new NotFoundException("Cannot create new task due lawyer with id " +
                                taskRequest.getLawyerId() + " not found." )
                );
        currentTask.setLegalCase(caseForTask);
        currentTask.setLawyer(lawyerForTask);
        currentTask.setTitle(taskRequest.getTitle());
        currentTask.setDescription(taskRequest.getDescription());
        currentTask.setStatus(taskRequest.getStatus());
        currentTask.setPriority(taskRequest.getTaskPriority());
        currentTask.setStartedDate(taskRequest.getStartedDate());

        currentTask.setDueDate(taskRequest.getDueDate());

        TaskResponse savedUpatedTask = taskRepo.save(currentTask).toResponse();
        return savedUpatedTask;
    }

    @Override
    public Void removeTaskById(Long taskId) {
        if(!taskRepo.existsById(taskId)){
            throw new NotFoundException("Task with id " + taskId + " for deleting not found.");
        }
//        taskRepo.
        taskRepo.deleteById(taskId);
        return null;
    }

    @Override
    public List<Task> getTasksByCurrentLawyer(Long lawyerId) {
        if (lawyerId == null) {
            throw new NotFoundException("Lawyer ID is required");
        }
        if (!appUserRepo.existsById(lawyerId)) {
            throw new NotFoundException("Lawyer with ID " + lawyerId + " not found");
        }
        List<Task> tasks = taskRepo.findByLawyerAppUserId(lawyerId);
        if (tasks.isEmpty()) {
            throw new NotFoundException("No tasks found for this lawyer");
        }
        return tasks;
    }

    @Override
    public Page<Task> getTasksByCurrentLawyer(Long lawyerId, Pageable pageable) {
        if (lawyerId == null) {
            throw new NotFoundException("Lawyer ID is required");
        }
        if (!appUserRepo.existsById(lawyerId)) {
            throw new NotFoundException("Lawyer with ID " + lawyerId + " not found");
        }
        Page<Task> tasks = taskRepo.findByLawyerAppUserId(lawyerId, pageable);
        if (tasks.isEmpty()) {
            throw new NotFoundException("No tasks found for this lawyer");
        }
        return tasks;
    }
}
