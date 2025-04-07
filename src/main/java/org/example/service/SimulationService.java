package org.example.service;

import org.cloudbus.cloudsim.*;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.*;
import org.example.algorithms.LeastConnectionBroker;
import org.example.algorithms.RoundRobinBroker;
import org.example.model.SimulationResult;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class SimulationService {
    private static final int NUM_USERS = 1;
    private static final int NUM_VMS = 3;
    private static final int NUM_CLOUDLETS = 6;
    private static final int VM_RAM = 2048;
    private static final int HOST_RAM = 16384;

    public SimulationResult runSimulation(String algorithm) {
        CloudSim.init(NUM_USERS, Calendar.getInstance(), false);
        createDatacenter("Datacenter_1");

        DatacenterBroker broker;

        try {
            switch (algorithm.toLowerCase()) {
                case "leastconnection":
                    broker = new LeastConnectionBroker("LC_Broker");
                    break;
                case "roundrobin":
                default:
                    broker = new RoundRobinBroker("RR_Broker");
                    break;
            }

            broker.submitVmList(createVmList(broker.getId(), NUM_VMS));
            broker.submitCloudletList(createCloudletList(broker.getId(), NUM_CLOUDLETS));

            CloudSim.startSimulation();
            List<Cloudlet> results = broker.getCloudletReceivedList();
            CloudSim.stopSimulation();

            return analyzePerformance(algorithm, results);

        } catch (Exception e) {
            e.printStackTrace();
            return new SimulationResult("Error: " + e.getMessage(), null, 0, 0, null);
        }
    }

    private Datacenter createDatacenter(String name) {
        List<Pe> peList = List.of(new Pe(0, new PeProvisionerSimple(8000)));
        List<Host> hostList = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            hostList.add(new Host(i, new RamProvisionerSimple(HOST_RAM),
                    new BwProvisionerSimple(10000), 1000000, peList,
                    new VmSchedulerTimeShared(peList)));
        }

        DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
                "x86", "Linux", "Xen", hostList,
                10.0, 0.01, 0.02, 0.001, 0.005);

        try {
            return new Datacenter(name, characteristics, new VmAllocationPolicySimple(hostList), new LinkedList<>(), 0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<Vm> createVmList(int brokerId, int numVms) {
        List<Vm> vmList = new ArrayList<>();

        for (int i = 0; i < numVms; i++) {
            int mips;
            if (i == 0) mips = 3000; // Fast VM
            else if (i == 1) mips = 2000;
            else mips = 1000; // Slow VM

            Vm vm = new Vm(i, brokerId, mips, 1, VM_RAM, 1000, 10000,
                    "Xen", new CloudletSchedulerTimeShared());
            vmList.add(vm);
        }

        return vmList;
    }

    private List<Cloudlet> createCloudletList(int brokerId, int numCloudlets) {
        List<Cloudlet> cloudletList = new ArrayList<>();
        Random rand = new Random();

        for (int i = 0; i < numCloudlets; i++) {
            int length = 20000 + rand.nextInt(40000); // Between 20k to 60k
            Cloudlet cloudlet = new Cloudlet(i, length, 1, 300, 300,
                    new UtilizationModelFull(), new UtilizationModelFull(), new UtilizationModelFull());
            cloudlet.setUserId(brokerId);
            cloudletList.add(cloudlet);
        }

        return cloudletList;
    }

    private SimulationResult analyzePerformance(String strategy, List<Cloudlet> list) {
        double totalExecTime = 0, makespan = 0;
        Map<Integer, Integer> vmLoadMap = new HashMap<>();
        List<String> cloudletLogs = new ArrayList<>();

        for (Cloudlet cl : list) {
            totalExecTime += cl.getActualCPUTime();
            makespan = Math.max(makespan, cl.getFinishTime());

            int vmId = cl.getVmId();
            vmLoadMap.put(vmId, vmLoadMap.getOrDefault(vmId, 0) + 1);

            cloudletLogs.add("Cloudlet " + cl.getCloudletId() + " -> VM " + cl.getVmId() +
                    " | Length: " + cl.getCloudletLength() +
                    " | Start: " + cl.getExecStartTime() +
                    " | Finish: " + cl.getFinishTime());
        }

        double avgExecTime = totalExecTime / list.size();
        return new SimulationResult(strategy, cloudletLogs, avgExecTime, makespan, vmLoadMap);
    }
}
