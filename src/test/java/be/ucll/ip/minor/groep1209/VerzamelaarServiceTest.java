package be.ucll.ip.minor.groep1209;

import be.ucll.ip.minor.groep1209.domain.model.Verzamelaar;
import be.ucll.ip.minor.groep1209.domain.service.ServiceException;
import be.ucll.ip.minor.groep1209.domain.service.VerzamelaarRepository;
import be.ucll.ip.minor.groep1209.domain.service.VerzamelaarService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class VerzamelaarServiceTest {
    @Mock
    VerzamelaarRepository verzamelaarRepository;

    @InjectMocks
    VerzamelaarService verzamelaarService;

    @Test
    public void GivenNoVerzamelaars_WhenValidVerzamelaarIsAdded_ThenVerzamelaarIsAddedAndVerzamelaarIsReturned() {
        Verzamelaar verzamelaar = VerzamelaarBuilder.aValidVerzamelaarTestverzamelaar().build();

        when(verzamelaarRepository.existsByName(verzamelaar.getName())).thenReturn(false);
        // The commented line under this gives an UnnecessaryStubbingException
        // when(verzamelaarRepository.existsByNameAndId(verzamelaar.getName(), verzamelaar.getId())).thenReturn(false);
        when(verzamelaarRepository.save(any())).thenReturn(verzamelaar);

        Verzamelaar addedVerzamelaar = verzamelaarService.saveVerzamelaar(verzamelaar);

        assertThat(verzamelaar.getName()).isSameAs(addedVerzamelaar.getName());
    }

    @Test
    public void GivenVerzamelaars_WhenValidVerzamelaarIsAddedWithAlreadyExistingName_ThenVerzamelaarIsNotAddedAndErrorIsThrown() {
        Verzamelaar verzamelaar = VerzamelaarBuilder.aValidVerzamelaarTestverzamelaar().build();
        Verzamelaar duplicateVerzamelaar = VerzamelaarBuilder.aValidVerzamelaarWithDuplicateName().build();

        when(verzamelaarRepository.findByName(duplicateVerzamelaar.getName())).thenReturn(verzamelaar);

        final Throwable raisedException = catchThrowable(() -> verzamelaarService.addVerzamelaar(duplicateVerzamelaar));

        assertThat(raisedException).isInstanceOf(ServiceException.class).hasMessageContaining("name.already.used");
    }

    @Test
    public void GivenVerzamelaars_WhenNonExistingVerzamelaarIsDeleted_ThenErrorIsThrown() {
        Verzamelaar verzamelaar = VerzamelaarBuilder.aValidVerzamelaarTestverzamelaar().build();

        when(verzamelaarRepository.findById(verzamelaar.getId())).thenReturn(Optional.empty());

        final Throwable raisedException = catchThrowable(() -> verzamelaarService.deleteVerzamelaarById(verzamelaar.getId()));

        assertThat(raisedException).isInstanceOf(ServiceException.class).hasMessageContaining("verzamelaar.not.exist");
    }
}
