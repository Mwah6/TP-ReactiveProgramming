package fr.mwahCorp.ReactiveProgramming.entities;

import java.time.Instant;

import org.springframework.data.annotation.Id;
//import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Document
@AllArgsConstructor @NoArgsConstructor @Data @ToString
public class Transaction {
	@Id
	private String id;
	private Instant instant;
	private double price;
//	@DBRef
	@JsonProperty(access = Access.WRITE_ONLY)
	private String societeID;

}
