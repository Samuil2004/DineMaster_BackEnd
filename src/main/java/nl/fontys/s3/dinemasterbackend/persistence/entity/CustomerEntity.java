package nl.fontys.s3.dinemasterbackend.persistence.entity;

import jakarta.persistence.*;
import jakarta.persistence.Column;
import lombok.*;
import lombok.experimental.SuperBuilder;


@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Setter
@Table(name = "customers")
@Inheritance(strategy = InheritanceType.JOINED)
@EqualsAndHashCode
public class CustomerEntity extends UserEntity {
    @EqualsAndHashCode.Include
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;


    @OneToOne(mappedBy = "customerDetails", cascade = CascadeType.ALL, orphanRemoval = true)
    private AddressEntity address;

}