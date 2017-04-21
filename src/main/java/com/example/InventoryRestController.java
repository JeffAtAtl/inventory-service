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
@RequestMapping("/skus")
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
	Collection<Sku> readSkus() {
		return this.skuRepository.findAll();
	}
	
	@RequestMapping(method = RequestMethod.POST)
	Sku addSku(@RequestBody Sku input) {
		return this.skuRepository.save(new Sku(input.sku, input.description));
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{sku}/stores")
	Collection<Store> readStores(@PathVariable String sku) {
		this.validateSku(sku);
		return this.storeRepository.findBySkuSku(sku);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/{sku}/stores")
	ResponseEntity<?> add(@PathVariable String sku, @RequestBody Store input) {
		this.validateSku(sku);

		return (ResponseEntity<?>) this.skuRepository
				.findBySku(sku)
				.map(s -> {
					Store result = this.storeRepository.save(new Store(s, input.storeNo, input.count));

					URI location = ServletUriComponentsBuilder
						.fromCurrentRequest().path("/{id}")
						.buildAndExpand(result.getId()).toUri();

					return ResponseEntity.created(location).build();
				})
				.orElse(ResponseEntity.noContent().build());

	}

	@RequestMapping(method = RequestMethod.GET, value = "/{sku}/stores/{storeId}")
	Store readStore(@PathVariable String sku, @PathVariable Long storeId) {
		this.validateSku(sku);
		return this.storeRepository.findOne(storeId);
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/{sku}/stores/{storeId}")
	Store updateStore(@PathVariable String sku, @PathVariable Long storeId, @RequestBody Store input) {
		this.validateSku(sku);
		this.storeRepository.delete(storeId);
		long newStoreId = storeRepository.save(input).getId();
		return this.storeRepository.findOne(newStoreId);
	}

	private void validateSku(String sku) {
		this.skuRepository.findBySku(sku).orElseThrow(
				() -> new SkuNotFoundException(sku));
	}
}
