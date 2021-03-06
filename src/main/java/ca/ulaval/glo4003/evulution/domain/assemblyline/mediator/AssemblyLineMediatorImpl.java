package ca.ulaval.glo4003.evulution.domain.assemblyline.mediator;

import ca.ulaval.glo4003.evulution.domain.assemblyline.AssemblyLineType;
import ca.ulaval.glo4003.evulution.domain.assemblyline.battery.BatteryAssemblyLine;
import ca.ulaval.glo4003.evulution.domain.assemblyline.battery.BatteryAssemblyLineSequential;
import ca.ulaval.glo4003.evulution.domain.assemblyline.car.CarAssemblyLine;
import ca.ulaval.glo4003.evulution.domain.assemblyline.complete.CompleteAssemblyLineSequential;

public class AssemblyLineMediatorImpl implements AssemblyLineMediator, AssemblyLineMediatorSwitcher {

    private BatteryAssemblyLineSequential batteryAssemblyLine;
    private CompleteAssemblyLineSequential completeAssemblyLine;
    private CarAssemblyLine carAssemblyLine;
    private AssemblyLineType state = AssemblyLineType.CAR;

    public AssemblyLineMediatorImpl(BatteryAssemblyLineSequential batteryAssemblyLine,
            CompleteAssemblyLineSequential completeAssemblyLine, CarAssemblyLine carAssemblyLine) {
        this.batteryAssemblyLine = batteryAssemblyLine;
        this.completeAssemblyLine = completeAssemblyLine;
        this.carAssemblyLine = carAssemblyLine;
    }

    @Override
    public void notify(Object assemblyLineClass) {
        if (assemblyLineClass.equals(CarAssemblyLine.class) && this.state == AssemblyLineType.CAR) {
            this.state = AssemblyLineType.BATTERY;
            this.batteryAssemblyLine.startNext();
        } else if (assemblyLineClass.equals(BatteryAssemblyLine.class)) {
            this.state = AssemblyLineType.COMPLETE;
            this.completeAssemblyLine.startNext();
        } else if (assemblyLineClass.equals(CompleteAssemblyLineSequential.class)) {
            this.state = AssemblyLineType.CAR;
            this.carAssemblyLine.startNext();
        }
    }

    @Override
    public boolean isCarState() {
        return this.state.equals(AssemblyLineType.CAR);
    }

    @Override
    public boolean isCompleteOrBatteryState() {
        return this.state.equals(AssemblyLineType.BATTERY) || this.state.equals(AssemblyLineType.COMPLETE);
    }

    public void setCarAssemblyLine(CarAssemblyLine carAssemblyLine) {
        this.carAssemblyLine = carAssemblyLine;
    }
}
