package fr.mwahCorp.ReactiveProgramming.dao;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import fr.mwahCorp.ReactiveProgramming.entities.Societe;

public interface SocieteRepository extends ReactiveMongoRepository<Societe, String>{

}
