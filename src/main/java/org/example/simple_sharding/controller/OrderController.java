package org.example.simple_sharding.controller;

import org.example.simple_sharding.entity.Order;
import org.example.simple_sharding.repo.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderRepository orderRepository;

    @PostMapping
    public Order create(@RequestBody Order order) {
        return orderRepository.save(order);
    }

    @GetMapping
    public List<Order> getAll() {
        return orderRepository.findAll();
    }
}
