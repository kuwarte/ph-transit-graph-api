package com.phtransitgraph.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "operators")
public class Operator {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "operator_name", nullable = false)
    private String operatorName;

    @Column(name = "franchise_no", unique = true)
    private String franchiseNo;

    @Column(name = "contact_number")
    private String contactNumber;

    @Column(name = "is_verified", nullable = false)
    private boolean verified;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        verified = false;
        createdAt = LocalDateTime.now();
    }
}
