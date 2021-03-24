package fr.mwahCorp.ReactiveProgramming;

import java.time.Instant;
import java.util.stream.Stream;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import fr.mwahCorp.ReactiveProgramming.dao.SocieteRepository;
import fr.mwahCorp.ReactiveProgramming.dao.TransactionRepository;
import fr.mwahCorp.ReactiveProgramming.entities.Societe;
import fr.mwahCorp.ReactiveProgramming.entities.Transaction;

@SpringBootApplication
public class TpReactiveProgrammingWebFluxReactiveApplication {

	public static void main(String[] args) {
		SpringApplication.run(TpReactiveProgrammingWebFluxReactiveApplication.class, args);

	}
	@Bean
	CommandLineRunner start(SocieteRepository societeRepository, TransactionRepository transactionRepository) {
		return args -> {
			societeRepository.deleteAll().subscribe(null, null, () -> {
				Stream.of("SG", "AWB", "BMCE", "AXA").forEach(s-> {
					societeRepository.save(new Societe(s, s, 100 +Math.random()*900))
					// subscribe Attend jusqu'à ce que l'opération soit faite. retourne la société ajoutée
					.subscribe(soc -> {
						System.out.println(soc.toString());
					}); 
				});
			
			transactionRepository.deleteAll().subscribe(null, null, () -> {
				Stream.of("SG", "AWB", "BMCE", "AXA").forEach(s-> {
					societeRepository.findById(s)
					.subscribe(soc -> {
						for (int i = 0; i < 10; i++) {
							Transaction transaction = new Transaction();
							transaction.setInstant(Instant.now());
							transaction.setSocieteID(soc.getId());
							transaction.setPrice(soc.getPrice()+(1+(Math.random()*12-6)/100));
							transactionRepository.save(transaction).subscribe(t->{
								System.out.println(t.toString());
							});
						}
					});
				});

				});

			});
		};

	}

}
