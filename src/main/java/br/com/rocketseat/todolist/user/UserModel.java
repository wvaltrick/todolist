package br.com.rocketseat.todolist.user;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data                            //gerador de getters e setter
@Entity(name = "tb_users")         //cria a tabela no DB
public class UserModel {

    @Id     //chave primaria
    @GeneratedValue(generator = "UUID") //gerador automático de ID
    private UUID id;

    @Column (unique = true) //atribui valor único no DB
    private String username;
    private String name;
    private String password;

    @CreationTimestamp 
    private LocalDateTime createdAt;
}
