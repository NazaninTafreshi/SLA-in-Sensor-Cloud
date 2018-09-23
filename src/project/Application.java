package project;

import java.util.ArrayList;

/**
 * Created by Ayoubi on 24/04/2016.
 */
public class Application {

    private int id;
    private int cost;
    private int[] responseTime = new int[2];
    private String taskType;
    private String brokerCon;
    private float x, y;
    private double weightCost;
    private double weightMinRt;
    private double weightMaxRt;
    private double[] weightResult;

    private CandidateResult resultFromEachBrokerSAW;
    ArrayList<CandidateResult> receiveResultFromBrokersSAW;
    private CandidateResult resultFromGlobalBrokerSAW;
    private CandidateResult resultFromEachBrokerTOPSIS;
    ArrayList<CandidateResult> receiveResultFromBrokersTOPSIS;
    private CandidateResult resultFromGlobalBrokerTOPSIS;

    public Application(int id) {
        this.id = id;
        receiveResultFromBrokersSAW = new ArrayList<>();
        receiveResultFromBrokersTOPSIS = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public Application setId(int id) {
        this.id = id;
        return this;
    }

    public int getCost() {
        return cost;
    }

    public Application setCost(int cost) {
        this.cost = cost;
        return this;
    }

    public int[] getResponseTime() {
        return responseTime;
    }

    public Application setResponseTime(int[] responseTime) {
        this.responseTime = responseTime;
        return this;
    }

    public String getTaskType() {
        return taskType;
    }

    public Application setTaskType(String taskType) {
        this.taskType = taskType;
        return this;
    }

    public String getBrokerCon() {
        return brokerCon;
    }

    public Application setBrokerCon(String brokerCon) {
        this.brokerCon = brokerCon;
        return this;
    }

    public float getX() {
        return x;
    }

    public Application setX(float x) {
        this.x = x;
        return this;
    }

    public float getY() {
        return y;
    }

    public Application setY(float y) {
        this.y = y;
        return this;
    }

    public double getWeightCost() {
        return weightCost;
    }

    public Application setWeightCost(double weight) {
        this.weightCost = weight;
        return this;
    }

    public double getWeightMinRt() {
        return weightMinRt;
    }

    public Application setWeightMinRt(double weightMinRt) {
        this.weightMinRt = weightMinRt;
        return this;
    }

    public double getWeightMaxRt() {
        return weightMaxRt;
    }

    public Application setWeightMaxRt(double weightMaxRt) {
        this.weightMaxRt = weightMaxRt;
        return this;
    }

    public Application setResultFromEachBrokerSAW(CandidateResult resultFromEachBrokerSAW) {
        this.resultFromEachBrokerSAW = resultFromEachBrokerSAW;
        receiveResultFromBrokersSAW.add(resultFromEachBrokerSAW);
        return this;
    }

    public ArrayList<CandidateResult> getReceiveResultFromBrokersSAW() {
        return receiveResultFromBrokersSAW;
    }

    public CandidateResult getResultFromGlobalBrokerSAW() {
        return resultFromGlobalBrokerSAW;
    }

    public Application setResultFromGlobalBrokerSAW(CandidateResult resultFromGlobalBrokerSAW) {
        this.resultFromGlobalBrokerSAW = resultFromGlobalBrokerSAW;
        return this;
    }

    public Application setResultFromEachBrokerTOPSIS(CandidateResult resultFromEachBrokerTOPSIS) {
        this.resultFromEachBrokerTOPSIS = resultFromEachBrokerTOPSIS;
        receiveResultFromBrokersTOPSIS.add(resultFromEachBrokerTOPSIS);
        return this;
    }

    public ArrayList<CandidateResult> getReceiveResultFromBrokersTOPSIS() {
        return receiveResultFromBrokersTOPSIS;
    }

    public CandidateResult getResultFromGlobalBrokerTOPSIS() {
        return resultFromGlobalBrokerTOPSIS;
    }

    public Application setResultFromGlobalBrokerTOPSIS(CandidateResult resultFromGlobalBrokerTOPSIS) {
        this.resultFromGlobalBrokerTOPSIS = resultFromGlobalBrokerTOPSIS;
        return this;
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
        else
            System.out.println(this.id + "   " + this.cost + "   -----------  weight -----------" + "   " + weightResult[0] + "   " + weightResult[1] + "  " + weightResult[2]);
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
