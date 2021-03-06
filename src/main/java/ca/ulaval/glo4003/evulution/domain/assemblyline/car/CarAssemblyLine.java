package ca.ulaval.glo4003.evulution.domain.assemblyline.car;

import ca.ulaval.glo4003.evulution.domain.production.car.CarProduction;
import ca.ulaval.glo4003.evulution.domain.production.exceptions.CarNotAssociatedWithManufactureException;

import java.util.List;

public interface CarAssemblyLine {
    void addProduction(CarProduction carProduction) throws CarNotAssociatedWithManufactureException;

    void advance();

    void shutdown();

    void reactivate();

    void startNext();

    void transferAssemblyLine(CarAssemblyLine carAssemblyLine);

    List<CarProduction> getWaitingList();

    boolean getIsBatteryInFire();
}
