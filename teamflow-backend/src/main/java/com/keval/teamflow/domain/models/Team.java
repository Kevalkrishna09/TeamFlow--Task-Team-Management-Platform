package com.keval.teamflow.domain.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

@NoArgsConstructor
@Entity
@Getter
@Setter
@Table
public class Team {
    public Team(String teamId,String name,String description){
        this.name = name;
        this.description= description;
        this.teamId = teamId;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String teamId;

    @NotBlank(message = "ClientConfigId is empty")
    private String name;

    private String description;

    @CreationTimestamp
    private Instant createdDate;


}
