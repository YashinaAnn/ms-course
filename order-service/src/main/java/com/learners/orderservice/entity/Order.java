package com.learners.orderservice.entity;

import com.learners.model.OrderStatus;
import lombok.*;
import org.hibernate.annotations.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name = "t_order")
@EqualsAndHashCode(callSuper = true, of = {"id", "customer", "orderLines", "orderStatus"})
@NoArgsConstructor
@AllArgsConstructor
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Type(type="org.hibernate.type.UUIDCharType")
    @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne
    private Customer customer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @Fetch(FetchMode.JOIN)
    private Set<OrderLine> orderLines;

    private OrderStatus orderStatus = OrderStatus.NEW;

    @Builder
    public Order(Long version, LocalDateTime creationDate, LocalDateTime lastModifiedDate,
                 UUID id, Customer customer, Set<OrderLine> orderLines, OrderStatus orderStatus) {
        super(version, creationDate, lastModifiedDate);
        this.id = id;
        this.customer = customer;
        this.orderLines = orderLines;
        this.orderStatus = orderStatus;
    }
}
