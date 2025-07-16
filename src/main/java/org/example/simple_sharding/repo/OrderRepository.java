package org.example.simple_sharding.repo;

import org.example.simple_sharding.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
