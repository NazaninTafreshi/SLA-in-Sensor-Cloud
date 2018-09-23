package project;

import java.util.Comparator;

/**
 * Created by Ayoubi on 17/05/2016.
 */
public class CandidateResult implements Comparable<CandidateResult>, Comparator<CandidateResult> {

    private int applicationId;
    private int brokerId;
    private int cloudId;
    private int totalCost;
    private Integer[] totalResponseTime = new Integer[2];
    private String TaskType;
    private double score;
    private double lambdaScore;
    private double[] weightedNormal;

    public CandidateResult() {
    }

    public CandidateResult(int applicationId, int brokerId, int cloudId, int totalCost, Integer[] totalResponseTime, String taskType) {
        this.applicationId = applicationId;
        this.brokerId = brokerId;
        this.cloudId = cloudId;
        this.totalCost = totalCost;
        this.totalResponseTime = totalResponseTime;
        TaskType = taskType;
    }
    public CandidateResult(int applicationId, int brokerId, int cloudId, int totalCost, Integer[] totalResponseTime, String taskType,double lambdaScore) {
        this.applicationId = applicationId;
        this.brokerId = brokerId;
        this.cloudId = cloudId;
        this.totalCost = totalCost;
        this.totalResponseTime = totalResponseTime;
        TaskType = taskType;
        this.lambdaScore = lambdaScore;
    }

    public int getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(int applicationId) {
        this.applicationId = applicationId;
    }

    public int getBrokerId() {
        return brokerId;
    }

    public void setBrokerId(int brokerId) {
        this.brokerId = brokerId;
    }

    public int getCloudId() {
        return cloudId;
    }

    public void setCloudId(int cloudId) {
        this.cloudId = cloudId;
    }

    public int getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(int totalCost) {
        this.totalCost = totalCost;
    }

    public Integer[] getTotalResponseTime() {
        return totalResponseTime;
    }

    public void setTotalResponseTime(Integer[] totalResponseTime) {
        this.totalResponseTime = totalResponseTime;
    }

    public String getTaskType() {
        return TaskType;
    }

    public CandidateResult setTaskType(String taskType) {
        TaskType = taskType;
        return this;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public double getLambdaScore() {
        return lambdaScore;
    }

    public CandidateResult setLambdaScore(double lambdaScore) {
        this.lambdaScore = lambdaScore;
        return this;
    }

    public double[] getWeightedNormal() {
        return weightedNormal;
    }

    public void setWeightedNormal(double[] weightedNormal) {
        this.weightedNormal = weightedNormal;
    }


    @Override
    public int compareTo(CandidateResult candid) {
        double otherAmount = candid.score;
        if (score == otherAmount)
            return 0;
        else if (score > otherAmount)
            return -1;
        else
            return 1;
    }

    @Override
    public int compare(CandidateResult c1, CandidateResult c2) {
        return Double.compare(c1.getLambdaScore(), c2.getLambdaScore());
    }
}
