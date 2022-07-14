package be.ucll.ip.minor.groep1209;

import be.ucll.ip.minor.groep1209.domain.model.Club;
import be.ucll.ip.minor.groep1209.domain.model.Clubhuis;
import be.ucll.ip.minor.groep1209.domain.model.DomainException;
import be.ucll.ip.minor.groep1209.domain.service.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ClubhuisServiceTest {

    @Mock
    ClubhuisRepository clubhuisRepository;

    @InjectMocks
    ClubhuisService clubhuisService;

    @Test
    public void GivenAClubhouseIsRegistered_WhenAValidClubhuisIsAddedWithAlreadyExistingNameAndGemeente_TheClubhuisIsNotAddedAndErrorIsThrown(){
        Clubhuis clubhuis = ClubhuisBuilder.aValidClubhuisTestHuis().build();
        Clubhuis clubhuisDuplicate = ClubhuisBuilder.aValidClubhuisTestHuis().build();

        when(clubhuisRepository.existsByNameAndGemeente(clubhuis.getName(), clubhuis.getGemeente())).thenReturn(true);
        when(clubhuisRepository.existsByNameAndGemeenteAndId(clubhuisDuplicate.getName(), clubhuisDuplicate.getGemeente(), clubhuisDuplicate.getId())).thenReturn(false);

        final Throwable raisedException = catchThrowable(() -> clubhuisService.saveClubhuis(clubhuisDuplicate));

        assertThat(raisedException).isInstanceOf(DomainException.class).hasMessageContaining("clubhuis.name.gemeente.exists");
    }

    @Test
    public void GivenNoClubhouseIsRegistered_WhenValidClubhuisIsAdded_ThenClubhuisIsAddedAndClubhuisIsReturned(){
        Clubhuis clubhuis = ClubhuisBuilder.aValidClubhuisTestHuis().build();
        int aantalClubhuizenErvoor = clubhuisRepository.findAll().size();

        when(clubhuisRepository.existsByNameAndGemeente(clubhuis.getName(), clubhuis.getGemeente())).thenReturn(false);
        when(clubhuisRepository.save(clubhuis)).thenReturn(clubhuis);

        int aantalClubhuizenErna = clubhuisRepository.findAll().size();

        assertThat(aantalClubhuizenErvoor == aantalClubhuizenErna - 1);
    }

}
