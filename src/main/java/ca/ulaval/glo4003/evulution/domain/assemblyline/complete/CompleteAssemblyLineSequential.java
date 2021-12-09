package ca.ulaval.glo4003.evulution.domain.assemblyline.complete;

import ca.ulaval.glo4003.evulution.domain.production.car.CarProductionRepository;
import ca.ulaval.glo4003.evulution.domain.production.battery.BatteryProductionRepository;
import ca.ulaval.glo4003.evulution.domain.assemblyline.mediator.AssemblyLineMediator;
import ca.ulaval.glo4003.evulution.domain.delivery.exceptions.DeliveryIncompleteException;
import ca.ulaval.glo4003.evulution.domain.email.EmailFactory;
import ca.ulaval.glo4003.evulution.domain.manufacture.ProductionId;
import ca.ulaval.glo4003.evulution.domain.production.complete.CompleteAssemblyProduction;
import ca.ulaval.glo4003.evulution.infrastructure.assemblyline.exceptions.InvalidMappingKeyException;
import ca.ulaval.glo4003.evulution.domain.email.exceptions.EmailException;

import java.util.LinkedList;

public class CompleteAssemblyLineSequential {

    private static final double FIFTY_PERCENT_CHANCE = 0.5;
    private static final Integer ASSEMBLY_DELAY_IN_WEEKS = 1;

    private final EmailFactory emailFactory;
    private final CarProductionRepository carProductionRepository;
    private final BatteryProductionRepository batteryProductionRepository;
    private AssemblyLineMediator assemblyLineMediator;
    private LinkedList<CompleteAssemblyProduction> waitingList = new LinkedList<>();
    private CompleteAssemblyProduction currentProduction;
    private int weeksRemaining;
    private boolean isCarCompleteInProduction = false;
    private boolean isBatteryInFire = false;

    public CompleteAssemblyLineSequential(EmailFactory emailFactory, CarProductionRepository carProductionRepository,
                                          BatteryProductionRepository batteryProductionRepository) {
        this.emailFactory = emailFactory;
        this.carProductionRepository = carProductionRepository;
        this.batteryProductionRepository = batteryProductionRepository;
    }

    public void addProduction(CompleteAssemblyProduction completeAssemblyProduction) {
        waitingList.add(completeAssemblyProduction);
    }

    public void advance() throws DeliveryIncompleteException, InvalidMappingKeyException, EmailException {

        if (!isCarCompleteInProduction || isBatteryInFire) {
            return;
        }

        if (weeksRemaining == 2) {
            this.currentProduction.addDelayInWeeksAndSendEmail(ASSEMBLY_DELAY_IN_WEEKS, emailFactory);
            this.weeksRemaining--;
        } else if (weeksRemaining == 1) {
            this.weeksRemaining--;
        } else if (weeksRemaining == 0) {
            this.currentProduction.ship();
            ProductionId productionId = currentProduction.getProductionId();
            this.carProductionRepository.remove(productionId);
            this.batteryProductionRepository.remove(productionId);
            assemblyLineMediator.notify(this.getClass());
            this.isCarCompleteInProduction = false;
        }
    }

    public void shutdown() {
        this.isBatteryInFire = true;
        if (isCarCompleteInProduction)
            this.waitingList.add(currentProduction);
        this.isCarCompleteInProduction = false;
    }

    public void reactivate() {
        this.isBatteryInFire = false;
    }

    public void startNext() throws EmailException {

        if (this.isBatteryInFire)
            return;

        setUpForProduction();
    }

    private void setUpForProduction() throws EmailException {
        this.currentProduction = this.waitingList.pop();
        this.weeksRemaining = Math.random() < FIFTY_PERCENT_CHANCE ? ASSEMBLY_DELAY_IN_WEEKS * 2
                : ASSEMBLY_DELAY_IN_WEEKS;

        currentProduction.sendProductionStartEmail(emailFactory, weeksRemaining);

        this.isCarCompleteInProduction = true;
    }

    public void setMediator(AssemblyLineMediator assemblyLineMediator) {
        this.assemblyLineMediator = assemblyLineMediator;
    }
}