package com.keval.teamflow.domain.models;

import com.keval.teamflow.domain.enums.InvitationStatus;
import com.keval.teamflow.domain.enums.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import java.time.Instant;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@Entity
public class TeamInvitation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teamInvitationId;

    @Email
    private String email;

    @NonNull
    private String teamId;

    @NonNull
    @Column(unique = true)
    private String token;

    @NonNull
    private Instant expiryTime;

    private UserRole role;
    @Enumerated(EnumType.STRING)
    private InvitationStatus invitationStatus;
}
