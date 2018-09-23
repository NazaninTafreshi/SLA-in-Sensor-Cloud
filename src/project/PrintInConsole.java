package project;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Ayoubi on 17/08/2016.
 */
public class PrintInConsole {

    void printAttributes(Manager manager) {
        //==============================================================================================================
        //==============================================================================================================
        //==================================== Print attribute of sensor network, ======================================
        //================================= cloud provider, broker and application======================================
        //==============================================================================================================
        //==============================================================================================================

        for (int i = 0; i < BasicInformation.getSensorNumber(); i++) {
            System.out.println("\nSensorNetwork id is " + manager.getSensorNetwork(i).getId() + ":\n"
                    + "cost of sensorNetwork:" + manager.getSensorNetwork(i).getCost() + "\nResponse time is "
                    + Arrays.toString(manager.getSensorNetwork(i).getResponseTime()) + "\ntaskType is : "
                    + manager.getSensorNetwork(i).getTaskType() + "\nCloudCon is: "
                    + manager.getSensorNetwork(i).getCloudCon() + "\n");
        }

        System.out.println("______________________________________________________________" + "\n");

        for (int i = 0; i < BasicInformation.getCloudNumber(); i++) {
            System.out.println("Cloud provider id is: " + manager.getCloudProvider(i).getId() + "\ncost of cloud provider is: "
                    + manager.getCloudProvider(i).getCost() + "\nResponse time is: "
                    + Arrays.toString(manager.getCloudProvider(i).getResponseTime()) + "\nBrokerCon is: "
                    + manager.getCloudProvider(i).getBrokerCon() + "\nSensorCon is: "
                    + manager.getCloudProvider(i).getSensorCon());

            int cloudSensorSize = manager.getCloudProvider(i).getSensorNetworks().size(); // number sensor connected to cloud
            System.out.println("cloud " + manager.getCloudProvider(i).getId() + " has " + cloudSensorSize + " sensorNetwork:");
            for (int j = 0; j < cloudSensorSize; j++) {
                System.out.println(
                        "cost for sensor " + manager.getCloudProvider(i).getSensorNetworks().get(j).getId()
                                + " that connect with this cloud is: "
                                + manager.getCloudProvider(i).getSensorNetworks().get(j).getCost());
                System.out.println("Response Time for sensor "
                        + manager.getCloudProvider(i).getSensorNetworks().get(j).getId() + " is: "
                        + Arrays.toString(manager.getCloudProvider(i).getSensorNetworks().get(j).getResponseTime()));
                System.out.println(
                        "TaskType for sensor " + manager.getCloudProvider(i).getSensorNetworks().get(j).getId()
                                + " is: " + manager.getCloudProvider(i).getTaskTypeForSensorCon());
            }
            System.out.println("cost sensor plus cost cloud: " + manager.getCloudProvider(i).getCostForSensorCon());
            for (int k = 0; k < manager.getCloudProvider(i).getResponseTimeForSensorCon().size(); k++) {
                System.out.println("Response Time  sensor plus cloud:"
                        + Arrays.toString(manager.getCloudProvider(i).getResponseTimeForSensorCon().get(k)));
            }
            System.out.println();
        }
        System.out.println("______________________________________________________________" + "\n");

        for (int i = 0; i < BasicInformation.getBrokerNumber(); i++) {
            System.out.println("Broker id is: " + manager.getBroker(i).getId());
            System.out.println("appCon is: " + manager.getBroker(i).getAppCon() + "\nCloudCon is: "
                    + manager.getBroker(i).getCloudCon());
            System.out.println("Broker cost is: " + manager.getBroker(i).getCost());
            //for example if manager.broker.get(i).getCloudProviders().size() is 12 then brokerCloudSize is 12/4 = 3 that means broker connect to three cloud
            int brokerCloudSize = manager.getBroker(i).getCloudProviders().size() / 4;
            System.out.println(
                    "broker " + manager.getBroker(i).getId() + " has " + brokerCloudSize + " cloudProvider" + "\n");
            for (int j = 0; j < brokerCloudSize; j++) {
                System.out.println("cost for cloud " + manager.getBroker(i).getCloudProviders().get((j * 4))
                        + " that connect with this broker is: "
                        + manager.getBroker(i).getCloudProviders().get(j * 4 + 1));
                System.out.println("cost broker plus cloud " + manager.getBroker(i).getCloudProviders().get((j * 4))
                        + " that connect with this broker is: "
                        + manager.getBroker(i).getCloudProviders().get(j * 4 + 3));
                System.out.println("Task Type for this cloud is: "
                        + manager.getBroker(i).getCloudProviders().get(j * 4 + 2));
                for (int k = 0; k < manager.getBroker(i).getRtCloudProviders().get(j).size(); k++) {
                    System.out.println("Rt for cloud that connect with this broker is: "
                            + Arrays.toString(manager.getBroker(i).getRtCloudProviders().get(j).get(k)));
                }
            }
            System.out.println();

            int brokerAppSize = manager.getBroker(i).getApplications().size();
            System.out.println("broker " + manager.getBroker(i).getId() + " connect to " + brokerAppSize + " Application");
            for (int k = 0; k < brokerAppSize; k++) {
                System.out.println("cost of application " + manager.getBroker(i).getApplications().get(k).getId()
                        + " that connect to this broker is: " + manager.getBroker(i).getApplications().get(k).getCost()
                        + "\nresponse time of App is: "
                        + Arrays.toString(manager.getBroker(i).getApplications().get(k).getResponseTime())
                        + "\ntask type is: " + manager.getBroker(i).getApplications().get(k).getTaskType() + "\n");

            }
        }
        System.out.println("______________________________________________________________" + "\n");

        for (int i = 0; i < BasicInformation.getAppNumber(); i++) {
            double sum = manager.getApplication(i).getWeightCost() + manager.getApplication(i).getWeightMaxRt()
                    + manager.getApplication(i).getWeightMinRt();
            System.out.println("Application id is " + manager.getApplication(i).getId() + ":\n" + "cost of App:"
                    + manager.getApplication(i).getCost() + "\nResponse time is "
                    + Arrays.toString(manager.getApplication(i).getResponseTime()) + "\ntask type is:  "
                    + manager.getApplication(i).getTaskType() + "\nBrokerCon is: "
                    + manager.getApplication(i).getBrokerCon()
                    + "\nweight for cost: " + manager.getApplication(i).getWeightCost()
                    + "\nweight for minRt: " + manager.getApplication(i).getWeightMinRt()
                    + "\nweight for maxRt: " + manager.getApplication(i).getWeightMaxRt()
                    + "\nsum of weight: " + sum
                    + "\n");
        }
        // =============================================================================================================
        // =============================================================================================================
        // =============================================================================================================
    }

