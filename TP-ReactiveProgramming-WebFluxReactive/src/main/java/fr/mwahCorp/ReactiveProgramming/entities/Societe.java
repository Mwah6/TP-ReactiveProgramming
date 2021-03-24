package fr.mwahCorp.ReactiveProgramming.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Document
@AllArgsConstructor @NoArgsConstructor @Data @ToString
public class Societe {
	@Id
	private String id;
	private String name;
	private double price;
	

}
