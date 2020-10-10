package br.com.javamongo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import br.com.javamongo.model.Aluno;
import br.com.javamongo.model.Nota;
import br.com.javamongo.repository.AlunoRepository;

@Controller
public class NotaController {
	
	private AlunoRepository alunoRepository;
	
	
	@Autowired
	public NotaController(AlunoRepository alunoRepository) {
		this.alunoRepository = alunoRepository;
	}
	
	@GetMapping("/nota/salvar/{id}")
	public String cadastrar(@PathVariable String id, Model model) {
		Aluno aluno = alunoRepository.obterAlunoPor(id);
		model.addAttribute("aluno",aluno);
		model.addAttribute("nota",new Nota());
		return "notas/cadastrar";
		
	}

	@PostMapping("/nota/salvar/{id}")
	public String salvar(@PathVariable String id, @ModelAttribute Nota nota) {
	  Aluno aluno = alunoRepository.obterAlunoPor(id);
	  alunoRepository.salvar(aluno.adicionarNota(nota));
	  return "redirect:/aluno/listar";
	}
}
