package org.example.algorithms;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.Vm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LeastConnectionBroker extends DatacenterBroker {

    public LeastConnectionBroker(String name) throws Exception {
        super(name);
    }

    @Override
    protected void submitCloudlets() {
        List<Vm> vmList = getVmList();
        List<Cloudlet> cloudletList = getCloudletList();

        if (vmList.isEmpty() || cloudletList.isEmpty()) return;

        // Track the number of cloudlets assigned to each VM
        Map<Integer, Integer> vmLoadMap = new HashMap<>();
        for (Vm vm : vmList) {
            vmLoadMap.put(vm.getId(), 0);
        }

        for (Cloudlet cloudlet : cloudletList) {
            // Find VM with the least number of assigned cloudlets
            int leastLoadedVmId = vmLoadMap.entrySet()
                    .stream()
                    .min(Map.Entry.comparingByValue())
                    .get()
                    .getKey();

            cloudlet.setVmId(leastLoadedVmId);
            vmLoadMap.put(leastLoadedVmId, vmLoadMap.get(leastLoadedVmId) + 1); // Increase count
        }

        // Submit all cloudlets to the parent class
        super.submitCloudlets();
    }
}
