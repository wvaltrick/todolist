package br.com.rocketseat.todolist.task;

import java.time.LocalDateTime;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.rocketseat.todolist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/tasks")
public class TaskController {
    
    @Autowired //Spring gerencia a estancia do repositório
    private ITaskRepository taskRepository;

    @PostMapping("/") //para criação
    public ResponseEntity create (@RequestBody TaskModel taskModel, HttpServletRequest request) {

        System.out.println("Chegou no Controller " + request.getAttribute("idUser"));
        var idUser = request.getAttribute("idUser");
        taskModel.setIdUser((UUID) idUser);

        var currentDate = LocalDateTime.now();  //validação de data
        if(currentDate.isAfter(taskModel.getInicioEm()) || currentDate.isAfter(taskModel.getFimEm())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data de início ou término deve ser maior que a data atual");
        }

        if(taskModel.getInicioEm().isAfter(taskModel.getFimEm())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data de término deve ser maior que a data de início");
        }

        var task = this.taskRepository.save(taskModel);
        return ResponseEntity.status(HttpStatus.OK).body(task);
    }

    @GetMapping("/")
    public java.util.List<TaskModel> list(HttpServletRequest request) {
        var idUser = request.getAttribute("idUser"); //traz o ID do usuário
        var tasks = this.taskRepository.findByIdUser((UUID) idUser); //busca as tarefas pelo ID do usuário
        return tasks;
    }
    
    @PutMapping("/{id}")    //efetua a Update da task
    public ResponseEntity update(@RequestBody TaskModel taskModel, HttpServletRequest request, @PathVariable UUID id){ //passa o id da task direto na url
        var idUser = request.getAttribute("idUser");
        var task = this.taskRepository.findById(id).orElse(null);
        
        if(task == null){
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tarefa não encontrada");
        }

        if (!task.getIdUser().equals(idUser)){ //verifica se o user é o dono da task que está atualizando
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário não tem permissão para alterar essa tarefa");
        }


        Utils.copyNonNullProperties(taskModel, task);
        
        var taskUpdated = this.taskRepository.save(task);
        return ResponseEntity.ok().body(taskUpdated);
    }
}
