package br.com.rocketseat.todolist.task;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

    /**
     * ID
     * Usuário (ID_USUARIO)
     * Descrição
     * Título
     * Data de Início
     * Data de término
     * Prioridade
     */
@Data
@Entity(name = "tb_task") //cria uma tabela
public class TaskModel {

    @Id
    @GeneratedValue(generator = "UUID")
     private UUID id;
     private String descricao;

     @Column(length = 50)
     private String titulo;
     private LocalDateTime inicioEm;
     private LocalDateTime fimEm;
     private String prioridade;

     private UUID idUser;
     
     @CreationTimestamp
     private LocalDateTime criadoEm;


     public void setTitulo(String titulo) throws Exception{
        if(titulo.length() > 50){
            throw new Exception("O campo titulo deve conter no máximo 50 caracteres");
        }
        this.titulo = titulo;
     }
     
}
