package com.learners.orderservice.entity;

import com.learners.orderservice.model.OrderStatusEnum;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

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

    private OrderStatusEnum orderStatus = OrderStatusEnum.NEW;

    @Builder
    public Order(Long version, LocalDateTime creationDate, LocalDateTime lastModifiedDate,
                 UUID id, Customer customer, Set<OrderLine> orderLines, OrderStatusEnum orderStatus) {
        super(version, creationDate, lastModifiedDate);
        this.id = id;
        this.customer = customer;
        this.orderLines = orderLines;
        this.orderStatus = orderStatus;
    }
}
