package be.ucll.ip.minor.groep1209;

import be.ucll.ip.minor.groep1209.domain.model.Club;
import be.ucll.ip.minor.groep1209.domain.model.Clubhuis;
import be.ucll.ip.minor.groep1209.domain.service.ClubhuisRepository;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ClubhuisRepositoryTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeClass
    public static void createValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterClass
    public static void close() {
        validatorFactory.close();
    }

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ClubhuisRepository clubhuisRepository;

    @Test
    public void WithClubhuizenRegistered_WhenFindAllContainingIsExecuted_AllClubhuizenContainingThatStringAreReteruned(){
        Clubhuis clubhuisGemeenteValidGemeente = ClubhuisBuilder.aValidClubhuisTestHuis().build();
        entityManager.persistAndFlush(clubhuisGemeenteValidGemeente);
        Clubhuis clubhuisGemeenteAnotherGemeente = ClubhuisBuilder.aValidAnotherClubhuisTestHuis().build();
        entityManager.persistAndFlush(clubhuisGemeenteAnotherGemeente);
        Clubhuis clubhuisGemeenteSsssss = ClubhuisBuilder.aValidYetAnotherClubhuisTestHuis().build();
        entityManager.persistAndFlush(clubhuisGemeenteSsssss);

        List<Clubhuis> clubhuisListContainingStringInGemeente = clubhuisRepository.findAllByGemeenteContainingIgnoreCase("a");

        assertEquals(2, clubhuisListContainingStringInGemeente.size());
    }
}
