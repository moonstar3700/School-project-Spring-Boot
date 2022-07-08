package be.ucll.ip.minor.groep1209;

import be.ucll.ip.minor.groep1209.controller.MuntRestController;
import be.ucll.ip.minor.groep1209.domain.model.Munt;
import be.ucll.ip.minor.groep1209.domain.model.Verzamelaar;
import be.ucll.ip.minor.groep1209.domain.service.MuntRepository;
import be.ucll.ip.minor.groep1209.domain.service.MuntService;
import be.ucll.ip.minor.groep1209.domain.service.VerzamelaarService;
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

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(MuntRestController.class)
public class MuntRestControllerTest {
    ObjectMapper mapper = new ObjectMapper();

    @MockBean
    MuntService service;

    @Autowired
    MockMvc muntRestController;

    @Autowired
    MessageSource messageSource;

    Munt test, another, yetanother, toomany, noname, nocountry;

    @Before
    public void setUp() {
        test = MuntBuilder.aValidMuntTestmunt().build();
        another = MuntBuilder.aValidMuntAnothermunt().build();
        yetanother = MuntBuilder.aValidMuntYetanothermunt().build();
        toomany = MuntBuilder.aValidMuntToomanymunten().build();
        noname = MuntBuilder.anInvalidMuntWithNoName().build();
        nocountry = MuntBuilder.anInvalidMuntWithNoCountry().build();
    }

    @Test
    public void givenMunten_whenGetRequestToGetAllMunten_thenJSONwithAllMuntenIsReturned() throws Exception {
        List<Munt> munten = Arrays.asList(test, another, yetanother, toomany);

        given(service.findAllMunten()).willReturn(munten);

        muntRestController.perform(get("/api/coin/overview")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", Is.is(test.getName())))
                .andExpect(jsonPath("$[1].name", Is.is(another.getName())))
                .andExpect(jsonPath("$[2].name", Is.is(yetanother.getName())))
                .andExpect(jsonPath("$[3].name", Is.is(toomany.getName())));
    }

    @Test
    public void givenNoMunten_whenPutRequestToAddAValidMuntIsExecuted_thenJSONisReturned() throws Exception {
        List<Munt> munt = Arrays.asList(test);

        when(service.saveMunt(test)).thenReturn(test);
        when(service.findAllMunten()).thenReturn(munt);

        muntRestController.perform(put("/api/coin/add")
                .content(mapper.writeValueAsString(test))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name", Is.is(test.getName())));
    }

    @Test
    public void givenNoMunten_whenPutRequestToAddAnInvalidMuntWithNoNameIsExecuted_thenErrorInJSONformatIsReturned() throws Exception {
        String errorMessage = messageSource.getMessage("name.missing", null, new Locale("en_US"));
        muntRestController.perform(put("/api/coin/add")
                .content(mapper.writeValueAsString(noname))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", Is.is(errorMessage)));
    }

    @Test
    public void givenNoMunten_whenPutRequestToAddAnInvalidMuntWithNoCountryIsExecuted_thenErrorInJSONformatIsReturned() throws Exception {
        String errorMessage = messageSource.getMessage("country.missing", null, new Locale("en_US"));
        muntRestController.perform(put("/api/coin/add")
                .content(mapper.writeValueAsString(nocountry))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.country", Is.is(errorMessage)));
    }

/*    @Test
    public void givenOneMunt_whenPutRequestToUpdateThatMuntIsExecuted_thenJSONisReturned() throws Exception {
        List<Munt> munt = Arrays.asList(test);

        //when(service.getMuntRepository().findById(test.getId())).thenReturn(Optional.of(test));
        when(service.saveMunt(test)).thenReturn(test);
        when(service.findAllMunten()).thenReturn(munt);

        muntRestController.perform(put("/api/coin/update/" + test.getId())
                .content(mapper.writeValueAsString(test))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name", Is.is(test.getName())));
    }*/

/*    @Test
    public void givenMunten_whenDeleteRequestToDeleteAMuntIsExecuted_thenJSONisReturned() throws Exception {
        List<Munt> munten = Arrays.asList(test, another, yetanother, toomany);

        when(service.findAllMunten()).thenReturn(munten);

        muntRestController.perform(delete("/api/coin/delete/1")
                .content(mapper.writeValueAsString(test))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name", Is.is(another.getName())))
                .andExpect(jsonPath("$[1].name", Is.is(yetanother.getName())))
                .andExpect(jsonPath("$[2].name", Is.is(toomany.getName())));
    }*/

    @Test
    public void givenMunten_whenGetRequestToSearchByYear_thenJSONwithAllMuntenWithThatYearIsReturned() throws Exception {
        List<Munt> munten = Arrays.asList(test);

        given(service.findAllByYear(test.getYear(), false)).willReturn(munten);

        muntRestController.perform(get("/api/coin/search/year/" + test.getYear())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].year", Is.is(test.getYear())));
    }

    @Test
    public void givenMunten_whenGetRequestToSearchByCountry_thenJSONwithAllMuntenWithCountryAreReturned() throws Exception {
        List<Munt> munten = Arrays.asList(test);

        given(service.findAllByCountry(test.getCountry())).willReturn(munten);

        muntRestController.perform(get("/api/coin/search/country?value=Belgium")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].country", Is.is(test.getCountry())));
    }
}
