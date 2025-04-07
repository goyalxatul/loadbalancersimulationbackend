package org.example.model;

import java.util.List;
import java.util.Map;

public class SimulationResult {
    private String strategy;
    private List<String> cloudletLogs;
    private double averageExecutionTime;
    private double makespan;
    private Map<Integer, Integer> vmLoads;

    public SimulationResult(String strategy, List<String> cloudletLogs, double averageExecutionTime, double makespan, Map<Integer, Integer> vmLoads) {
        this.strategy = strategy;
        this.cloudletLogs = cloudletLogs;
        this.averageExecutionTime = averageExecutionTime;
        this.makespan = makespan;
        this.vmLoads = vmLoads;
    }

    // Getters and Setters
    public String getStrategy() { return strategy; }
    public void setStrategy(String strategy) { this.strategy = strategy; }

    public List<String> getCloudletLogs() { return cloudletLogs; }
    public void setCloudletLogs(List<String> cloudletLogs) { this.cloudletLogs = cloudletLogs; }

    public double getAverageExecutionTime() { return averageExecutionTime; }
    public void setAverageExecutionTime(double averageExecutionTime) { this.averageExecutionTime = averageExecutionTime; }

    public double getMakespan() { return makespan; }
    public void setMakespan(double makespan) { this.makespan = makespan; }

    public Map<Integer, Integer> getVmLoads() { return vmLoads; }
    public void setVmLoads(Map<Integer, Integer> vmLoads) { this.vmLoads = vmLoads; }
}
