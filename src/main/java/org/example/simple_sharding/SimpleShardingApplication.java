package org.example.simple_sharding;

import org.example.simple_sharding.controller.OrderController;
import org.example.simple_sharding.entity.Order;
import org.example.simple_sharding.service.SampleUsageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SimpleShardingApplication implements CommandLineRunner {
	@Autowired
	private OrderController controller;

	@Autowired
	private SampleUsageService sampleUsageService;

	public static void main(String[] args) {
		SpringApplication.run(SimpleShardingApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
//		sampleUsageService.setupSampleShardingMappings();
//		Order order0 = new Order(0L, "order_1001", "Nguyen Van A", 200000.0, "CREATED");
//		controller.create(order0);
//		Order order1 = new Order(1L, "order_1002", "Nguyen Van B", 300000.0, "CREATED");
//		controller.create(order1);
//		Order order2 = new Order(2L, "order_1003", "Nguyen Van C", 400000.0, "CREATED");
//		controller.create(order2);
		Order order3 = new Order(3L, "order_1004", "Nguyen Van D", 500000.0, "CREATED");
		controller.create(order3);
		Order order4 = new Order(4L, "order_1005", "Nguyen Van E", 600000.0, "CREATED");
		controller.create(order4);
	}
}
