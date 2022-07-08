package be.ucll.ip.minor.groep1209;

import be.ucll.ip.minor.groep1209.domain.model.Munt;
import be.ucll.ip.minor.groep1209.domain.service.MuntRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.LIST;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class MuntRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MuntRepository muntRepository;

    //Aangezien we geen zelfgeschreven queries gebruiken is het niet echt nodig om hier testen te doen maar ik zal er
    // een paar maken om te tonen dat dit lukt -Robbe
    @Test
    public void WithMuntenRegistered_WhenFindAllByYearLessThanEqualIsExecuted_AllMuntenWithAYearLowerOrEqualToTheGivenYearAreReturned() {
        Munt y1500 = MuntBuilder.aValidMuntTestmunt().build();
        entityManager.persistAndFlush(y1500);
        Munt y350 = MuntBuilder.aValidMuntAnothermunt().build();
        entityManager.persistAndFlush(y350);
        Munt y680 = MuntBuilder.aValidMuntYetanothermunt().build();
        entityManager.persistAndFlush(y680);
        Munt y1630 = MuntBuilder.aValidMuntToomanymunten().build();
        entityManager.persistAndFlush(y1630);

        List<Munt> muntenLessOrEqualToYear1500 = muntRepository.findAllByYearLessThanEqual(1500);

        assertEquals(3, muntenLessOrEqualToYear1500.size());
    }

    @Test
    public void WithMuntenRegistered_WhenFindAllByOrderByNameDescIsExecuted_AllMuntenAreOrganizedByNameDescending() {
        Munt test = MuntBuilder.aValidMuntTestmunt().build();
        entityManager.persistAndFlush(test);
        Munt another = MuntBuilder.aValidMuntAnothermunt().build();
        entityManager.persistAndFlush(another);
        Munt yetanother = MuntBuilder.aValidMuntYetanothermunt().build();
        entityManager.persistAndFlush(yetanother);
        Munt toomany = MuntBuilder.aValidMuntToomanymunten().build();
        entityManager.persistAndFlush(toomany);

        List<Munt> munten = muntRepository.findAllByOrderByNameDesc();

        assertTrue(munten.get(0).getName().equals(yetanother.getName()));
        assertTrue(munten.get(1).getName().equals(toomany.getName()));
        assertTrue(munten.get(2).getName().equals(test.getName()));
        assertTrue(munten.get(3).getName().equals(another.getName()));
    }

    @Test
    public void WithMuntenRegistered_WhenFindAllByOrderByYearIsExecuted_AllMuntenAreOrganizedByAscendingYear() {
        Munt y1500 = MuntBuilder.aValidMuntTestmunt().build();
        entityManager.persistAndFlush(y1500);
        Munt y350 = MuntBuilder.aValidMuntAnothermunt().build();
        entityManager.persistAndFlush(y350);
        Munt y680 = MuntBuilder.aValidMuntYetanothermunt().build();
        entityManager.persistAndFlush(y680);
        Munt y1630 = MuntBuilder.aValidMuntToomanymunten().build();
        entityManager.persistAndFlush(y1630);

        List<Munt> munten = muntRepository.findAllByOrderByYear();


        assertTrue(munten.get(1).getYear()== y680.getYear());
        assertTrue(munten.get(2).getYear()== y1500.getYear());
        assertTrue(munten.get(3).getYear()== y1630.getYear());
        assertTrue(munten.get(0).getYear()== y350.getYear());
    }
}
