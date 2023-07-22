package com.mballem.curso.jasper.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mballem.curso.jasper.spring.entity.Funcionario;
import com.mballem.curso.jasper.spring.repository.FuncionarioRepository;

import java.sql.Connection;

@Controller
public class HomeController {

    @Autowired
    private Connection connection;
    
    private FuncionarioRepository repository;
    
    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/conn")
    public String myConn(Model model) {
        model.addAttribute("conn", connection != null ? "Conexão Ok" : "Ops... sem conexão");
        return "index";
    }
    
    @GetMapping("/certificados")
    public String certificadoValidador(@RequestParam("cid") Long cid, Model model) {

    	Funcionario funcionario = this.repository.findById(cid).get();
        model.addAttribute("mensagem", "Confirmamos a veracidade deste certificado, pertencente a " + funcionario.getNome()
         + ", emitido em " + funcionario.getDataAdmissao());

    	return "index";
    }
    
    

}
