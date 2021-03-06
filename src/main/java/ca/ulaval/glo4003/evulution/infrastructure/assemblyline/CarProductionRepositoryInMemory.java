package ca.ulaval.glo4003.evulution.infrastructure.assemblyline;

import ca.ulaval.glo4003.evulution.domain.manufacture.ProductionId;
import ca.ulaval.glo4003.evulution.domain.production.car.CarProduction;
import ca.ulaval.glo4003.evulution.domain.production.car.CarProductionRepository;
import ca.ulaval.glo4003.evulution.domain.production.exceptions.CarNotAssociatedWithManufactureException;

import java.util.HashMap;
import java.util.Map;

public class CarProductionRepositoryInMemory implements CarProductionRepository {
    private final Map<ProductionId, CarProduction> vehicles = new HashMap<>();

    @Override
    public void add(CarProduction carProduction) {
        this.vehicles.put(carProduction.getProductionId(), carProduction);
    }

    @Override
    public void remove(ProductionId productionId) {
        this.vehicles.remove(productionId);
    }

    @Override
    public boolean replaceCarProductionWithoutManufactureIfItHasBeenMade(CarProduction carProductionLinked)
            throws CarNotAssociatedWithManufactureException {
        if (!carProductionLinked.isAssociatedWithManufacture())
            throw new CarNotAssociatedWithManufactureException();

        ProductionId removedCarProductionId = null;
        for (CarProduction carProduction : vehicles.values()) {
            if (!carProduction.isAssociatedWithManufacture()
                    && carProduction.getCarStyle().equals(carProduction.getCarStyle())) {
                removedCarProductionId = carProduction.getProductionId();
                break;
            }
        }

        if (removedCarProductionId != null) {
            vehicles.remove(removedCarProductionId);
            vehicles.put(carProductionLinked.getProductionId(), carProductionLinked);
            return true;
        }

        return false;
    }
}
