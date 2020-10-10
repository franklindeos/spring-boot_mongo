package br.com.javamongo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import br.com.javamongo.model.Aluno;
import br.com.javamongo.model.Habilidade;
import br.com.javamongo.repository.AlunoRepository;

@Controller
public class HabilidadeController {
	
	private AlunoRepository alunoRepository;
	
	@Autowired
	public HabilidadeController(AlunoRepository alunoRepository) {
		this.alunoRepository = alunoRepository;
	}

	@GetMapping("/habilidade/salvar/{id}")
	public String cadastrar(@PathVariable String id, Model model) {
		Aluno aluno = alunoRepository.obterAlunoPor(id);
		model.addAttribute("aluno", aluno);
		model.addAttribute("habilidade", new Habilidade());
		return "habilidades/cadastrar";
	}
	
	@PostMapping("/habilidade/salvar/{id}")
	public String salvar(@PathVariable String id, @ModelAttribute Habilidade habilidade) {
		Aluno aluno = alunoRepository.obterAlunoPor(id);
		aluno.adicionaHabilidade(habilidade);
		alunoRepository.salvar(aluno);
		return "redirect:/aluno/listar";
	}

}
