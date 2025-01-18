package nl.fontys.s3.dinemasterbackend.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
@EqualsAndHashCode
public abstract class UserEntity {
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @EqualsAndHashCode.Include
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @EqualsAndHashCode.Include
    @Column(name = "last_name", nullable = false)
    private String lastName;
    
    @EqualsAndHashCode.Include
    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @EqualsAndHashCode.Include
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private RoleEntity role;

    @EqualsAndHashCode.Include
    @Column(name = "password", nullable = false)
    private String password;
}
