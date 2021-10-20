package ca.ulaval.glo4003.evulution.domain.sale;

import ca.ulaval.glo4003.evulution.domain.car.Battery;
import ca.ulaval.glo4003.evulution.domain.car.Car;
import ca.ulaval.glo4003.evulution.domain.delivery.Delivery;
import ca.ulaval.glo4003.evulution.domain.delivery.DeliveryDetails;
import ca.ulaval.glo4003.evulution.domain.sale.exceptions.CarNotChosenBeforeBatteryException;
import ca.ulaval.glo4003.evulution.domain.sale.exceptions.MissingElementsForSaleException;
import ca.ulaval.glo4003.evulution.domain.sale.exceptions.SaleCompleteException;
import ca.ulaval.glo4003.evulution.domain.sale.exceptions.SaleNotCompletedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class SaleTest {
    private static final String AN_EMAIL = "email@email.com";
    private static final Integer EFFICIENCY_EQUIVALENCE_RATE = 100;

    @Mock
    private TransactionId transactionId;

    @Mock
    private DeliveryDetails deliveryDetails;

    @Mock
    private Delivery delivery;

    @Mock
    private Car car;

    @Mock
    private Battery battery;

    private Sale sale;

    @BeforeEach
    public void setUp() {
        this.sale = new Sale(AN_EMAIL, transactionId, delivery);
    }

    @Test
    public void givenCompleteSale_whenChooseCar_thenThrowSaleCompleteException() {
        // given
        sale.setSaleAsCompleted();

        // when
        Executable chooseCar = () -> sale.chooseCar(car);

        // then
        assertThrows(SaleCompleteException.class, chooseCar);
    }

    @Test
    public void givenCompleteSale_whenChooseBattery_thenThrowSaleCompleteException() {
        // given
        sale.chooseCar(car);
        sale.setSaleAsCompleted();

        // when
        Executable chooseBattery = () -> sale.chooseBattery(battery);

        // then
        assertThrows(SaleCompleteException.class, chooseBattery);
    }

    @Test
    public void givenSaleWithoutCar_whenChooseBattery_thenThrowCarNotChosenBeforeBatteryException() {
        assertThrows(CarNotChosenBeforeBatteryException.class, () -> this.sale.chooseBattery(battery));
    }

    @Test
    public void givenNoChosenCar_whenCompleteSale_thenThrowMissingElementsForSaleException() {
        // when
        Executable completeSale = () -> sale.completeSale();

        // then
        assertThrows(MissingElementsForSaleException.class, completeSale);
    }

    @Test
    public void givenNoChosenBattery_whenCompleteSale_thenThrowMissingElementsForSaleException() {
        // given
        sale.chooseCar(car);

        // when
        Executable completeSale = () -> sale.completeSale();

        // then
        assertThrows(MissingElementsForSaleException.class, completeSale);
    }

    @Test
    public void givenSaleAlreadyCompleted_whenCompleteSale_thenThrowSaleCompleteException() {
        // given
        sale.chooseCar(car);
        sale.chooseBattery(battery);
        sale.setSaleAsCompleted();

        // when
        Executable completeSale = () -> sale.completeSale();

        // then
        assertThrows(SaleCompleteException.class, completeSale);
    }

    @Test
    public void whenCompleteSale_thenCalculateDeliveryDate() {
        // given
        sale.chooseCar(car);
        sale.chooseBattery(battery);

        // when
        sale.completeSale();

        // then
        Mockito.verify(delivery).calculateDeliveryDate(car.getTimeToProduceAsInt(), battery.getTimeToProduceAsInt());
    }

    @Test
    public void givenEfficiencyEquivalenceRate_whenGetBatteryAutonomy_thenReturnEstimatedRange() {
        // given
        sale.chooseCar(car);
        sale.chooseBattery(battery);
        BDDMockito.given(car.getEfficiencyEquivalenceRate()).willReturn(EFFICIENCY_EQUIVALENCE_RATE);

        // when
        sale.getBatteryAutonomy();

        // then
        Mockito.verify(battery).calculateEstimatedRange(EFFICIENCY_EQUIVALENCE_RATE);
    }

    @Test
    public void givenSaleNotComplete_whenSetDeliveryDetails_thenThrowSaleNotCompletedException() {
        assertThrows(SaleNotCompletedException.class, () -> this.sale.setDeliveryDetails(deliveryDetails));
    }

    @Test
    public void whenSetDeliveryDetails_thenDeliverySetDetails() {
        // given
        sale.chooseCar(car);
        sale.chooseBattery(battery);
        sale.setSaleAsCompleted();

        // when
        sale.setDeliveryDetails(deliveryDetails);

        // then
        Mockito.verify(delivery).setDeliveryDetails(deliveryDetails);
    }

    @Test
    public void whenDeliverToCampus_thenDeliverySetToCampus() {
        // when
        sale.deliverToCampus();

        // then
        Mockito.verify(delivery).deliverToCampus();
    }
}