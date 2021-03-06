package fr.mwahCorp.ReactiveProgramming.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.mwahCorp.ReactiveProgramming.dao.SocieteRepository;
import fr.mwahCorp.ReactiveProgramming.entities.Societe;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class SocieteReactiveRestController {
	@Autowired
	private SocieteRepository societeRepository;
	
	@GetMapping(value = "/societes")
	public Flux<Societe> findAll(){
		return societeRepository.findAll();
	}
	@GetMapping(value = "/societes/{id}")
	public Mono<Societe> getOne(@PathVariable String id){
		return societeRepository.findById(id);
	}
	@PostMapping("/societes")
	public Mono<Societe> save (@RequestBody Societe societe) {
		return societeRepository.save(societe);
		
	}
	@DeleteMapping(value = "/societes/{id}")
	public Mono<Void> delete(@PathVariable String id){
		return societeRepository.deleteById(id);
	}
	@PutMapping(value = "/societes/{id}")
	public Mono<Societe> update(@RequestBody Societe societe, @PathVariable String id){
		societe.setId(id);
		return societeRepository.save(societe);
	}
}
