package fr.mwahCorp.ReactiveProgramming.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import fr.mwahCorp.ReactiveProgramming.dao.SocieteRepository;
import fr.mwahCorp.ReactiveProgramming.dao.TransactionRepository;

@Controller
public class WebMVCController {
	private SocieteRepository societeRepository;
	private TransactionRepository transactionRepository;

	//Injection des dépendances
	public WebMVCController(SocieteRepository societeRepository, TransactionRepository transactionRepository) {
		this.societeRepository = societeRepository;
		this.transactionRepository = transactionRepository;
	}
	@GetMapping("/index")
	public String index(Model model) {
		//Onn envoie des Flux au model (c'est une promesse).Thymeleaf s'adapte sans se poser de questions
		model.addAttribute("societes", societeRepository.findAll());
		return "index";
	}
}
