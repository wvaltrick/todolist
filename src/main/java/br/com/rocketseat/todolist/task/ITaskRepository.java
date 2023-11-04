package br.com.rocketseat.todolist.task;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface ITaskRepository extends JpaRepository<TaskModel, UUID>{

    List<TaskModel> findByIdUser(UUID idUser); //traz uma lista de TaskModel baseado no id do usuário inserido

   
}
