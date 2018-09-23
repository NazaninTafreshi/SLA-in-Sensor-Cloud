package project;

import java.util.ArrayList;

/**
 * Created by Ayoubi on 25/04/2016.
 */
public class CloudProvider {

    private int id;
    private int cost;
    private int responseTime[] = new int[2];
    private String brokerCon;
    private String sensorCon;
    private float x, y;

    private ArrayList<SensorNetwork> sensorNetworks;

    // collectiveCost, collectiveResponseTime and taskType
    private ArrayList<Integer> costForSensorCon;
    private ArrayList<Integer[]> responseTimeForSensorCon;
    private ArrayList<String> taskTypeForSensorCon;


    public CloudProvider(int id) {
        sensorNetworks = new ArrayList<>();
        costForSensorCon = new ArrayList<>();
        responseTimeForSensorCon = new ArrayList<>();
        taskTypeForSensorCon = new ArrayList<>();

        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int[] getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(int[] responseTime) {
        this.responseTime = BasicInformation.responseTimeMethod(responseTime);
    }

    public String getBrokerCon() {
        return brokerCon;
    }

    public void setBrokerCon(String brokerCon) {
        this.brokerCon = brokerCon;
    }

    public String getSensorCon() {
        return sensorCon;
    }

    public void setSensorCon(String sensorCon) {
        this.sensorCon = sensorCon;
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

    public ArrayList<SensorNetwork> getSensorNetworks() {
        return sensorNetworks;
    }

    public void setSensorNetworks(ArrayList<SensorNetwork> sensorNetworks) {
        this.sensorNetworks = sensorNetworks;

        costForSensorCon.clear();
        responseTimeForSensorCon.clear();
        taskTypeForSensorCon.clear();

        for (int i = 0; i < sensorNetworks.size(); i++) {
            costForSensorCon.add(cost + sensorNetworks.get(i).getCost());
            Integer[] temp = new Integer[2];
            int temp0 = sensorNetworks.get(i).getResponseTime()[0] + responseTime[0];
            int temp1 = sensorNetworks.get(i).getResponseTime()[1] + responseTime[1];
            temp[0] = temp0;
            temp[1] = temp1;
            responseTimeForSensorCon.add(temp);
            taskTypeForSensorCon.add(sensorNetworks.get(i).getTaskType());
        }
    }

    public ArrayList<Integer> getCostForSensorCon() {
        return costForSensorCon;
    }

    public ArrayList<Integer[]> getResponseTimeForSensorCon() {
        return responseTimeForSensorCon;
    }

    public ArrayList<String> getTaskTypeForSensorCon() {
        return taskTypeForSensorCon;
    }

}
