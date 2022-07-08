package be.ucll.ip.minor.groep1209;

import be.ucll.ip.minor.groep1209.domain.model.Munt;
import be.ucll.ip.minor.groep1209.domain.service.MuntRepository;
import be.ucll.ip.minor.groep1209.domain.service.MuntService;
import be.ucll.ip.minor.groep1209.domain.service.ServiceException;
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
public class MuntServiceTest {
    @Mock
    MuntRepository muntRepository;

    @InjectMocks
    MuntService muntService;

    @Test
    public void GivenNoMunten_WhenValidMuntIsAdded_ThenMuntIsAddedAndMuntIsReturned() {
        Munt munt = MuntBuilder.aValidMuntTestmunt().build();

        when(muntRepository.existsByName(munt.getName())).thenReturn(false);
        // The commented line under this gives an UnnecessaryStubbingException
        // when(muntRepository.existsByNameAndId(munt.getName(), munt.getId())).thenReturn(false);
        when(muntRepository.save(any())).thenReturn(munt);

        Munt addedMunt = muntService.saveMunt(munt);

        assertThat(munt.getName()).isSameAs(addedMunt.getName());
    }

    @Test
    public void GivenMunten_WhenValidMuntIsAddedWithAlreadyExistingName_ThenMuntIsNotAddedAndErrorIsThrown() {
        Munt munt = MuntBuilder.aValidMuntTestmunt().build();
        Munt duplicateMunt = MuntBuilder.aValidMuntWithDuplicateName().build();

        // These 2 methods are required to check wether or not a munt can be added or updated
        when(muntRepository.existsByName(munt.getName())).thenReturn(true);
        when(muntRepository.existsByNameAndId(duplicateMunt.getName(), duplicateMunt.getId())).thenReturn(false);

        final Throwable raisedException = catchThrowable(() -> muntService.saveMunt(duplicateMunt));

        assertThat(raisedException).isInstanceOf(ServiceException.class).hasMessageContaining("name.exists");
    }

    @Test
    public void GivenMunten_WhenNonExistingMuntIsDeleted_ThenErrorIsThrown() {
        Munt munt = MuntBuilder.aValidMuntTestmunt().build();

        when(muntRepository.findById(munt.getId())).thenReturn(Optional.empty());

        final Throwable raisedException = catchThrowable(() -> muntService.deleteMuntById(munt.getId()));

        assertThat(raisedException).isInstanceOf(ServiceException.class).hasMessageContaining("coin.not.exist");
    }
}