    //==================================================================================================================
    //======================================== Print attribute of globalBroker =========================================
    //==================================================================================================================
    void printGlobalBroker(GlobalBroker globalBroker) {

        //for example if globalBroker.getCloudProviders().size() is 16 then globalBrokerCloudSize is 16/4 = 4 that means
        // broker connect to four cloud
        int globalBrokerCloudSize = globalBroker.getCloudProviders().size() / 4;
        System.out.print("\u001B[35m" + "globalBroker has " + globalBrokerCloudSize + " cloudProvider" + "\n");
        System.out.print("\u001B[38m");
        for (int i = 0; i < globalBrokerCloudSize; i++) {
            System.out.println("cost for globalBroker is: " + globalBroker.getCost());
            System.out.println("cost for cloud " + globalBroker.getCloudProviders().get((i * 4))
                    + " that connect with this broker is: "
                    + globalBroker.getCloudProviders().get(i * 4 + 1));
            System.out.println(
                    "Task Type for this cloud is: " + globalBroker.getCloudProviders().get(i * 4 + 2));
            System.out.println("cost globalBroker plus cloud " + globalBroker.getCloudProviders().get((i * 4))
                    + " that connect with this broker is: "
                    + globalBroker.getCloudProviders().get(i * 4 + 3));
            for (int j = 0; j < globalBroker.getRtCloudProviders().get(i).size(); j++) {
                System.out.println("Rt for cloud that connect with this broker is: "
                        + Arrays.toString(globalBroker.getRtCloudProviders().get(i).get(j)));
            }
            System.out.println();
        }
        int globalBrokerAppSize = globalBroker.getApplications().size();
        System.out.println("\u001B[35m" + "globalBroker " + " connect to " + globalBrokerAppSize + " Application");
        System.out.print("\u001B[38m");
        for (int k = 0; k < globalBrokerAppSize; k++) {
            System.out.println("cost of application " + globalBroker.getApplications().get(k).getId()
                    + " that connect to this broker is: " + globalBroker.getApplications().get(k).getCost()
                    + "\nresponse time of App is: "
                    + Arrays.toString(globalBroker.getApplications().get(k).getResponseTime())
                    + "\ntask type is: " + globalBroker.getApplications().get(k).getTaskType() + "\n");

        }
    }
    // =================================================================================================================
    // =================================================================================================================

