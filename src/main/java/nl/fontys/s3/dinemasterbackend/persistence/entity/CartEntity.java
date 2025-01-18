package nl.fontys.s3.dinemasterbackend.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "carts")
public class CartEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    @EqualsAndHashCode.Include
    private Long cartId;

    @Column(name = "customer_id", nullable = false)
    //@EqualsAndHashCode.Include
    private Long customerId;

    @Column(name = "price", nullable = false)
    private Double price;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    @EqualsAndHashCode.Include
    private List<SelectedItemEntity> selectedItemEntities;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;
}
