package project;

import java.util.*;

/**
 * Created by Ayoubi on 24/04/2016.
 */
public class Configurer {

    private final static List<String> taskTypeList = new ArrayList<>();
    private String taskType;

    /* block initialization */
    {
        taskTypeList.add("Area monitoring");
        taskTypeList.add("Health care monitoring");
//        taskTypeList.add("Air pollution monitoring");
//        taskTypeList.add("Forest fire detection");
//        taskTypeList.add("Landslide detection");
//        taskTypeList.add("Water quality monitoring");
//        taskTypeList.add("Natural disaster prevention");
//        taskTypeList.add("Industrial monitoring");
//        taskTypeList.add("Machine health monitoring");
//        taskTypeList.add("Waste water monitoring");
//        taskTypeList.add("Structural health monitoring");

    }

    public void setTaskType(int type) {
        taskType = taskTypeList.get(type);
    }

    public String getTaskType() {
        return taskType;
    }

    /**
     * generateRandomNumber method generate random number between two integer
     *
     * @param low  integer
     * @param high integer
     * @return integer number between two input parameter that first is smaller than second
     */
    public static int generateRandomNumber(int low, int high) {
        Random rn = new Random();
        return rn.nextInt(high - low) + low;
    }

    /**
     * generateRandomNumber method generate random number between 0 and one integer
     *
     * @param number integer
     * @return integer number between 0 and input parameter
     */
    public static int generateRandomNumber(int number) {
        Random rn = new Random();
        int num = rn.nextInt(number);
        return num;
    }


    /**
     * generateTaskType method generate random number for select on of the taskType in the taskTypeList.
     *
     * @return integer random number in the range of taskTypeSet member
     */
    public static int generateTaskType() {
        Random rn = new Random();
        return rn.nextInt(taskTypeList.size() - 1);
    }

    //  /** generate random number
//     * @param
//     * @return double random number between 0 and 1
//     */
//    public static double generateRandomNumber() {
//        double num =  Math.random();
//        return (double)Math.round(num*1000)/1000;
//
//    }

//    public static double generateRandomDoubleNumber(double low, double high) {
//        double random = new Random().nextDouble();
//        Double first = low + (random * (high - low));
//        return (double) Math.round(first * 10) / 10;
//    }

    //    /**
//     * generate random number for Sla parameter like minimum and maximum of
//     * response time, cost and Etc.
//     *
//     * @param low integer
//     * @param high integer
//     * @return integer random number between low and high parameter
//     */
//    public static int generateRandomNumber(int low, int high) {
//        Random rn = new Random();
//        int first = rn.nextInt(high - low) + low;
//        int mod = first % 5;
//        int end = first - mod;
//        if (end == 0) {
//            end += 5;
//        }
//        return end;
//    }

}
