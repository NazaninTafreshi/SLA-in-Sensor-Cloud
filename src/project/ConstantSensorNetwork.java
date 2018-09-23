package project;

/**
 * Created by Ayoubi on 24/09/2016.
 */
public class ConstantSensorNetwork {
    private final int cost;
    private final int responseTime[] = new int[2];
    private final String taskType;

    public ConstantSensorNetwork(int cost, int[] responseTime, String taskType) {
        this.cost = cost;
        this.taskType = taskType;
        responseTime = BasicInformation.responseTimeMethod(responseTime);
        this.responseTime[0] = responseTime[0];
        this.responseTime[1] = responseTime[1];
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
}
