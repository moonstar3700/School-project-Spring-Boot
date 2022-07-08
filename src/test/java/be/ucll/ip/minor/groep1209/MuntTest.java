package be.ucll.ip.minor.groep1209;

import be.ucll.ip.minor.groep1209.domain.model.Munt;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MuntTest {
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
    public void Given_ValidMunt_ShouldNotGiveViolations(){
        Munt munt = MuntBuilder.aValidMuntTestmunt().build();

        Set<ConstraintViolation<Munt>> violations = validator.validate(munt);

        assertTrue(violations.isEmpty());
    }

    @Test
    public void Given_InvalidMunt_WithEmptyName_ShouldGiveViolationNameMissing(){
        Munt munt = MuntBuilder.anInvalidMuntWithNoName().build();

        Set<ConstraintViolation<Munt>> violations = validator.validate(munt);

        assertEquals(violations.size(), 1);
        ConstraintViolation<Munt> violation = violations.iterator().next();
        assertEquals("name.missing", violation.getMessage());
        assertEquals("name", violation.getPropertyPath().toString());
        assertEquals("", violation.getInvalidValue());
    }

    @Test
    public void Given_InvalidMunt_WithEmptyCountry_ShouldGiveViolationCountryMissing(){
        Munt munt = MuntBuilder.anInvalidMuntWithNoCountry().build();

        Set<ConstraintViolation<Munt>> violations = validator.validate(munt);

        assertEquals(violations.size(), 1);
        ConstraintViolation<Munt> violation = violations.iterator().next();
        assertEquals("country.missing", violation.getMessage());
        assertEquals("country", violation.getPropertyPath().toString());
        assertEquals("", violation.getInvalidValue());
    }

    @Test
    public void Given_InvalidMunt_WithNoCurrency_ShouldGiveViolationCurrencyMissing(){
        Munt munt = MuntBuilder.anInvalidMuntWithNoCurrency().build();

        Set<ConstraintViolation<Munt>> violations = validator.validate(munt);

        assertEquals(violations.size(), 1);
        ConstraintViolation<Munt> violation = violations.iterator().next();
        assertEquals("currency.missing", violation.getMessage());
        assertEquals("currency", violation.getPropertyPath().toString());
        assertEquals(null, violation.getInvalidValue());
    }

    @Test
    public void Given_InvalidMunt_WithNoValue_ShouldGiveViolationValueMissing(){
        Munt munt = MuntBuilder.anInvalidMuntWithNoValue().build();

        Set<ConstraintViolation<Munt>> violations = validator.validate(munt);

        assertEquals(violations.size(), 1);
        ConstraintViolation<Munt> violation = violations.iterator().next();
        assertEquals("value.missing", violation.getMessage());
        assertEquals("value", violation.getPropertyPath().toString());
        assertEquals(null, violation.getInvalidValue());
    }

    @Test
    public void Given_InvalidMunt_WithValueLessThan0_ShouldGiveViolationValueTooLow(){
        Munt munt = MuntBuilder.anInvalidMuntWithValueLessThan0().build();

        Set<ConstraintViolation<Munt>> violations = validator.validate(munt);

        assertEquals(violations.size(), 1);
        ConstraintViolation<Munt> violation = violations.iterator().next();
        assertEquals("value.too.low", violation.getMessage());
        assertEquals("value", violation.getPropertyPath().toString());
        assertEquals(-1.0, violation.getInvalidValue());
    }

    @Test
    public void Given_InvalidMunt_WithNoYear_ShouldGiveViolationNotANumber(){
        Munt munt = MuntBuilder.anInvalidMuntWithNoYear().build();

        Set<ConstraintViolation<Munt>> violations = validator.validate(munt);

        assertEquals(violations.size(), 1);
        ConstraintViolation<Munt> violation = violations.iterator().next();
        assertEquals("not.a.number", violation.getMessage());
        assertEquals("year", violation.getPropertyPath().toString());
        assertEquals(null, violation.getInvalidValue());
    }

    @Test
    public void Given_InvalidMunt_WithYearLessThan600BC_ShouldGiveViolationYearTooLow(){
        Munt munt = MuntBuilder.anInvalidMuntWithYearLessThan600BC().build();

        Set<ConstraintViolation<Munt>> violations = validator.validate(munt);

        assertEquals(violations.size(), 1);
        ConstraintViolation<Munt> violation = violations.iterator().next();
        assertEquals("year.too.low", violation.getMessage());
        assertEquals("year", violation.getPropertyPath().toString());
        assertEquals(-700, violation.getInvalidValue());
    }

    // This test will not work because the year is validated in the MuntService
//    @Test
//    public void Given_InvalidMunt_WithYearMoreThan2022_ShouldGiveViolationYearTooHigh(){
//        Munt munt = MuntBuilder.anInvalidMuntWithYearGreaterThan2022().build();
//
//        Set<ConstraintViolation<Munt>> violations = validator.validate(munt);
//
//        assertEquals(violations.size(), 1);
//        ConstraintViolation<Munt> violation = violations.iterator().next();
//        assertEquals("year.too.big", violation.getMessage());
//        assertEquals("year", violation.getPropertyPath().toString());
//        assertEquals(2050, violation.getInvalidValue());
//    }
}
