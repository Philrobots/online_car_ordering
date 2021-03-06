package ca.ulaval.glo4003.evulution.service.assemblyLine;

import ca.ulaval.glo4003.evulution.domain.assemblyline.AssemblyLineType;
import ca.ulaval.glo4003.evulution.domain.assemblyline.ProductionLine;
import ca.ulaval.glo4003.evulution.domain.assemblyline.ProductionSwitcher;
import ca.ulaval.glo4003.evulution.domain.assemblyline.ProductionType;
import ca.ulaval.glo4003.evulution.domain.assemblyline.exceptions.AssemblyLineIsNotShutdownException;
import ca.ulaval.glo4003.evulution.domain.assemblyline.exceptions.AssemblyLineIsShutdownException;
import ca.ulaval.glo4003.evulution.domain.assemblyline.exceptions.InvalidAssemblyLineException;
import ca.ulaval.glo4003.evulution.domain.assemblyline.exceptions.InvalidAssemblyLineOrderException;
import ca.ulaval.glo4003.evulution.service.assemblyLine.dto.SwitchProductionsDto;
import ca.ulaval.glo4003.evulution.service.exceptions.ServiceBadInputParameterException;
import ca.ulaval.glo4003.evulution.service.exceptions.ServiceBadOrderOfOperationsException;

public class AssemblyLineService {

    private final ProductionLine productionLine;
    private final ProductionSwitcher productionSwitcher;

    public AssemblyLineService(ProductionLine productionLine, ProductionSwitcher productionSwitcher) {
        this.productionLine = productionLine;
        this.productionSwitcher = productionSwitcher;
    }

    public void shutdown() {
        try {
            this.productionLine.shutdown();
        } catch (AssemblyLineIsShutdownException e) {
            throw new ServiceBadOrderOfOperationsException();
        }
    }

    public void reactivate() {
        try {
            this.productionLine.reactivate();
        } catch (AssemblyLineIsNotShutdownException e) {
            throw new ServiceBadOrderOfOperationsException();
        }
    }

    public void switchProductions(SwitchProductionsDto switchProductionsDto) {
        try {
            ProductionType productionType = ProductionType.valueOf(switchProductionsDto.productionMode);
            AssemblyLineType assemblyLineType = AssemblyLineType.valueOf(switchProductionsDto.lineType);
            this.productionSwitcher.switchProduction(assemblyLineType, productionType);
        } catch (IllegalArgumentException | InvalidAssemblyLineException e) {
            throw new ServiceBadInputParameterException();
        } catch (InvalidAssemblyLineOrderException e) {
            throw new ServiceBadOrderOfOperationsException();
        }
    }
}
