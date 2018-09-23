package project;

import java.util.ArrayList;

/**
 * Created by Ayoubi on 23/09/2016.
 */
public class StaticObjects {
    public static final ArrayList<ConstantApplication> constantApplications = new ArrayList<>();
    public static final ArrayList<ConstantCloudProvider> constantCloudProviders = new ArrayList<>();
    public static final ArrayList<ConstantSensorNetwork> constantSensorNetworks = new ArrayList<>();
    public static boolean appFlag;
    public static boolean clFlag;
    public static boolean snFlag;
    public static boolean sensor;
    public static boolean cloud;
    public static boolean app;
 
    private static String cycleC;

    public int getCycleC() {
        return Integer.parseInt(cycleC);
    }

    public void setCycleC(String cycleC) {
        this.cycleC = cycleC;
    }
    public static boolean isAppFlag() {
        return appFlag;
    }

    public static void setAppFlag(boolean appFlag) {
        StaticObjects.appFlag = appFlag;
    }

    public static boolean isClFlag() {
        return clFlag;
    }

    public static void setClFlag(boolean clFlag) {
        StaticObjects.clFlag = clFlag;
    }

    public static boolean isSnFlag() {
        return snFlag;
    }

    public static void setSnFlag(boolean snFlag) {
        StaticObjects.snFlag = snFlag;
    }

    public static boolean isSensor() {
        return sensor;
    }

    public static void setSensor(boolean sensor) {
        StaticObjects.sensor = sensor;
    }

    public static boolean isCloud() {
        return cloud;
    }

    public static void setCloud(boolean cloud) {
        StaticObjects.cloud = cloud;
    }

    public static boolean isApp() {
        return app;
    }

    public static void setApp(boolean app) {
        StaticObjects.app = app;
    }
}
