package com.example;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

/**
 * @author Josh Long
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = InventoryServiceApplication.class)
@WebAppConfiguration
public class InventoryServiceApplicationTests {


    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    private Sku sku;

    private List<Store> storeList = new ArrayList<>();

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private SkuRepository skuRepository;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
            .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
            .findAny()
            .orElse(null);

        assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();

        this.storeRepository.deleteAllInBatch();
        this.skuRepository.deleteAllInBatch();

        this.sku = skuRepository.save(new Sku("123", "iPhone 7 32GB"));
        this.storeList.add(storeRepository.save(new Store(sku, "900", 5)));
        this.storeList.add(storeRepository.save(new Store(sku, "901", 2)));
    }

    @Test
    public void skuNotFound() throws Exception {
        mockMvc.perform(post("/321/stores/")
                .content(this.json(new Store()))
                .contentType(contentType))
                .andExpect(status().isNotFound());
    }

    @Test
    public void readSinglestore() throws Exception {
        mockMvc.perform(get("/" + sku + "/stores/"
                + this.storeList.get(0).getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id", is(this.storeList.get(0).getId().intValue())))
                .andExpect(jsonPath("$.storeNo", is("900")))
                .andExpect(jsonPath("$.count", is(5)));
    }

    @Test
    public void readStores() throws Exception {
        mockMvc.perform(get("/" + sku + "/stores"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(this.storeList.get(0).getId().intValue())))
                .andExpect(jsonPath("$[0].storeNo", is("900")))
                .andExpect(jsonPath("$[0].count", is(5)))
                .andExpect(jsonPath("$[1].id", is(this.storeList.get(1).getId().intValue())))
                .andExpect(jsonPath("$[1].storeNo", is("901")))
                .andExpect(jsonPath("$[1].count", is(2)));
    }

    @Test
    public void createStore() throws Exception {
        String storeJson = json(new Store(this.sku, "902", 3));

        this.mockMvc.perform(post("/" + sku + "/stores/")
                .contentType(contentType)
                .content(storeJson))
                .andExpect(status().isCreated());
    }

    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }
}
