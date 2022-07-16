package be.ucll.ip.minor.groep1209;

import be.ucll.ip.minor.groep1209.controller.ClubhuisRestController;
import be.ucll.ip.minor.groep1209.controller.MuntRestController;
import be.ucll.ip.minor.groep1209.domain.model.Clubhuis;
import be.ucll.ip.minor.groep1209.domain.service.ClubhuisService;
import be.ucll.ip.minor.groep1209.domain.service.MuntService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.mockito.BDDMockito;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.assertj.core.api.BDDAssumptions.given;

@RunWith(SpringRunner.class)
@WebMvcTest(ClubhuisRestController.class)
public class ClubhuisRestControllerTest {
    ObjectMapper mapper = new ObjectMapper();

    @MockBean
    ClubhuisService service;

    @Autowired
    MockMvc clubhuisRestController;

    @Autowired
    MessageSource messageSource;

    Clubhuis test, another, yetAnother, noname, nogemeente;

    @Before
    public void setUp(){
        test = ClubhuisBuilder.aValidClubhuisTestHuis().build();
        another = ClubhuisBuilder.aValidAnotherClubhuisTestHuis().build();
        yetAnother = ClubhuisBuilder.aValidYetAnotherClubhuisTestHuis().build();
        noname = ClubhuisBuilder.anIvalidClubhuisWithNoNameTestHuis().build();
        nogemeente = ClubhuisBuilder.anIvalidClubhuisWithNoMunicipalTestHuis().build();
    }

    @Test
    public void givenClubhuizen_whenGetRequestToGetAllClubhuizen_thenJSONWithAllClubhuizenIsReturned() throws Exception{
        List<Clubhuis> list = Arrays.asList(test, another, yetAnother);

        BDDMockito.given(service.findAll()).willReturn(list);

        clubhuisRestController.perform(get("/api/clubhuis/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", Is.is(test.getName())))
                .andExpect(jsonPath("$[1].name", Is.is(another.getName())))
                .andExpect(jsonPath("$[2].name", Is.is(yetAnother.getName())));


    }

    @Test
    public void givenNoClubhuis_whenPutRequestToAddAnInvalidClubhuisWithNoNameIsExecuted_thenErrorInJSONformatIsReturned() throws Exception {
        String errorMessage = messageSource.getMessage("name.missing", null, new Locale("en_US"));
        clubhuisRestController.perform(put("/api/clubhuis/add")
                .content(mapper.writeValueAsString(noname))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", Is.is(errorMessage)));
    }

    @Test
    public void givenNoClubhuis_whenPutRequestToAddAnInvalidClubhuisWithNoGemeenteIsExecuted_thenErrorInJSONformatIsReturned() throws Exception {
        String errorMessage = messageSource.getMessage("gemeente.missing", null, new Locale("en_US"));
        clubhuisRestController.perform(put("/api/clubhuis/add")
                .content(mapper.writeValueAsString(nogemeente))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.gemeente", Is.is(errorMessage)));
    }


}
