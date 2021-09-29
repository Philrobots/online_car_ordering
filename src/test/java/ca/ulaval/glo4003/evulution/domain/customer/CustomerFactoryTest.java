package ca.ulaval.glo4003.evulution.domain.customer;

import ca.ulaval.glo4003.evulution.api.exceptions.InvalidDateFormatException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class CustomerFactoryTest {
    private static final String AN_EMAIl = "tiray@expat.com";
    private static final String A_PASSWORD = "123456";
    private static final String A_BIRTH_DATE = "1999-08-08";
    private static final String A_NAME = "TI RAY EXPAT";
    private static final Gender A_GENDER = Gender.WOMEN;

    private final String DATE_FORMAT = "yyyy-MM-dd";
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
    private final LocalDate A_LOCAL_DATE = LocalDate.parse(A_BIRTH_DATE, formatter);
    private String A_DATE_IN_THE_FUTURE = "2070-03-03";

    private CustomerFactory customerFactory;

    @BeforeEach
    public void setUp() {
        customerFactory = new CustomerFactory();
    }

    @Test
    public void givenInformation_whenCreateCustomer_thenCustomerHasTheSameInformations() {
        // when
        Customer customer = customerFactory.create(A_NAME, A_BIRTH_DATE, AN_EMAIl, A_PASSWORD, A_GENDER);

        // then
        assertEquals(customer.getEmail(), AN_EMAIl);
        assertEquals(customer.getBirthDate(), A_LOCAL_DATE);
        assertEquals(customer.getName(), A_NAME);
        assertEquals(customer.getPassword(), A_PASSWORD);
        assertEquals(customer.getGender(), A_GENDER);
    }

    @Test
    public void givenBirthDateInTheFuture_whenCreateCustomer_thenShouldThrowException() {
        assertThrows(InvalidDateFormatException.class, () -> customerFactory.create(A_NAME, A_DATE_IN_THE_FUTURE, AN_EMAIl, A_PASSWORD, A_GENDER));
    }
}
