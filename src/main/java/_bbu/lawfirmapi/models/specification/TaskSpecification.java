package _bbu.lawfirmapi.models.specification;

import _bbu.lawfirmapi.repositories.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskSpecification {
    private final TaskRepository taskRepository;


}
