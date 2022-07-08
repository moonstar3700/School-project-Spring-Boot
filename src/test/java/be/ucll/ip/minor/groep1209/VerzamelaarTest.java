package be.ucll.ip.minor.groep1209;

import be.ucll.ip.minor.groep1209.domain.model.Verzamelaar;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.Assert.*;

public class VerzamelaarTest {
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
    public void Given_ValidVerzamelaar_ShouldNotGiveViolations(){
        Verzamelaar verzamelaar = VerzamelaarBuilder.aValidVerzamelaarTestverzamelaar().build();

        Set<ConstraintViolation<Verzamelaar>> violations = validator.validate(verzamelaar);

        assertTrue(violations.isEmpty());
    }

    @Test
    public void Given_InvalidVerzamelaar_WithEmptyName_ShouldGiveViolationNameMissingAndNotEnoughCharacters(){
        Verzamelaar verzamelaar = VerzamelaarBuilder.anInvalidVerzamelaarWithNoName().build();

        Set<ConstraintViolation<Verzamelaar>> violations = validator.validate(verzamelaar);

        assertEquals(violations.size(), 2);
        ConstraintViolation<Verzamelaar> violation = violations.iterator().next();
        assertEquals("name.missing", violation.getMessage());
        assertEquals("name", violation.getPropertyPath().toString());
        assertEquals("", violation.getInvalidValue());
        violations.remove(violations.iterator().next());
        ConstraintViolation<Verzamelaar> violation1 = violations.iterator().next();
        assertEquals("name.min.length.3", violation1.getMessage());
        assertEquals("name", violation.getPropertyPath().toString());
        assertEquals("", violation.getInvalidValue());
    }

    @Test
    public void Given_InvalidVerzamelaar_WithEmptyFirstname_ShouldGiveViolationFirstnameMissing(){
        Verzamelaar verzamelaar = VerzamelaarBuilder.anInvalidVerzamelaarWithNoFirstname().build();

        Set<ConstraintViolation<Verzamelaar>> violations = validator.validate(verzamelaar);

        assertEquals(violations.size(), 1);
        ConstraintViolation<Verzamelaar> violation = violations.iterator().next();
        assertEquals("firstname.missing", violation.getMessage());
        assertEquals("firstname", violation.getPropertyPath().toString());
        assertEquals("", violation.getInvalidValue());
    }

    @Test
    public void Given_InvalidVerzamelaar_WithEmptyRegion_ShouldGiveViolationRegionMissing(){
        Verzamelaar verzamelaar = VerzamelaarBuilder.anInvalidVerzamelaarWithNoRegion().build();

        Set<ConstraintViolation<Verzamelaar>> violations = validator.validate(verzamelaar);

        assertEquals(violations.size(), 1);
        ConstraintViolation<Verzamelaar> violation = violations.iterator().next();
        assertEquals("region.missing", violation.getMessage());
        assertEquals("region", violation.getPropertyPath().toString());
        assertEquals("", violation.getInvalidValue());
    }

    @Test
    public void Given_InvalidVerzamelaar_WithNoAge_ShouldGiveViolationNotANumber(){
        Verzamelaar verzamelaar = VerzamelaarBuilder.anInvalidVerzamelaarWithNoAge().build();

        Set<ConstraintViolation<Verzamelaar>> violations = validator.validate(verzamelaar);

        assertEquals(violations.size(), 1);
        ConstraintViolation<Verzamelaar> violation = violations.iterator().next();
        assertEquals("not.a.number", violation.getMessage());
        assertEquals("age", violation.getPropertyPath().toString());
        assertNull(violation.getInvalidValue());
    }

    @Test
    public void Given_InvalidVerzamelaar_WithAgeLessThan18_ShouldGiveViolationAgeTooLow(){
        Verzamelaar verzamelaar = VerzamelaarBuilder.anInvalidVerzamelaarWithAgeTooYoung().build();

        Set<ConstraintViolation<Verzamelaar>> violations = validator.validate(verzamelaar);

        assertEquals(violations.size(), 1);
        ConstraintViolation<Verzamelaar> violation = violations.iterator().next();
        assertEquals("age.lower.than.18", violation.getMessage());
        assertEquals("age", violation.getPropertyPath().toString());
        assertEquals(12, violation.getInvalidValue());
    }

    @Test
    public void Given_InvalidVerzamelaar_WithAgeHigherThan110_ShouldGiveViolationAgeTooHigh(){
        Verzamelaar verzamelaar = VerzamelaarBuilder.anInvalidVerzamelaarWithAgeTooOld().build();

        Set<ConstraintViolation<Verzamelaar>> violations = validator.validate(verzamelaar);

        assertEquals(violations.size(), 1);
        ConstraintViolation<Verzamelaar> violation = violations.iterator().next();
        assertEquals("age.higher.than.110", violation.getMessage());
        assertEquals("age", violation.getPropertyPath().toString());
        assertEquals(130, violation.getInvalidValue());
    }

    @Test
    public void Given_InvalidVerzamelaar_WithNameLessThen3Characters_ShouldGiveViolationNameTooSmall(){
        Verzamelaar verzamelaar = VerzamelaarBuilder.anInvalidVerzamelaarWithLessThenThreeCharacters().build();

        Set<ConstraintViolation<Verzamelaar>> violations = validator.validate(verzamelaar);

        assertEquals(violations.size(), 1);
        ConstraintViolation<Verzamelaar> violation = violations.iterator().next();
        assertEquals("name.min.length.3", violation.getMessage());
        assertEquals("name", violation.getPropertyPath().toString());
        assertEquals("Me", violation.getInvalidValue());
    }
}
