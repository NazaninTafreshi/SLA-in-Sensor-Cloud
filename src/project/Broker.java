package project;

import java.util.ArrayList;

/**
 * Created by Ayoubi on 24/04/2016.
 */
public class Broker {

    private int id;
    private int cost;
    private String appCon;
    private String cloudCon;
    private float x, y;

    private ArrayList<ArrayList> cloudProviders;
    private ArrayList<ArrayList<Integer[]>> RtCloudProviders;
    private ArrayList<Application> applications;
    private ArrayList<CandidateResult> CandidateResultForAllApplication;
    private Integer[] costForCloudCon;

    public Broker(int id) {
        cloudProviders = new ArrayList<>();
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getCost() {
        return cost;
    }

    public Broker setCost(int cost) {
        this.cost = cost;
        return this;
    }

    public String getAppCon() {
        return appCon;
    }

    public void setAppCon(String appCon) {
        this.appCon = appCon;
    }

    public String getCloudCon() {
        return cloudCon;
    }

    public void setCloudCon(String cloudCon) {
        this.cloudCon = cloudCon;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public ArrayList<ArrayList> getCloudProviders() {
        return cloudProviders;
    }

    public void setCloudProviders(ArrayList<ArrayList> cloudProviders) {
        this.cloudProviders = cloudProviders;
    }

    public ArrayList<ArrayList<Integer[]>> getRtCloudProviders() {
        return RtCloudProviders;
    }

    public void setRtCloudProviders(ArrayList<ArrayList<Integer[]>> rtCloudProviders) {
        RtCloudProviders = rtCloudProviders;
    }

    public ArrayList<Application> getApplications() {
        return applications;
    }

    public void setApplications(ArrayList<Application> applications) {
        this.applications = applications;
    }

    public ArrayList<CandidateResult> getCandidateResultForAllApplication() {
        return CandidateResultForAllApplication;
    }

    public void setCandidateResultForAllApplication(ArrayList<CandidateResult> candidateResultForAllApplication) {
        CandidateResultForAllApplication = candidateResultForAllApplication;
    }
}
