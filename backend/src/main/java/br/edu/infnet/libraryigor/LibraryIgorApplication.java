package br.edu.infnet.libraryigor;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@WebServlet("/api/*")
@EnableScheduling // listener de calendário para executar rotina diária
@ServletComponentScan // listener para a configuração do CORS origin (se estiver executando localmente o Spring Boot e Angular a partir de portas diferentes)
@EnableFeignClients // fazer chamadas para outras APIs
    public class LibraryIgorApplication extends HttpServlet {
    public static void main(String[] args) {
        SpringApplication.run(LibraryIgorApplication.class, args);
    }
}