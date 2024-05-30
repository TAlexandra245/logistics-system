package com.project.logistics.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.logistics.dto.DestinationDto;
import com.project.logistics.exceptions.ResourceNotFoundException;
import com.project.logistics.repository.DestinationRepository;
import com.project.logistics.service.DestinationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(DestinationController.class)
class DestinationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DestinationService destinationService;

    private DestinationDto destinationDto;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        destinationDto = new DestinationDto();
        destinationDto.setId(1L);
        destinationDto.setName("Ploiesti");
        destinationDto.setDistance(20);
    }

    @Test
    public void testGetDestinationById() throws Exception {

        //given
        Long id = 1L;
        when(destinationService.findById(id)).thenReturn(destinationDto);

        mockMvc.perform(get("/destinations/{id}", id).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(destinationDto.getId()))
                .andExpect(jsonPath("$.name").value(destinationDto.getName()))
                .andExpect(jsonPath("$.distance").value(destinationDto.getDistance()));
    }

    @Test
    public void testGetDestinationById_NotFound() throws Exception {
        Long nonExistingId = 50L;
        when(destinationService.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(get("/destinations/{id}", nonExistingId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

    }

}