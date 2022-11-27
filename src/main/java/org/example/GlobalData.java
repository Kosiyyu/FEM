package org.example;

import lombok.Data;

@Data
public class GlobalData {
    private double simulationTime = 0.0;
    private double simulationStepTime = 0.0;
    private double conductivity = 0.0;
    private double alfa = 0.0;
    private double tot = 0.0;
    private double initialTemp = 0.0;
    private double density = 0.0;
    private double specificHeat = 0.0;

    public GlobalData() {
    }

    public GlobalData(Double simulationTime, Double simulationStepTime, Double conductivity, Double alfa, Double tot, Double initialTemp, Double density, Double specificHeat) {
        this.simulationTime = simulationTime;
        this.simulationStepTime = simulationStepTime;
        this.conductivity = conductivity;
        this.alfa = alfa;
        this.tot = tot;
        this.initialTemp = initialTemp;
        this.density = density;
        this.specificHeat = specificHeat;
    }

    @Override
    public String toString() {
        return "simulationTime: " + simulationTime +
                ", simulationStepTime: " + simulationStepTime +
                ", conductivity: " + conductivity +
                ", alfa: " + alfa +
                ", tot: " + tot +
                ", initialTemp: " + initialTemp +
                ", density: " + density +
                ", specificHeat: " + specificHeat;
    }
}
