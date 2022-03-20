package com.learners.orderservice.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, of = {"id", "pizzaId", "orderQty", "qtyAllocated"})
@ToString(callSuper = true, of = {"id", "pizzaId", "orderQty", "qtyAllocated"})
public class OrderLine extends BaseEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Type(type="org.hibernate.type.UUIDCharType")
    @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
    private UUID id;

    private Long pizzaId;
    private String upc;

    @ManyToOne
    private Order order;

    private Integer orderQty = 0;
    private Integer qtyAllocated = 0;

    @Builder
    public OrderLine(Long version, LocalDateTime creationDate, LocalDateTime lastModifiedDate,
                     UUID id, Long pizzaId, Order order, Integer orderQty, Integer qtyAllocated) {
        super(version, creationDate, lastModifiedDate);
        this.id = id;
        this.pizzaId = pizzaId;
        this.order = order;
        this.orderQty = orderQty;
        this.qtyAllocated = qtyAllocated;
    }
}
