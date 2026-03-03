package com.antigravity.news.controller;

import com.antigravity.news.dto.LocalizedFeedResponse;
import com.antigravity.news.service.NewsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class NewsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NewsService newsService;

    @Test
    @WithMockUser
    void getLocalFeed_withParams_shouldReturnSuccess() throws Exception {
        LocalizedFeedResponse mockResponse = LocalizedFeedResponse.builder()
                .userLocation(LocalizedFeedResponse.LocationInfo.builder().district("Udupi").build())
                .build();

        when(newsService.getLocalFeed(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(mockResponse);

        mockMvc.perform(get("/api/news/local-feed")
                .param("district", "Udupi")
                .param("category", "local"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userLocation.district").value("Udupi"));
    }

    @Test
    void getLocalFeed_unauthorized_shouldReturn403() throws Exception {
        mockMvc.perform(get("/api/news/local-feed"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    void getLocalFeed_serviceThrowsException_shouldReturn500() throws Exception {
        when(newsService.getLocalFeed(any(), any(), any(), any()))
                .thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(get("/api/news/local-feed"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithMockUser
    void getLocalFeed_allNullParams_shouldStillWork() throws Exception {
        when(newsService.getLocalFeed(null, null, null, null))
                .thenReturn(LocalizedFeedResponse.builder().build());

        mockMvc.perform(get("/api/news/local-feed"))
                .andExpect(status().isOk());
    }
}
