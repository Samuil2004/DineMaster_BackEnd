package nl.fontys.s3.dinemasterbackend.persistence.entity;
import jakarta.persistence.*;
import lombok.*;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="addresses")
public class AddressEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", nullable = false)
    private CustomerEntity customerDetails;

    @Column(name = "street",nullable=false)
    private String street;

    @Column(name = "city",nullable=false)
    private String city;

    @Column(name = "postal_code",nullable=false)
    private String postalCode;

    @Column(name = "country",nullable=false)
    private String country;
}

