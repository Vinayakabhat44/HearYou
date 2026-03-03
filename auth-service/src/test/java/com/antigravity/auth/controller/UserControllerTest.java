package com.antigravity.auth.controller;

import com.antigravity.auth.dto.LocationData;
import com.antigravity.auth.entity.User;
import com.antigravity.auth.repository.UserRepository;
import com.antigravity.auth.service.GeocodingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private GeocodingService geocodingService;

    @Test
    @WithMockUser
    void getUser_existingUser_shouldReturnUser() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setDistrict("Udupi");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.district").value("Udupi"));
    }

    @Test
    @WithMockUser
    void getUser_withNullLocation_shouldReturnUserWithNulls() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        // location fields are null by default

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.district").isEmpty());
    }

    @Test
    @WithMockUser
    void updateLocation_geocodingServiceFails_shouldHandleException() throws Exception {
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(geocodingService.reverseGeocode(anyDouble(), anyDouble()))
                .thenThrow(new RuntimeException("Geocoding service unavailable"));

        mockMvc.perform(put("/api/users/1/location")
                .param("lat", "13.34")
                .param("lng", "74.74"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithMockUser
    void updateLocation_partialLocationData_shouldSaveSuccessfully() throws Exception {
        User user = new User();
        user.setId(1L);
        LocationData partialData = new LocationData();
        partialData.setDistrict("Udupi");
        // other fields null

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(geocodingService.reverseGeocode(anyDouble(), anyDouble())).thenReturn(partialData);
        when(userRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(put("/api/users/1/location")
                .param("lat", "13.34")
                .param("lng", "74.74"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.district").value("Udupi"))
                .andExpect(jsonPath("$.taluk").isEmpty());
    }
}