    void printFirstCalculateResult(Leader leader) {
        System.out.println();
        System.out.println("===================================================");
        for (int i = 0; i < BasicInformation.getBrokerNumber(); i++) {
            System.out.println("\u001B[31m" + "\nbroker " + leader.getManager().getBroker(i).getId() + ":");
            if (leader.getManager().getBroker(i).getCandidateResultForAllApplication().isEmpty()) {
                System.out.println("\u001B[30m" + "no results found in broker " + leader.getManager().getBroker(i).getId()
                        + " for none of the application");
            } else {
                for (int j = 0; j < leader.getManager().getBroker(i).getCandidateResultForAllApplication().size(); j++) {
                    System.out.print("\u001B[30m" + "\napplication id: " + leader.getManager().getBroker(i).getCandidateResultForAllApplication().get(j).getApplicationId()
                            + "\ncloud id: " + leader.getManager().getBroker(i).getCandidateResultForAllApplication().get(j).getCloudId()
                            + "\ntotal cost: " + leader.getManager().getBroker(i).getCandidateResultForAllApplication().get(j).getTotalCost()
                            + "\ntotal Response time: " + Arrays.toString(leader.getManager().getBroker(i).getCandidateResultForAllApplication().get(j).getTotalResponseTime())
                            + "\ntask type: " + leader.getManager().getBroker(i).getCandidateResultForAllApplication().get(j).getTaskType() + "\n");
                    System.out.println("score: " + leader.getManager().getBroker(i).getCandidateResultForAllApplication().get(j).getScore());
                }
            }
        }
        // MAY BE NOT Usefully
        System.out.println("===================================================");
    }

    void printSecondCalculateResultSAW(Leader leader) {
        System.out.print("\u001B[1m");
        System.out.println("\u001B[1m" + "Result Found For Application " + leader.mostAppropriateResultSAW_L.getApplicationId() + " in Broker "
                + leader.mostAppropriateResultSAW_L.getBrokerId() + " is:");
        System.out.print("\u001B[47m");
        System.out.print("cloud id: " + leader.mostAppropriateResultSAW_L.getCloudId()
                + "\ntotal cost: " + leader.mostAppropriateResultSAW_L.getTotalCost()
                + "\ntotal Response time: " + Arrays.toString(leader.mostAppropriateResultSAW_L.getTotalResponseTime())
                + "\ntask type: " + leader.mostAppropriateResultSAW_L.getTaskType() + "\n"
                + "score: " + leader.mostAppropriateResultSAW_L.getScore() + "\n");
        System.out.println("\u001B[48m");
    }

