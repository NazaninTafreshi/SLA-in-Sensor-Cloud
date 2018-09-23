package project;

/**
 * Created by Ayoubi on 23/09/2016.
 */
public class ConstantCloudProvider {
    private final int cost;
    private final int responseTime[] = new int[2];


    public ConstantCloudProvider( int cost, int responseTime[]) {
        this.cost = cost;
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
}
