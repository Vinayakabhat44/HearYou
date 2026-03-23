package com.mitraai.core.feed.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mitraai.core.feed.entity.Story;
import com.mitraai.core.feed.service.StoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class StoryControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private StoryService storyService;

        @Autowired
        private ObjectMapper objectMapper;

        @Test
        @WithMockUser(username = "1")
        void createStory_withFile_shouldReturnCreatedStory() throws Exception {
                // Arrange
                Story story = new Story();
                story.setContent("Multipart Story");
                story.setType(Story.StoryType.STORY);

                MockMultipartFile storyPart = new MockMultipartFile(
                                "story",
                                "",
                                "application/json",
                                objectMapper.writeValueAsBytes(story));

                MockMultipartFile filePart = new MockMultipartFile(
                                "file",
                                "test.jpg",
                                "image/jpeg",
                                "test image content".getBytes());

                Story savedStory = new Story();
                savedStory.setId(1L);
                savedStory.setContent("Multipart Story");
                savedStory.setMediaUrl("http://s3.com/test.jpg");

                when(storyService.createStory(any(Story.class), any(), any(), anyLong(), any())).thenReturn(savedStory);

                // Act & Assert
                mockMvc.perform(multipart("/api/stories")
                                .file(storyPart)
                                .file(filePart)
                                .param("lat", "12.34")
                                .param("lng", "77.56")
                                .contentType(MediaType.MULTIPART_FORM_DATA))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(1))
                                .andExpect(jsonPath("$.mediaUrl").value("http://s3.com/test.jpg"));
        }

        @Test
        @WithMockUser(username = "1")
        void createStoryJson_shouldReturnCreatedStory() throws Exception {
                // Arrange
                Story story = new Story();
                story.setContent("JSON Story");

                Story savedStory = new Story();
                savedStory.setId(2L);
                savedStory.setContent("JSON Story");
                savedStory.setVillage("Test Village");

                when(storyService.createStory(any(Story.class), any(), any(), anyLong(), isNull()))
                                .thenReturn(savedStory);

                // Act & Assert
                mockMvc.perform(post("/api/stories")
                                .content(objectMapper.writeValueAsString(story))
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("lat", "12.34")
                                .param("lng", "77.56"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.village").value("Test Village"));
        }
}
