package com.kartaca.airquality.Controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class AirQualityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetByLocation() throws Exception {
        mockMvc.perform(get("/api/air_quality_data")
                .param("latitude", "40.0")
                .param("longitude", "29.0"))
                .andExpect(status().isOk());
    }
}
