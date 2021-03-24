package fr.mwahCorp.ReactiveProgramming.web;

import java.time.Duration;
import java.time.Instant;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import fr.mwahCorp.ReactiveProgramming.dao.SocieteRepository;
import fr.mwahCorp.ReactiveProgramming.dao.TransactionRepository;
import fr.mwahCorp.ReactiveProgramming.entities.Societe;
import fr.mwahCorp.ReactiveProgramming.entities.Transaction;
import lombok.Data;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class TransactionReactiveRestController {
	@Autowired
	private TransactionRepository transactionRepository;
	@Autowired
	private SocieteRepository societeRepository;
	
	
//	Exemple :
//	@GetMapping(value ="/data", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
//	public Flux<Double> data(){
//		//RestTemplate est un objet de Sping qui permet d'établir une connexion http vers une API Rest
//		//Mais 'est impéaitf. D'où WebClient
//		WebClient webClient = WebClient.create("http://localhost:8082");
//		Flux<Double> result = webClient.get()
//				.uri("/events").accept(MediaType.APPLICATION_STREAM_JSON)
//				.retrieve().bodyToFlux(Event.class)
//				.map(d->d.getValue());
//		return result;
//	}

	
	@GetMapping(value = "/transactions")
	public Flux<Transaction> findAll(){
		return transactionRepository.findAll();
	}
	@GetMapping(value = "/transactions/{id}")
	public Mono<Transaction> getOne(@PathVariable String id){
		return transactionRepository.findById(id);
	}
	@PostMapping("/transactions")
	public Mono<Transaction> save (@RequestBody Transaction transaction) {
		return transactionRepository.save(transaction);

	}
	@DeleteMapping(value = "/transactions/{id}")
	public Mono<Void> delete(@PathVariable String id){
		return transactionRepository.deleteById(id);
	}
	@PutMapping(value = "/transactions/{id}")
	public Mono<Transaction> update(@RequestBody Transaction transaction, @PathVariable String id){
		transaction.setId(id);
		return transactionRepository.save(transaction);
	}
	@GetMapping(value = "/streamTransactions", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	// par défaut, c'est un "produces = MediaType.APPLICATION_JSON_VALUE"
	public Flux<Transaction> streamTransactions(){
		return transactionRepository.findAll();
	}
	@GetMapping(value = "/transactionsBySociete/{id}")
	public Flux<Transaction> transactionsBySociete(@PathVariable String id){
		//		Societe societe = new Societe();
		//		societe.setId(id);
		return transactionRepository.findBySocieteID(id);
	}
	//	@GetMapping(value = "/streamTransactionsBySociete/{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	//	public Flux<Transaction> StreamTransactionsBySociete(@PathVariable String id){
	//
	//		return transactionRepository.findBySocieteID(id);
	//	}
	@GetMapping(value = "/streamTransactionsBySociete/{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<Transaction> streamTransactionsBySociete(@PathVariable String id){
		return societeRepository.findById(id)
				//flat-> lie les tableaux entre eux many-> passe du mono au flux
				.flatMapMany(soc->{
					//Création d'un Flux qui génère pour chaque seconde
					Flux<Long> interval = Flux.interval(Duration.ofMillis(1000));
					Flux<Transaction> transactionFlux = Flux.fromStream(Stream.generate(()->{
						Transaction transaction = new Transaction();
						transaction.setInstant(Instant.now());
						transaction.setSocieteID(soc.getId());
						transaction.setPrice(soc.getPrice()+(1+(Math.random()*12-6)/100));
						return transaction;
					}));
					//.zip() Combine les deux flux
					return Flux.zip(interval, transactionFlux)
							.map(data->{
								return data.getT2();
								//share => partage : une même demande reçoit les même données
							}).share();
				});
	}
	
	@GetMapping(value = "/events/{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<Double> events (@PathVariable String id){
		//RestTemplate est un objet de Sping qui permet d'établir une connexion http vers une API Rest
		//Mais 'est impéaitf. D'où WebClient
		WebClient webClient = WebClient.create("http://localhost:8082");
		Flux<Double> eventFlux = webClient.get()
				.uri("/streamEvents/" + id)
				.retrieve().bodyToFlux(Event.class)
				.map(data->data.getValue());
		return eventFlux;
	}
	@GetMapping("/test")
	public String test() {
		return Thread.currentThread().getName();
	}
}
@Data
class Event {
	private Instant instant;
	private double value;
	private String societeID;
}