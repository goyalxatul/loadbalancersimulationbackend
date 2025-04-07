package org.example.algorithms;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.Vm;

import java.util.List;

public class RoundRobinBroker extends DatacenterBroker {
    private int lastVmIndex = -1;

    public RoundRobinBroker(String name) throws Exception {
        super(name);
    }

    @Override
    protected void submitCloudlets() {
        List<Vm> vmList = getVmList();
        List<Cloudlet> cloudletList = getCloudletList();

        if (vmList.isEmpty() || cloudletList.isEmpty()) return;

        for (Cloudlet cloudlet : cloudletList) {
            lastVmIndex = (lastVmIndex + 1) % vmList.size(); // Assign cloudlets in round-robin
            Vm assignedVm = vmList.get(lastVmIndex);
            cloudlet.setVmId(assignedVm.getId());
        }

        // Call parent method to handle submission
        super.submitCloudlets();
    }
}

