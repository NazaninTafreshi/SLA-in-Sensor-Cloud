package project;

import static project.Configurer.generateRandomNumber;

/**
 * Created by Ayoubi on 27/04/2016.
 */
public class BasicInformation {

    // Number of application in json file
    private static int appNumber;

    // Number of broker in json file
    private static int brokerNumber;

    // Number of cloudProvider in arrayList:
    private static int cloudNumber;

    // Number sensorNetwork in json file
    private static int sensorNumber;

    //maximum and minimum cost and responseTime that sensorNetworks generate
    final static int minCostSn = 300, maxCostSn = 350;
    final static int lowMinRtSn = 15, upMinRtSn = 25, lowMaxRtSn = 20, upMaxRtSn = 35;

    //maximum and minimum cost and responseTime that cloudProviders generate
    final static int lowCostCP = 200, upCostCP = 250;
    final static int lowMinRtCP = 5, upMinRtCP = 10, lowMaxRtCP = 10, upMaxRtCP = 20;

    //maximum and minimum cost and responseTime that applications generate
    public final static int minCostApp = 490, maxCostApp = 670;
    public final static int lowMinRtApp = 20, upMinRtApp = 35;
    public final static int lowMaxRtApp = 30, upMaxRtApp = 60;

    private static String json;

    private BasicInformation() {
    }

    public static int getAppNumber() {
        return appNumber;
    }

    public static void setAppNumber(int appNumber) {
        BasicInformation.appNumber = appNumber;
    }

    public static int getBrokerNumber() {
        return brokerNumber;
    }

    public static void setBrokerNumber(int brokerNumber) {
        BasicInformation.brokerNumber = brokerNumber;
    }

    public static int getCloudNumber() {
        return cloudNumber;
    }

    public static void setCloudNumber(int cloudNumber) {
        BasicInformation.cloudNumber = cloudNumber;
    }

    public static int getSensorNumber() {
        return sensorNumber;
    }

    public static void setSensorNumber(int sensorNumber) {
        BasicInformation.sensorNumber = sensorNumber;
    }

    /**
     * responseTimeMethod get responseTime array,
     * responseTime[0] should be smaller than responseTime[1] so if responseTime[0] is greater than responseTime[1] swap them and
     * if responseTime[0] equal to responseTime[1] an integer number add to responseTime[1]
     *
     * @param responseTime array that contains two integer number for minRt and maxRt respectively
     * @return int[] contains minRt and maxRt respectively
     */
    public static int[] responseTimeMethod(int[] responseTime) {
        int[] result;
        if (responseTime[0] > responseTime[1]) {
            int temp = responseTime[1];
            responseTime[1] = responseTime[0];
            responseTime[0] = temp;
        }

        if (responseTime[0] == responseTime[1]) {
            responseTime[1] += generateRandomNumber(5, 10);
        }

        result = responseTime;
        return result;
    }

    public static String getJsonFile() {
        return json;
    }

    public static void setJsonFile(String json) {
        BasicInformation.json = json;
    }

}
