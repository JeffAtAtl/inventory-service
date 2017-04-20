package com.example;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}

	@Bean
	CommandLineRunner init(SkuRepository skuRepository,
			               StoreRepository storeRepository) {
		return (evt) -> Arrays.asList(
				"123:iPhone 7 32GB,234:iPhone 7 Plus 32 GB".split(","))
				.forEach( a -> {
							String[] skuArry = a.split(":");
							Sku sku = skuRepository.save(new Sku(skuArry[0],skuArry[1]));
                            if (skuArry[0].equals("123")) {
								storeRepository.save(new Store(sku,"900",5));
								storeRepository.save(new Store(sku,"901",3));
							} else {
								storeRepository.save(new Store(sku,"900",2));
							}
						});
	}
}
