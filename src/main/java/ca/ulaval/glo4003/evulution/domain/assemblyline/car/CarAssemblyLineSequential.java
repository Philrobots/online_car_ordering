package ca.ulaval.glo4003.evulution.domain.assemblyline.car;

import ca.ulaval.glo4003.evulution.domain.assemblyline.car.adapter.CarAssemblyAdapter;
import ca.ulaval.glo4003.evulution.domain.assemblyline.mediator.AssemblyLineMediator;
import ca.ulaval.glo4003.evulution.domain.email.ProductionLineEmailNotifier;
import ca.ulaval.glo4003.evulution.domain.production.car.CarProduction;
import ca.ulaval.glo4003.evulution.domain.production.car.CarProductionRepository;

import java.util.LinkedList;
import java.util.List;

public class CarAssemblyLineSequential implements CarAssemblyLine {

    private LinkedList<CarProduction> carProductionWaitList = new LinkedList<>();

    private final CarAssemblyAdapter carAssemblyAdapter;
    private ProductionLineEmailNotifier productionLineEmailNotifier;
    private final CarProductionRepository carProductionRepository;
    private AssemblyLineMediator assemblyLineMediator;
    private CarProduction currentCarProduction;
    private boolean isBatteryInFire = false;
    private boolean isCarInProduction = false;

    public CarAssemblyLineSequential(CarAssemblyAdapter carAssemblyAdapter,
            CarProductionRepository carProductionRepository, ProductionLineEmailNotifier productionLineEmailNotifier) {
        this.carAssemblyAdapter = carAssemblyAdapter;
        this.carProductionRepository = carProductionRepository;
        this.productionLineEmailNotifier = productionLineEmailNotifier;
    }

    public void setMediator(AssemblyLineMediator assemblyLineMediator) {
        this.assemblyLineMediator = assemblyLineMediator;
    }

    public void addProduction(CarProduction carProduction) {
        this.carProductionWaitList.add(carProduction);
        if (!(isCarInProduction || this.isBatteryInFire)) {
            setupNextProduction();
        }
    }

    public void advance() {
        if (!isCarInProduction || this.isBatteryInFire) {
            return;
        }

        boolean isCarAssembled = currentCarProduction.advance(carAssemblyAdapter);

        if (isCarAssembled) {
            this.carProductionRepository.add(currentCarProduction);
            this.assemblyLineMediator.notify(CarAssemblyLine.class);
            this.isCarInProduction = false;
        }
    }

    public void shutdown() {
        this.isBatteryInFire = true;
    }

    public void reactivate() {
        this.isBatteryInFire = false;
        if (this.assemblyLineMediator.isCarState() && !carProductionWaitList.isEmpty())
            setupNextProduction();
    }

    public void startNext() {
        if (!carProductionWaitList.isEmpty())
            setupNextProduction();
    }

    @Override
    public void transferAssemblyLine(CarAssemblyLine carAssemblyLine) {
        this.carProductionWaitList = new LinkedList<>(carAssemblyLine.getWaitingList());
        this.isBatteryInFire = carAssemblyLine.getIsBatteryInFire();
        if (!carProductionWaitList.isEmpty()) {
            this.setupNextProduction();
        }
    }

    @Override
    public List<CarProduction> getWaitingList() {
        if (isCarInProduction)
            this.carProductionWaitList.addFirst(currentCarProduction);
        LinkedList<CarProduction> returnList = new LinkedList<>(this.carProductionWaitList);
        carProductionWaitList.clear();
        this.isCarInProduction = false;

        return returnList;
    }

    @Override
    public boolean getIsBatteryInFire() {
        return isBatteryInFire;
    }

    private void setupNextProduction() {
        this.isCarInProduction = true;
        this.currentCarProduction = this.carProductionWaitList.pop();
        productionLineEmailNotifier.sendCarStartedEmail(currentCarProduction.getProductionId(),
                currentCarProduction.getTimeToProduce());
        this.currentCarProduction.newCarCommand(carAssemblyAdapter);
    }
}
