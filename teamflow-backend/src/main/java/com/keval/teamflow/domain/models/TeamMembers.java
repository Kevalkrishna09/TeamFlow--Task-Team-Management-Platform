package com.keval.teamflow.domain.models;


import com.keval.teamflow.domain.enums.UserRole;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.concurrent.ThreadPoolExecutor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Setter
@Getter
@Entity
@NoArgsConstructor
@Table(
        name = "TeamMembers",
        uniqueConstraints = @UniqueConstraint(columnNames = {"team_id", "user_id"})
)

public class TeamMembers {

    public TeamMembers(User user ,Team team, UserRole role){
        this.user = user;
        this.role= role;
        this.team=team;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teamMembersId;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "team_id",nullable = false)
    private Team team;

    @Enumerated(EnumType.STRING)
    @NonNull
    private UserRole role;
}
