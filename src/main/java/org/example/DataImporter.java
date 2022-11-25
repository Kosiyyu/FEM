package org.example;

import lombok.Data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Data
public class DataImporter {

    GlobalData globalData;
    Grid grid;

    public DataImporter(){

    }

    public void importData(String fileName){
        File file = new File(fileName);
        Scanner sc;
        int counter = 0;
        String dummyLine = "";
        List<String> lines = new ArrayList<>();

        try{
            sc = new Scanner(file);
            while (sc.hasNextLine()){
                String line = sc.nextLine();
                lines.add(line);
            }

            String [] simulationTimeText = lines.get(0).split(" ");
            //System.out.println(simulationTimeText[1]);
            double simulationTime = Double.parseDouble(simulationTimeText[1]);

            String [] simulationStepTimeText = lines.get(1).split(" ");
            //System.out.println(simulationStepTimeText[1]);
            double simulationStepTime = Double.parseDouble(simulationStepTimeText[1]);

            String [] conductivityText = lines.get(2).split(" ");
            //System.out.println(conductivityText[1]);
            double conductivity = Double.parseDouble(conductivityText[1]);

            String [] alfaText = lines.get(3).split(" ");
            //System.out.println(alfaText[1]);
            double alfa = Double.parseDouble(alfaText[1]);

            String [] totText = lines.get(4).split(" ");
            //System.out.println(totText[1]);
            double tot = Double.parseDouble(totText[1]);

            String [] initialTempText  = lines.get(5).split(" ");
            //System.out.println(initialTempText[1]);
            double initialTemp = Double.parseDouble(initialTempText[1]);

            String [] densityText  = lines.get(6).split(" ");
            //System.out.println(densityText[1]);
            double density = Double.parseDouble(densityText[1]);

            String [] specificHeatText  = lines.get(7).split(" ");
            //System.out.println(specificHeatText[1]);
            double specificHeat = Double.parseDouble(specificHeatText[1]);

            String [] nodesNumberText  = lines.get(8).split(" ");
            //System.out.println(nodesNumberText[2]);
            int nodesNumber = Integer.parseInt(nodesNumberText[2]);

            String [] elementsNumberText  = lines.get(9).split(" ");
            //System.out.println(elementsNumberText[2]);
            int elementsNumber = Integer.parseInt(elementsNumberText[2]);

            Node[] nodes = new Node[nodesNumber];
            counter = 11;
            for(int i = 0; i < nodesNumber; i++){
                String [] nodesLine = lines.get(counter).split(",");
                int id = Integer.parseInt(nodesLine[0].replaceAll("\\s+",""));
                double x = Double.parseDouble(nodesLine[1].replaceAll("\\s+",""));
                double y = Double.parseDouble(nodesLine[2].replaceAll("\\s+",""));
                nodes[i] = new Node(id, x, y);
                counter++;
            }
            counter++;

            Element[] elements = new Element[elementsNumber];
            for(int i = 0; i < elementsNumber; i++){
                String [] elementsLine = lines.get(counter).split(",");
                int elementId = Integer.parseInt(elementsLine[0].replaceAll("\\s+",""));
                int id1 = Integer.parseInt(elementsLine[1].replaceAll("\\s+",""));
                int id2 = Integer.parseInt(elementsLine[2].replaceAll("\\s+",""));
                int id3 = Integer.parseInt(elementsLine[3].replaceAll("\\s+",""));
                int id4 = Integer.parseInt(elementsLine[4].replaceAll("\\s+",""));

                int [] ID = new int[4];
                ID[0] = id1;
                ID[1] = id2;
                ID[2] = id3;
                ID[3] = id4;
                elements[i] = new Element(elementId, ID);
                counter++;
            }
            counter++;
            String [] BCLine = lines.get(counter).split(",");
            int [] BC = new int[BCLine.length];
            for(int i = 0; i < BCLine.length; i++){
                BC[i] = Integer.parseInt(BCLine[i].replaceAll("\\s+",""));
            }

            for(int i = 0; i < BCLine.length; i++){
                for(int j = 0; j < nodes.length; j++){
                    if(BC[i] == nodes[j].getNodeId()){
                        nodes[j].setBC(1);
                    }
                }
            }
            globalData = new GlobalData(simulationTime,simulationStepTime,conductivity, alfa, tot, initialTemp, density, specificHeat);
            grid = new Grid(nodesNumber, elementsNumber, nodes, elements);
        }
        catch (Exception e){
            System.err.println("Error message: " + e.getMessage());
        }
    }

}
