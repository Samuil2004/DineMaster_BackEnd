package nl.fontys.s3.dinemasterbackend.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "orders")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;

    //@ManyToOne(cascade = CascadeType.ALL)
    @ManyToOne
    @JoinColumn(name = "cart_id")
    private CartEntity cartEntity;

    @Column(name = "comments")
    private String comments;

    @ManyToOne
    @JoinColumn(name = "order_status_id")
    private OrderStatusEntity orderStatus;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private CustomerEntity customerEntity;

    @Column(name = "delivery_fee", nullable = false)
    private Double deliveryFee;

    @Column(name = "is_taken", nullable = false) // Maps to a column in the table
    private Boolean isTaken; // New field

}