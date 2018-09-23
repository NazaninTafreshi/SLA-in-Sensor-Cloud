package project;


/**
 * Created by Ayoubi on 24/04/2016.
 */
public class SensorNetwork {
    private int id;
    private int cost;
    private int responseTime[] = new int[2];
    private String taskType;
    private String cloudCon;
    private float x, y;

    public SensorNetwork(int id) {
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

    public SensorNetwork setResponseTime(int[] responseTime) {

        this.responseTime = BasicInformation.responseTimeMethod(responseTime);
        return this;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
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

}
