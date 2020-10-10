package br.com.javamongo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.javamongo.model.Aluno;
import br.com.javamongo.repository.AlunoRepository;

@Controller
public class AlunoController {

	private AlunoRepository alunoRepository;

	@Autowired
	public AlunoController(AlunoRepository alunoRepository) {
		this.alunoRepository = alunoRepository;
	}

	@GetMapping("/aluno/cadastrar")
	public String cadastrar(Model model) {
		model.addAttribute("aluno", new Aluno());
		return "aluno/cadastrar";
	}

	@PostMapping("/aluno/salvar")
	public String salvar(@ModelAttribute Aluno aluno) {
		alunoRepository.salvar(aluno);
		return "redirect:/";
	}

	@GetMapping("/aluno/listar")
	public String listar(Model model) {
		List<Aluno> alunos = alunoRepository.listar();
		model.addAttribute("alunos", alunos);

		return "aluno/listar";
	}

	@GetMapping("/aluno/visualizar/{id}")
	public String visualizar(@PathVariable String id, Model model) {
		Aluno aluno = alunoRepository.obterAlunoPor(id);
		model.addAttribute("aluno", aluno);
		return "aluno/visualizar";
	}

	@RequestMapping(method = RequestMethod.GET, path = "/aluno/pesquisarnome")
	public String buscarPorNome(Model model) {

		return "aluno/pesquisarnome";
	}

//	@GetMapping("/aluno/pesquisar")
//	public String pesquisar(@RequestParam String nome, Model model) { 
//		List<Aluno> 	alunos = alunoRepository.obterAlunosPor(nome); model.addAttribute("alunos",
//	alunos); return "aluno/pesquisarnome";
//	}

	@RequestMapping(value = "/aluno/pesquisar",method = RequestMethod.GET)
	public String pesquisar(@RequestParam String nome, Model model) {
		List<Aluno> alunos = alunoRepository.obterAlunosPor(nome);
		model.addAttribute("alunos", alunos);
		return "aluno/pesquisarnome";
	}

}