    void printUltimateResultSAW(int i, Leader leader) {
        System.out.print("\u001B[1m");
        System.out.println("\u001B[1m" + "\nfinal Result that Application " + leader.ultimateResultSAW_L.get(i).getApplicationId() + " select is:");
        System.out.print("\u001B[34m");
        System.out.print("\u001B[1m");
        System.out.print("broker id:" + leader.ultimateResultSAW_L.get(i).getBrokerId() + "\ncloud id: " + leader.ultimateResultSAW_L.get(i).getCloudId()
                + "\ntotal cost: " + leader.ultimateResultSAW_L.get(i).getTotalCost()
                + "\ntotal Response time: " + Arrays.toString(leader.ultimateResultSAW_L.get(i).getTotalResponseTime())
                + "\ntask type: " + leader.ultimateResultSAW_L.get(i).getTaskType() + "\n"
                + "score: " + leader.ultimateResultSAW_L.get(i).getScore() + "\n");
        System.out.print("\u001B[48m");
    }

    void printUltimateResultSAW_M(int i, Leader leader) {
        System.out.print("\u001B[1m");
        System.out.println("\u001B[1m" + "\nmaximum score Result that Application " + leader.ultimateResultSAW_L_M.get(i).getApplicationId() + " select is:");
        System.out.print("\u001B[34m");
        System.out.print("\u001B[1m");
        System.out.print("broker id:" + leader.ultimateResultSAW_L_M.get(i).getBrokerId() + "\ncloud id: " + leader.ultimateResultSAW_L_M.get(i).getCloudId()
                + "\ntotal cost: " + leader.ultimateResultSAW_L_M.get(i).getTotalCost()
                + "\ntotal Response time: " + Arrays.toString(leader.ultimateResultSAW_L_M.get(i).getTotalResponseTime())
                + "\ntask type: " + leader.ultimateResultSAW_L_M.get(i).getTaskType() + "\n"
                + "score: " + leader.ultimateResultSAW_L_M.get(i).getScore() + "\n");
        System.out.print("\u001B[48m");
    }

    void printUltimateResultTOPSIS(int i, Leader leader) {
        System.out.print("\u001B[1m");
        System.out.println("\u001B[1m" + "\nfinal Result that Application " + leader.ultimateResultTOPSIS_L.get(i).getApplicationId() + " select is:");
        System.out.print("\u001B[34m");
        System.out.print("\u001B[1m");
        System.out.print("broker id:" + leader.ultimateResultTOPSIS_L.get(i).getBrokerId() + "\ncloud id: " + leader.ultimateResultTOPSIS_L.get(i).getCloudId()
                + "\ntotal cost: " + leader.ultimateResultTOPSIS_L.get(i).getTotalCost()
                + "\ntotal Response time: " + Arrays.toString(leader.ultimateResultTOPSIS_L.get(i).getTotalResponseTime())
                + "\ntask type: " + leader.ultimateResultTOPSIS_L.get(i).getTaskType() + "\n"
                + "score: " + leader.ultimateResultTOPSIS_L.get(i).getScore() + "\n");
        System.out.print("\u001B[48m");
    }

    void printUltimateResultTOPSIS_M(int i, Leader leader) {
        System.out.print("\u001B[1m");
        System.out.println("\u001B[1m" + "\nmaximum score Result that Application " + leader.ultimateResultTOPSIS_L_M.get(i).getApplicationId() + " select is:");
        System.out.print("\u001B[34m");
        System.out.print("\u001B[1m");
        System.out.print("broker id:" + leader.ultimateResultTOPSIS_L_M.get(i).getBrokerId() + "\ncloud id: " + leader.ultimateResultTOPSIS_L_M.get(i).getCloudId()
                + "\ntotal cost: " + leader.ultimateResultTOPSIS_L_M.get(i).getTotalCost()
                + "\ntotal Response time: " + Arrays.toString(leader.ultimateResultTOPSIS_L_M.get(i).getTotalResponseTime())
                + "\ntask type: " + leader.ultimateResultTOPSIS_L_M.get(i).getTaskType() + "\n"
                + "score: " + leader.ultimateResultTOPSIS_L_M.get(i).getScore() + "\n");
        System.out.print("\u001B[48m");
    }

