package org.example.controller;

import org.example.model.SimulationResult;
import org.example.service.SimulationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/simulate")
@CrossOrigin(origins = "*")
public class SimulationController {

    @Autowired
    private SimulationService simulationService;

    // Endpoint to test API
    @GetMapping
    public String runSimulation() {
        return "Simulation API is working!";
    }

    // Endpoint to run simulation based on algorithm
    @GetMapping("/{algorithm}")
    public SimulationResult simulate(@PathVariable String algorithm) {
        return simulationService.runSimulation(algorithm);
    }
}
