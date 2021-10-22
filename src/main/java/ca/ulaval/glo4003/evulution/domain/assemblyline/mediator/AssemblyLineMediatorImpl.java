package ca.ulaval.glo4003.evulution.domain.assemblyline.mediator;

import ca.ulaval.glo4003.evulution.domain.assemblyline.BatteryAssemblyLine;
import ca.ulaval.glo4003.evulution.domain.assemblyline.CompleteCarAssemblyLine;
import ca.ulaval.glo4003.evulution.domain.assemblyline.VehicleAssemblyLine;

public class AssemblyLineMediatorImpl implements AssemblyLineMediator {

    private BatteryAssemblyLine batteryAssemblyLine;
    private CompleteCarAssemblyLine completeCarAssemblyLine;

    public AssemblyLineMediatorImpl(BatteryAssemblyLine batteryAssemblyLine,
            CompleteCarAssemblyLine completeCarAssemblyLine) {
        this.batteryAssemblyLine = batteryAssemblyLine;
        this.completeCarAssemblyLine = completeCarAssemblyLine;
    }

    @Override
    public void notify(Class assemblyLineClass) {
        if (assemblyLineClass.equals(VehicleAssemblyLine.class)) {
            this.batteryAssemblyLine.startNext();
        } else if (assemblyLineClass.equals(BatteryAssemblyLine.class)) {
            this.completeCarAssemblyLine.startNext();
        }
    }
}