    void printNoResultForEachBroker(int i, int j, Leader leader) {
        System.out.print("\u001B[31m");
        System.out.print("\u001B[1m");
        System.out.println("No Result Found For Application " + leader.manager.getBroker(i).getApplications().get(j).getId()
                + " In Broker " + leader.manager.getBroker(i).getId());
        System.out.print("\u001B[48m");
    }


    void gBPrintSecondCalculateResultSAW(Leader leader) {
        System.out.print("\u001B[1m");
        System.out.println("\u001B[1m" + "Result Found For Application " + leader.mostAppropriateResultSAW_G.getApplicationId() + " in globalBroker is:");
        System.out.print("\u001B[47m");
        System.out.print("cloud id: " + leader.mostAppropriateResultSAW_G.getCloudId()
                + "\ntotal cost: " + leader.mostAppropriateResultSAW_G.getTotalCost()
                + "\ntotal Response time: " + Arrays.toString(leader.mostAppropriateResultSAW_G.getTotalResponseTime())
                + "\ntask type: " + leader.mostAppropriateResultSAW_G.getTaskType() + "\n"
                + "score: " + leader.mostAppropriateResultSAW_G.getScore() + "\n");
        System.out.println("\u001B[48m");
    }

    void printNoResultForGlobalBroker(int j, Leader leader) {
        System.out.print("\u001B[31m");
        System.out.print("\u001B[1m");
        System.out.println("No Result Found For Application " + leader.globalBroker.getApplications().get(j).getId() +
                " In globalBroker ");
        System.out.print("\u001B[48m");
    }

    void gBPrintSecondCalculateResultTOPSIS(Leader leader) {
        System.out.print("\u001B[1m");
        System.out.println("\u001B[1m" + "Result Found For Application " + leader.mostAppropriateResultTOPSIS_G.getApplicationId() + " in globalBroker is:");
        System.out.print("\u001B[47m");
        System.out.print("cloud id: " + leader.mostAppropriateResultTOPSIS_G.getCloudId()
                + "\ntotal cost: " + leader.mostAppropriateResultTOPSIS_G.getTotalCost()
                + "\ntotal Response time: " + Arrays.toString(leader.mostAppropriateResultTOPSIS_G.getTotalResponseTime())
                + "\ntask type: " + leader.mostAppropriateResultTOPSIS_G.getTaskType() + "\n"
                + "score: " + leader.mostAppropriateResultTOPSIS_G.getScore() + "\n");
        System.out.println("\u001B[48m");
    }

    void printUltimateResultSAWGB(int i, Leader leader) {
        System.out.print("\u001B[1m");
        System.out.println("\u001B[1m" + "\nfinal Result that Application " + leader.ultimateResultSAW_G.get(i).getApplicationId() + " select is:");
        System.out.print("\u001B[34m");
        System.out.print("\u001B[1m");
        System.out.print("broker id:" + leader.ultimateResultSAW_G.get(i).getBrokerId() + "\ncloud id: " + leader.ultimateResultSAW_G.get(i).getCloudId()
                + "\ntotal cost: " + leader.ultimateResultSAW_G.get(i).getTotalCost()
                + "\ntotal Response time: " + Arrays.toString(leader.ultimateResultSAW_G.get(i).getTotalResponseTime())
                + "\ntask type: " + leader.ultimateResultSAW_G.get(i).getTaskType() + "\n"
                + "score: " + leader.ultimateResultSAW_G.get(i).getScore() + "\n");
        System.out.print("\u001B[48m");
    }

