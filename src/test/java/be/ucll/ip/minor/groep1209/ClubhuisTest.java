package be.ucll.ip.minor.groep1209;

import be.ucll.ip.minor.groep1209.domain.model.Clubhuis;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ClubhuisTest {

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

    @Test
    public void Given_valid_Clubhuis_shouldNotGiveViolations(){
        Clubhuis clubhuis = ClubhuisBuilder.aValidClubhuisTestHuis().build();

        Set<ConstraintViolation<Clubhuis>> violations = validator.validate(clubhuis);

        assertTrue(violations.isEmpty());
    }

    @Test
    public void Given_InvalidClubhuis_WithNoName_ShouldGiveViolationNameMissing(){
        Clubhuis clubhuis = ClubhuisBuilder.anIvalidClubhuisWithNoNameTestHuis().build();

        Set<ConstraintViolation<Clubhuis>> violations = validator.validate(clubhuis);

        assertEquals(violations.size(), 1);
        ConstraintViolation<Clubhuis> violation = violations.iterator().next();
        assertEquals("name.missing", violation.getMessage());
        assertEquals("name", violation.getPropertyPath().toString());
        assertEquals("", violation.getInvalidValue());
    }

    @Test
    public void Given_InvalidClubhuis_WithNoEmail_ShouldGiveViolationEmailMissing(){
        Clubhuis clubhuis = ClubhuisBuilder.anIvalidClubhuisWithNoEmailTestHuis().build();

        Set<ConstraintViolation<Clubhuis>> violations = validator.validate(clubhuis);

        assertEquals(violations.size(), 1);
        ConstraintViolation<Clubhuis> violation = violations.iterator().next();
        assertEquals("email.missing", violation.getMessage());
        assertEquals("email", violation.getPropertyPath().toString());
        assertEquals("", violation.getInvalidValue());
    }

    @Test
    public void Given_InvalidClubhuis_WithNoGemeente_ShouldGiveViolationGemeenteMissing(){
        Clubhuis clubhuis = ClubhuisBuilder.anIvalidClubhuisWithNoMunicipalTestHuis().build();

        Set<ConstraintViolation<Clubhuis>> violations = validator.validate(clubhuis);

        assertEquals(violations.size(), 1);
        ConstraintViolation<Clubhuis> violation = violations.iterator().next();
        assertEquals("gemeente.missing", violation.getMessage());
        assertEquals("gemeente", violation.getPropertyPath().toString());
        assertEquals("", violation.getInvalidValue());
    }

    @Test
    public void Given_InvalidClubhuis_WithNoMaxMemebers_ShouldGiveViolationNotANumber(){
        Clubhuis clubhuis = ClubhuisBuilder.anIvalidClubhuisWithNoMaxMembersTestHuis().build();

        Set<ConstraintViolation<Clubhuis>> violations = validator.validate(clubhuis);

        assertEquals(violations.size(), 1);
        ConstraintViolation<Clubhuis> violation = violations.iterator().next();
        assertEquals("not.a.number", violation.getMessage());
        assertEquals("maxMembers", violation.getPropertyPath().toString());
        assertEquals(null, violation.getInvalidValue());
    }

    @Test
    public void Given_InvalidClubhuis_WithMaxMember_ShouldGiveViolationMemberPositiveNumber(){
        Clubhuis clubhuis = ClubhuisBuilder.anIvalidClubhuisWithNegativeMaxMembersTestHuis().build();

        Set<ConstraintViolation<Clubhuis>> violations = validator.validate(clubhuis);

        assertEquals(violations.size(), 1);
        ConstraintViolation<Clubhuis> violation = violations.iterator().next();
        assertEquals("members.positive.number", violation.getMessage());
        assertEquals("maxMembers", violation.getPropertyPath().toString());
        assertEquals(-10, violation.getInvalidValue());
    }

    @Test
    public void Given_InvalidClubhuis_WithNotValidEmail_ShouldGiveViolation(){
        Clubhuis clubhuis = ClubhuisBuilder.anIvalidClubhuisWithNotAEmailTestHuis().build();

        Set<ConstraintViolation<Clubhuis>> violations = validator.validate(clubhuis);

        assertEquals(violations.size(), 1);
        ConstraintViolation<Clubhuis> violation = violations.iterator().next();
        assertEquals("email.not.valid", violation.getMessage());
        assertEquals("email", violation.getPropertyPath().toString());
        assertEquals("invalidemail.be", violation.getInvalidValue());
    }
}
