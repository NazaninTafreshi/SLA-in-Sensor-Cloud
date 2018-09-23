package project;

import java.util.Arrays;

/**
 * Created by Ayoubi on 24/04/2016.
 */
public class ConstantApplication {

    private final int cost;
    private final int[] responseTime = new int[2];
    private final String taskType;
    private final double weightCost;
    private final double weightMinRt;
    private final double weightMaxRt;
    private double[] weightResult;


    public ConstantApplication( int cost, String taskType, int[] responseTime) {
        this.cost = cost;
        this.taskType = taskType;
        responseTime = BasicInformation.responseTimeMethod(responseTime);
        this.responseTime[0] = responseTime[0];
        this.responseTime[1] = responseTime[1];
        // weight: importance of sla parameter include weight of cost, minRt and maxRt for each of applications
        double weightResult[] = generateWeight(cost, responseTime[0], responseTime[1]);
        this.weightCost = weightResult[0];
        this.weightMinRt = weightResult[1];
        this.weightMaxRt = weightResult[2];
    }


    public int getCost() {
        return cost;
    }

    public int[] getResponseTime() {
        return responseTime;
    }

    public String getTaskType() {
        return taskType;
    }

    public double getWeightCost() {
        return weightCost;
    }

    public double getWeightMinRt() {
        return weightMinRt;
    }

    public double getWeightMaxRt() {
        return weightMaxRt;
    }

    /**
     * generateWeight method get sla parameters then proportional to the amount of each parameters produces weight for them
     *
     * @param costApp  cost that application announce it
     * @param minRtApp minRt that application announce it
     * @param maxRtApp maxRt that application announce it
     * @return double[] that contains weights for cost, minRt and maxRt respectively
     */
    public  double[] generateWeight(int costApp, int minRtApp, int maxRtApp) {
        weightResult = new double[3];
        double wCost, wMinRt, wMaxRt;
        int lowCost = BasicInformation.minCostApp;
        int differenceCost = BasicInformation.maxCostApp - BasicInformation.minCostApp;
        int lowMinRt = BasicInformation.lowMinRtApp;
        int differenceMinRt = BasicInformation.upMinRtApp - BasicInformation.lowMinRtApp;
        int lowMaxRt = BasicInformation.lowMaxRtApp;
        int differenceMaxRt = BasicInformation.upMaxRtApp - BasicInformation.lowMaxRtApp;

        wCost = proportionalWeight(costApp, lowCost, differenceCost);
        wMinRt = proportionalWeight(minRtApp, lowMinRt, differenceMinRt);
        wMaxRt = proportionalWeight(maxRtApp, lowMaxRt, differenceMaxRt);
        // scaling weights
        double sumOfWeight = wCost + wMinRt + wMaxRt;
        weightResult[0] = (double) Math.round((wCost / sumOfWeight) * 100) / 100;
        weightResult[1] = (double) Math.round((wMinRt / sumOfWeight) * 100) / 100;
        weightResult[2] = (double) Math.round((wMaxRt / sumOfWeight) * 100) / 100;
        if (weightResult[0] + weightResult[1] + weightResult[2] != 1) {
            generateWeight(this.cost, this.responseTime[0], this.responseTime[1]);
        }
        else {
            System.out.println( + this.cost +  "   "+ Arrays.toString(this.responseTime) + "   -----------  weight -----------" + "   " + weightResult[0] + "   " + weightResult[1] + "  " + weightResult[2]);
        }
        return weightResult;
    }


    /**
     * proportionalWeight method with respect to input parameters generate weight
     *
     * @param input      value that caller want determine weight for it
     * @param low        the lowest value of input parameter (cost, minRt or maxRt)
     * @param difference the difference between the lowest and highest value of input parameter
     * @return double weight for input parameter
     */
    private double proportionalWeight(int input, int low, int difference) {
        double weight;
        if (input <= low + (difference * .1)) {
            weight = Configurer.generateRandomNumber(27, 30);
        } else if (input > low + (difference * .1) && input <= low + (difference * .2)) {
            weight = Configurer.generateRandomNumber(24, 27);
        } else if (input > low + (difference * .2) && input <= low + (difference * .3)) {
            weight = Configurer.generateRandomNumber(21, 24);
        } else if (input > low + (difference * .3) && input <= low + (difference * .4)) {
            weight = Configurer.generateRandomNumber(18, 21);
        } else if (input > low + (difference * .4) && input <= low + (difference * .5)) {
            weight = Configurer.generateRandomNumber(15, 18);
        } else if (input > low + (difference * .5) && input <= low + (difference * .6)) {
            weight = Configurer.generateRandomNumber(12, 15);
        } else if (input > low + (difference * .6) && input <= low + (difference * .7)) {
            weight = Configurer.generateRandomNumber(9, 12);
        } else if (input > low + (difference * .7) && input <= low + (difference * .8)) {
            weight = Configurer.generateRandomNumber(6, 9);
        } else if (input > low + (difference * .8) && input <= low + (difference * .9)) {
            weight = Configurer.generateRandomNumber(3, 6);
        } else {
            weight = Configurer.generateRandomNumber(1, 3);
        }
        return weight;
    }

}