    void printUltimateResultTOPSISGB(int i, Leader leader) {
        System.out.print("\u001B[1m");
        System.out.println("\u001B[1m" + "\nfinal Result that Application " + leader.ultimateResultTOPSIS_G.get(i).getApplicationId() + " select is:");
        System.out.print("\u001B[34m");
        System.out.print("\u001B[1m");
        System.out.print("broker id:" + leader.ultimateResultTOPSIS_G.get(i).getBrokerId() + "\ncloud id: " + leader.ultimateResultTOPSIS_G.get(i).getCloudId()
                + "\ntotal cost: " + leader.ultimateResultTOPSIS_G.get(i).getTotalCost()
                + "\ntotal Response time: " + Arrays.toString(leader.ultimateResultTOPSIS_G.get(i).getTotalResponseTime())
                + "\ntask type: " + leader.ultimateResultTOPSIS_G.get(i).getTaskType() + "\n"
                + "score: " + leader.ultimateResultTOPSIS_G.get(i).getScore() + "\n");
        System.out.print("\u001B[48m");
    }

    void printSecondCalculateResultTOPSIS(Leader leader) {
        System.out.print("\u001B[1m");
        System.out.println("\u001B[1m" + "Result Found For Application " + leader.mostAppropriateResultTOPSIS_L.getApplicationId() + " in Broker "
                + leader.mostAppropriateResultTOPSIS_L.getBrokerId() + " is:");
        System.out.print("\u001B[47m");
        System.out.print("cloud id: " + leader.mostAppropriateResultTOPSIS_L.getCloudId()
                + "\ntotal cost: " + leader.mostAppropriateResultTOPSIS_L.getTotalCost()
                + "\ntotal Response time: " + Arrays.toString(leader.mostAppropriateResultTOPSIS_L.getTotalResponseTime())
                + "\ntask type: " + leader.mostAppropriateResultTOPSIS_L.getTaskType() + "\n"
                + "score: " + leader.mostAppropriateResultTOPSIS_L.getScore() + "\n");
        System.out.println("\u001B[48m");
    }

//    public void print_SAW_G_First_Rank(Leader leader) {
//        System.out.println(" First Rank: SAW_Global ");
//        for (int i = 0; i < leader.firstSortResultSAW_G.size(); i++) {
//            System.out.println("FF****FF " + leader.firstSortResultSAW_G.get(i).getTotalCost() + " "
//                    + Arrays.toString(leader.firstSortResultSAW_G.get(i).getTotalResponseTime()) + " BId "
//                    + leader.firstSortResultSAW_G.get(i).getBrokerId()
//                    + " AppId " + leader.firstSortResultSAW_G.get(i).getApplicationId() + " CloudId "
//                    + leader.firstSortResultSAW_G.get(i).getCloudId() + "  " + leader.firstSortResultSAW_G.get(i).getScore());
//        }
//        System.out.println("==========================================");
//    }

    public void print_Rank(ArrayList<CandidateResult> candidateResults, boolean firstSecond, int method_SAW_TOPSIS_ETOPSIS) {
        if (method_SAW_TOPSIS_ETOPSIS == 0) {
            System.out.println("===============    Rank: SAW_Global   ===============");
        } else if (method_SAW_TOPSIS_ETOPSIS == 1) {
            System.out.println("===============    Rank: TOPSIS_Global   ===============");
        } else {
            System.out.println("===============    Rank: E_TOPSIS_Global   ===============");
        }

        if (firstSecond) {
            System.out.println("//   FIRST   //");
        } else {
            System.out.println("//   SECOND   //");
        }

        for (int i = 0; i < candidateResults.size(); i++) {
            System.out.println(candidateResults.get(i).getTotalCost() + " "
                    + Arrays.toString(candidateResults.get(i).getTotalResponseTime()) + " BId "
                    + candidateResults.get(i).getBrokerId()
                    + " AppId " + candidateResults.get(i).getApplicationId() + " CloudId "
                    + candidateResults.get(i).getCloudId() + "  " + candidateResults.get(i).getScore()
                    + "     " + candidateResults.get(i).getLambdaScore());
        }
    }


