package com.example;

import java.net.URI;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/{sku}/stores")
class InventoryRestController {

	private final StoreRepository storeRepository;

	private final SkuRepository skuRepository;

	@Autowired
	InventoryRestController(StoreRepository storeRepository,
						             SkuRepository skuRepository) {
		this.storeRepository = storeRepository;
		this.skuRepository = skuRepository;
	}

	@RequestMapping(method = RequestMethod.GET)
	Collection<Store> readStores(@PathVariable String sku) {
		this.validateSku(sku);
		return this.storeRepository.findBySku(sku);
	}

	@RequestMapping(method = RequestMethod.POST)
	ResponseEntity<?> add(@PathVariable String sku, @RequestBody Store input) {
		this.validateSku(sku);

		return (ResponseEntity<?>) this.skuRepository
				.findBySku(sku)
				.map(s -> {
					Store result = storeRepository.save(new Store(s, input.storeNo, input.count));

					URI location = ServletUriComponentsBuilder
						.fromCurrentRequest().path("/{id}")
						.buildAndExpand(result.getId()).toUri();

					return ResponseEntity.created(location).build();
				})
				.orElse(ResponseEntity.noContent().build());

	}

	@RequestMapping(method = RequestMethod.GET, value = "/{storeId}")
	Store readStore(@PathVariable String sku, @PathVariable Long storeId) {
		this.validateSku(sku);
		return this.storeRepository.findOne(storeId);
	}

	private void validateSku(String sku) {
		this.skuRepository.findBySku(sku).orElseThrow(
				() -> new SkuNotFoundException(sku));
	}
}
