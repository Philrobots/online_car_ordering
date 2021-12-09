package ca.ulaval.glo4003.evulution.domain.production.car;

import ca.ulaval.glo4003.evulution.domain.assemblyline.car.adapter.CarAssemblyAdapter;
import ca.ulaval.glo4003.evulution.domain.email.Email;
import ca.ulaval.glo4003.evulution.domain.email.EmailFactory;
import ca.ulaval.glo4003.evulution.domain.manufacture.ProductionId;
import ca.ulaval.glo4003.evulution.domain.email.exceptions.EmailException;

import java.util.List;

public class CarProduction {

    private ProductionId productionId;
    private String email;
    private int carProductionTimeInWeeks;
    private String carStyle;
    private boolean isAssociatedWithManufacture;

    public CarProduction(ProductionId productionId, String carStyle, String email, int carProductionTime, boolean isAssociatedWithManufacture) {
        this.productionId = productionId;
        this.carStyle = carStyle;
        this.email = email;
        this.carProductionTimeInWeeks = carProductionTime;
        this.isAssociatedWithManufacture = isAssociatedWithManufacture;
    }

    public void associateWithManufacture(ProductionId productionId, Email email){

    }
    public void newCarCommand(CarAssemblyAdapter carAssemblyAdapter) {
        carAssemblyAdapter.newVehicleCommand(productionId, carStyle);
    }

    public void sendEmail(EmailFactory emailFactory) throws EmailException {
        emailFactory.createVehicleInProductionEmail(List.of(email), carProductionTimeInWeeks).send();
    }

    public boolean advance(CarAssemblyAdapter carAssemblyAdapter) {
        carAssemblyAdapter.advance();
        return carAssemblyAdapter.isAssembled(productionId);
    }

    public ProductionId getProductionId() {
        return productionId;
    }
}