    public void printRankinAbnormalityAndUniformity(ArrayList<CandidateResult> firstSortResult, ArrayList<CandidateResult> secondSortResult) {
        boolean flag = true;
        if (firstSortResult.get(0).getScore() == 1 && secondSortResult.get(0).getScore() == 1) {
            System.out.println("all att in first alt is the min and so other alt is zero");
            System.out.println(secondSortResult.get(0).getApplicationId() + "  " + "0000");
        } else {
            for (int i = 0; i < firstSortResult.size(); i++) {
                int jk = i + 1;
                if (firstSortResult.get(i).getCloudId() != secondSortResult.get(i).getCloudId() ||
                        firstSortResult.get(i).getTotalCost() != secondSortResult.get(i).getTotalCost() ||
                        firstSortResult.get(i).getTotalResponseTime() != secondSortResult.get(i).getTotalResponseTime()) {
                    System.out.println("ranking abnormal     " + "AppId  " + firstSortResult.get(i).getApplicationId() + " "
                            + secondSortResult.get(i).getApplicationId() + " " + "rank" + jk);
                    flag = false;
                }
            }
            if (flag) {
                System.out.println(secondSortResult.get(0).getApplicationId() + "  " + 1111);
            }
        }
    }


    public void printRankinAbnormalityAndUniformityETOPSIS(ArrayList<CandidateResult> firstSortResult, ArrayList<CandidateResult> secondSortResult) {
        boolean flag = true;
        if ((firstSortResult.get(0).getLambdaScore() == 0.8 && secondSortResult.get(0).getLambdaScore() == 0.8) ||
                (firstSortResult.get(0).getLambdaScore() == .889 && secondSortResult.get(0).getLambdaScore() == .889)) {
            System.out.println("first is one and other is zero");
            System.out.println(secondSortResult.get(0).getApplicationId() + "  " + 0000);
        } else {
            for (int i = 0; i < firstSortResult.size(); i++) {
                int jk = i + 1;
                if (firstSortResult.get(i).getCloudId() != secondSortResult.get(i).getCloudId() ||
                        firstSortResult.get(i).getTotalCost() != secondSortResult.get(i).getTotalCost() ||
                        firstSortResult.get(i).getTotalResponseTime() != secondSortResult.get(i).getTotalResponseTime()) {
                    System.out.println("ranking abnormal     " + "AppId  " + firstSortResult.get(i).getApplicationId() + " "
                            + secondSortResult.get(i).getApplicationId() + " " + "rank" + jk);
                    flag = false;
                }
            }
            if (flag) {
                System.out.println(secondSortResult.get(0).getApplicationId() + "  " + 1111);
            }
        }
    }

    public void printPrecision(ArrayList<CandidateResult> sortResult, int methodSAW_TOPSIS_ETOPSIS) {
        if (methodSAW_TOPSIS_ETOPSIS == 0 || methodSAW_TOPSIS_ETOPSIS == 1) {
            System.out.println("******    SAW and TOPSIS Precision  ******");
            for (int i = 0; i < sortResult.size() - 1; i++) {
                int subsequent = i + 1;
                Double number = (double) Math.round((sortResult.get(i).getScore() - sortResult.get(subsequent).getScore()) * 10000) / 10000;
                System.out.println(number);
            }
        } else {
            System.out.println("******    E-TOPSIS Precision  ******");
            for (int i = 0; i < sortResult.size() - 1; i++) {
                int subsequent = i + 1;
                Double number = (double) Math.round((sortResult.get(i).getLambdaScore() - sortResult.get(subsequent).getLambdaScore()) * 10000) / 10000;
                System.out.println(number);
            }
        }
    }
}
