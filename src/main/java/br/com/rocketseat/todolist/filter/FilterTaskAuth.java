package br.com.rocketseat.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.rocketseat.todolist.user.IUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component // para o spring poder gerenciar
public class FilterTaskAuth extends OncePerRequestFilter{

    @Autowired
    private IUserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

                var servletPath = request.getServletPath();
                
                if (servletPath.startsWith("/tasks/")){     //se o servlet iniciar com /task segue para autenticacao
                 
                // Pegar a autenticacao (usuario e senha)
                var authorization = request.getHeader("Authorization");
               
                var authEncoded = authorization.substring("Basic".length()).trim(); // remove a palavra Basic, utilizando apenas a codificacao da senha

                byte[] authDecode = Base64.getDecoder().decode(authEncoded); // converte para um array de byte

                var authString = new String(authDecode);

                String[] credentials = authString.split(":"); //o user e a senha vem como: "user:senha", ele vai quebrar com split e deixar em dois indexs 0 e 1
                String username = credentials[0];
                String password = credentials[1];

                System.out.println("Autorização:");
                System.out.println(username);
                System.out.println(password);

                // validar usuário

                    var user = userRepository.findByUsername(username);
                    if (user == null){
                        response.sendError(401, "Usuário não autorizado");
                    } else {
                        // validar senha
                        var passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
                        
                        if(passwordVerify.verified){
                            request.setAttribute("idUser", user.getId());  //busca o ID do usuário que autenticou
                            filterChain.doFilter(request, response);
                        } else {
                            response.sendError(401);
                        }
                    }

                } else {        // segue em frente
                    filterChain.doFilter(request, response);
                }
                     
    }
    
}

