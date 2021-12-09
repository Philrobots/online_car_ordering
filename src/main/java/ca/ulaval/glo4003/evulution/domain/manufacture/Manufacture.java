package ca.ulaval.glo4003.evulution.domain.manufacture;

import ca.ulaval.glo4003.evulution.domain.car.Battery;
import ca.ulaval.glo4003.evulution.domain.car.Car;
import ca.ulaval.glo4003.evulution.domain.delivery.Delivery;
import ca.ulaval.glo4003.evulution.domain.delivery.DeliveryId;
import ca.ulaval.glo4003.evulution.domain.production.battery.BatteryProduction;
import ca.ulaval.glo4003.evulution.domain.production.battery.BatteryProductionFactory;
import ca.ulaval.glo4003.evulution.domain.production.car.CarProduction;
import ca.ulaval.glo4003.evulution.domain.production.car.CarProductionFactory;
import ca.ulaval.glo4003.evulution.domain.production.complete.CompleteAssemblyProduction;
import ca.ulaval.glo4003.evulution.domain.production.complete.CompleteAssemblyProductionFactory;
import ca.ulaval.glo4003.evulution.domain.sale.exceptions.CarNotChosenBeforeBatteryException;
import ca.ulaval.glo4003.evulution.domain.sale.exceptions.MissingElementsForSaleException;

public class Manufacture {
    // TODO pt mettre manufacture command
    private ProductionId productionId;
    private Car car;
    private Battery battery;
    private Delivery delivery;
    private ManufactureStatus status;

    public Manufacture(Delivery delivery) {
        this.productionId = new ProductionId();
        this.delivery = delivery;
        status = ManufactureStatus.PENDING;
    }

    public DeliveryId getDeliveryId() {
        return delivery.getDeliveryId();
    }

    public Delivery getDelivery() {
        return delivery;
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
    }

    public void setCar(Car car) {
        this.car = car;
        this.delivery.setCarTimeToProduce(car.getTimeToProduceAsInt());
    }

    public int addBattery(Battery battery) throws CarNotChosenBeforeBatteryException {
        if (this.car == null)
            throw new CarNotChosenBeforeBatteryException();
        this.battery = battery;
        this.delivery.setBatteryTimeToProduce(battery.getTimeToProduceAsInt());
        return getEstimatedRange();
    }

    public boolean isReadyToProduce() {
        return this.status.equals(ManufactureStatus.READY);
    }

    private int getEstimatedRange() {
        return battery.calculateEstimatedRange(car.getEfficiencyEquivalenceRate());
    }

    public void setReadyToProduce() throws MissingElementsForSaleException {
        if (this.car == null || this.battery == null)
            throw new MissingElementsForSaleException();
        status = ManufactureStatus.READY;
    }

    public void setInProduction() {
        status = ManufactureStatus.IN_PRODUCTION;
    }

    public BatteryProduction generateBatteryProduction(String email,
            BatteryProductionFactory batteryProductionFactory) {
        return batteryProductionFactory.create(productionId, battery.getType(), email, battery.getTimeToProduceAsInt());
    }

    public CarProduction generateCarProduction(String email, CarProductionFactory carProductionFactory) {
        return carProductionFactory.create(productionId, car.getStyle(), email, car.getTimeToProduceAsInt());
    }

    public CompleteAssemblyProduction generateCompleteAssemblyProduction(String email,
            CompleteAssemblyProductionFactory completeAssemblyProductionFactory) {
        return completeAssemblyProductionFactory.create(productionId, delivery, email);
    }

}