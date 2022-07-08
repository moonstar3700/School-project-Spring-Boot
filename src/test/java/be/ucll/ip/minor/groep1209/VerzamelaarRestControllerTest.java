package be.ucll.ip.minor.groep1209;

import be.ucll.ip.minor.groep1209.controller.VerzamelaarRestController;
import be.ucll.ip.minor.groep1209.domain.model.Verzamelaar;
import be.ucll.ip.minor.groep1209.domain.service.VerzamelaarService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(VerzamelaarRestController.class)
public class VerzamelaarRestControllerTest {
    ObjectMapper mapper = new ObjectMapper();

    @MockBean
    VerzamelaarService service;

    @Autowired
    MockMvc verzamelaarRestController;

    Verzamelaar jarno, thomas, pieter;

    @Before
    public void setUp() {
        jarno = VerzamelaarBuilder.aValidVerzamelaarTestverzamelaar().build();
        thomas = VerzamelaarBuilder.aValidVerzamelaarAnotherverzamelaar().build();
        pieter = VerzamelaarBuilder.anInvalidVerzamelaarWithNoName().build();
    }

    @Test
    public void givenVerzamelaars_whenGetRequestToAllVerzamelaars_thenJSONwithAllVerzamelaarsReturned() throws Exception {
        List<Verzamelaar> verzamelaars = Arrays.asList(jarno, thomas);

        given(service.findAllVerzamelaars()).willReturn(verzamelaars);

        verzamelaarRestController.perform(get("/api/collector/overview")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", Is.is(jarno.getName())))
                .andExpect(jsonPath("$[1].name", Is.is(thomas.getName())));
    }

    @Test
    public void givenNoVerzamelaars_whenPutRequestToAddAValidVerzamelaar_thenJSONisReturned() throws Exception {
        List<Verzamelaar> verzamelaars = Arrays.asList(jarno);

        when(service.addVerzamelaar(jarno)).thenReturn(jarno);
        when(service.findAllVerzamelaars()).thenReturn(verzamelaars);

        verzamelaarRestController.perform(put("/api/collector/add")
                .content(mapper.writeValueAsString(jarno))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name", Is.is(jarno.getName())));
    }

    @Test
    public void givenNoVerzamelaars_whenPutRequestToAddAnInvalidVerzamelaar_thenErrorInJSONformatIsReturned() throws Exception {
        verzamelaarRestController.perform(put("/api/collector/add")
                .content(mapper.writeValueAsString(pieter))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", Is.is("Name can not be empty")));
    }
}
