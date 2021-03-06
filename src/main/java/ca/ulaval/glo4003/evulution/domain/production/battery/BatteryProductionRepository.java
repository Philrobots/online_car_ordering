package ca.ulaval.glo4003.evulution.domain.production.battery;

import ca.ulaval.glo4003.evulution.domain.manufacture.ProductionId;

import java.util.List;

public interface BatteryProductionRepository {

    void add(BatteryProduction batteryProduction);

    void remove(ProductionId productionId);

    List<BatteryProduction> getAndSendToProduction();

}
