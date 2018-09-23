package project;

import java.util.ArrayList;

/**
 * Created by Ayoubi on 12/08/2016.
 */
public class GlobalBroker {

    private int id;
    private int cost;
    private ArrayList<ArrayList> cloudProviders;
    private ArrayList<ArrayList<Integer[]>> RtCloudProviders;
    private ArrayList<Application> applications;
    private ArrayList<CandidateResult> CandidateResultForAllApplication;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCost() {
        return cost;
    }

    public GlobalBroker setCost(int cost) {
        this.cost = cost;
        return this;
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
