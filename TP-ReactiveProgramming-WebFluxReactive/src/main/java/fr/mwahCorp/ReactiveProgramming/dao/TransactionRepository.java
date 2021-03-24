package fr.mwahCorp.ReactiveProgramming.dao;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;


import fr.mwahCorp.ReactiveProgramming.entities.Transaction;
import reactor.core.publisher.Flux;


public interface TransactionRepository extends ReactiveMongoRepository<Transaction, String>{

	public Flux<Transaction> findBySocieteID(String societeID);
}
