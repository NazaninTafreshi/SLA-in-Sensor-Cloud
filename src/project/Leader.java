package project;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

import static project.Configurer.generateRandomNumber;

/**
 * Created by Ayoubi on 06/05/2016.
 */
public class Leader {
    Manager manager;
    GlobalBroker globalBroker;
    PrintInConsole print;
    private Write write;

    /**
     * @return the manager
     */
    public Manager getManager() {
        return manager;
    }

    // Best result save in mostAppropriateResultSAW and mostAppropriateResultTOPSIS objects in each of brokers
    CandidateResult mostAppropriateResultSAW_L = new CandidateResult();
    CandidateResult mostAppropriateResultTOPSIS_L = new CandidateResult();

    // Best result save in mostAppropriateResultSAW_G and mostAppropriateResultTOPSIS_G objects in globalBroker
    CandidateResult mostAppropriateResultSAW_G = new CandidateResult();
    CandidateResult mostAppropriateResultTOPSIS_G = new CandidateResult();

    // ultimateResult in each of application for different methods
    public ArrayList<CandidateResult> ultimateResultSAW_L = new ArrayList<>();
    public ArrayList<CandidateResult> ultimateResultTOPSIS_L = new ArrayList<>();
    public ArrayList<CandidateResult> ultimateResultSAW_G = new ArrayList<>();
    public ArrayList<CandidateResult> ultimateResultTOPSIS_G = new ArrayList<>();

    ArrayList<CandidateResult> firstSortResultSAW_G;
    ArrayList<CandidateResult> secondSortResultSAW_G;
    ArrayList<CandidateResult> firstSortResultTOPSIS_G;
    ArrayList<CandidateResult> secondSortResultTOPSIS_G;
    ArrayList<CandidateResult> firstSortResult_E_TOPSIS_G;
    ArrayList<CandidateResult> secondSortResult_E_TOPSIS_G;

    //ultimateResult in each of application for local methods (applications just calculate maximum score without run SAW and TOPSIS method)
    public ArrayList<CandidateResult> ultimateResultSAW_L_M = new ArrayList<>();
    public ArrayList<CandidateResult> ultimateResultTOPSIS_L_M = new ArrayList<>();

    // cost for each of cloud provider that connect to broker
    int baseCost = 10;
    //////// int baseCost = Configurer.generateRandomNumber(10,15);

    public Leader() {
        manager = new Manager();
        globalBroker = new GlobalBroker();
        print = new PrintInConsole();
        write = new Write();
        firstSortResultSAW_G = new ArrayList<>();
        secondSortResultSAW_G = new ArrayList<>();
        firstSortResultTOPSIS_G = new ArrayList<>();
        secondSortResultTOPSIS_G = new ArrayList<>();
        firstSortResult_E_TOPSIS_G = new ArrayList<>();
        secondSortResult_E_TOPSIS_G = new ArrayList<>();
        //==============================================================================================================
        // ============================================== set Attributes of ============================================
        // ================= sensorNetworks, cloudProviders, brokers, globalBroker and applications ====================
        // =============================================================================================================

        setAttributesSensorNetworks();
        setAttributesCloudProviders();
        setAttributesBrokers();
        setAttributesApplications();
        setAttributesGlobalBroker();

        run();
    }

    //==================================================================================================================
    //============================================  Brokers Find Results ===============================================
    //=========================================== For Application Request ==============================================
    //===================================================== & ==========================================================
    //==================================== Applications Select the best Results ========================================
    //========================================== that get from Brokers =================================================
    //==================================================================================================================

    /**
     * each of brokers get request of application(s) that connect to it and
     * find the best cloudProvider that can meet application's request with use of
     * Multiple Attribute Decision Making (MADM) method
     * Technique for Order of Preference by Similarity to Ideal Solution (TOPSIS) method
     */

    private void run() {
        //Print attribute of sensorNetwork,cloudProvider, broker, application
        print.printAttributes(manager);
        //Print attribute of globalBroker
        print.printGlobalBroker(globalBroker);

        //==============================================================================================================
        //============================================   method 1_1:(SAW Local) ========================================
        //==============================================================================================================

        System.out.println("****************************************    method 1_1:(SAW Local)    ****************************************");
        /**
         * one of MADM methods is simple additive weighting (SAW)
         */
        for (int i = 0; i < BasicInformation.getBrokerNumber(); i++) {
            //ToDo: candidateResultForAllApp may be not usefully
            ArrayList<CandidateResult> candidateResultForAllApp = new ArrayList<>();
            for (int j = 0; j < manager.getBroker(i).getApplications().size(); j++) {
                /**
                 * each of brokers call calculateCandidateResultSAW and maybe call
                 * findCandidateCloudWithMaxScore methods for each application that connect to it,
                 * Eighth parameter not used
                 */
                ArrayList<CandidateResult> candidateResultForEachAppSAW = calculateCandidateResultSAW(false, i,
                        manager.getBroker(i).getApplications().get(j).getId(),
                        manager.getBroker(i).getApplications().get(j).getCost(),
                        manager.getBroker(i).getApplications().get(j).getResponseTime(),
                        manager.getBroker(i).getApplications().get(j).getTaskType(), true, null);
                /**
                 * if calculateCandidateResultSAW method returns value(s),
                 * this(these) value(s) (include: application id, broker id,
                 * cloud provider id that can meet application requirements,
                 * collectiveCost, collectiveResponseTime, taskType and score )
                 * add to candidateResultForEachAppSAW ArrayList
                 */
                if (!candidateResultForEachAppSAW.isEmpty()) {
                    // ToDo: this for may be not usefully
                    for (int k = 0; k < candidateResultForEachAppSAW.size(); k++) {
                        candidateResultForAllApp.add(candidateResultForEachAppSAW.get(k));
                    }
                    /**
                     * if candidateResultForEachAppSAW.size()==1 then save it to mostAppropriateResultSAW_L
                     * else call findCandidateCloudWithMaxScore method to find alternative with maximum score
                     * and then save it to mostAppropriateResultSAW_L
                     */
                    mostAppropriateResultSAW_L = null;
                    if (candidateResultForEachAppSAW.size() == 1) {
                        mostAppropriateResultSAW_L = candidateResultForEachAppSAW.get(0);
                    } else {
                        mostAppropriateResultSAW_L = findCandidateCloudWithMaxScore(candidateResultForEachAppSAW);
                    }
                    //print most appropriate result in each of brokers
                    print.printSecondCalculateResultSAW(this);

                    for (int k = 0; k < BasicInformation.getAppNumber(); k++) {
                        if (manager.getApplication(k).getId() == mostAppropriateResultSAW_L.getApplicationId()) {
                            /**
                             * each of application receive zero or one result
                             * from each of brokers that connect to it, if result
                             * exist in brokers for this application, this(these) result save in application
                             */
                            manager.getApplication(k).setResultFromEachBrokerSAW(mostAppropriateResultSAW_L);
                        }
                    }
                } else { //else of   if (!candidateResultForEachAppSAW.isEmpty())
                    print.printNoResultForEachBroker(i, j, this);
                }
            }
            // ToDo: this set method may be not usefully
            manager.getBroker(i).setCandidateResultForAllApplication(candidateResultForAllApp);
        }

        //==============================================================================================================
        //===========================================   method 1_2:(SAW Global)   ======================================
        //==============================================================================================================

        System.out.println("*************************************** method 1_2:(SAW Global) ***************************************");
        for (int i = 0; i < globalBroker.getApplications().size(); i++) {
            /**
             * globalBroker call calculateCandidateResultSAW and and maybe call
             * findCandidateCloudWithMaxScore methods for all application that connect to it,
             * Eighth parameter not used
             */
            ArrayList<CandidateResult> candidateResultForEachAppSAW = calculateCandidateResultSAW(true, globalBroker.getId(),
                    globalBroker.getApplications().get(i).getId(),
                    globalBroker.getApplications().get(i).getCost(),
                    globalBroker.getApplications().get(i).getResponseTime(),
                    globalBroker.getApplications().get(i).getTaskType(), true, null);
            /**
             * if calculateCandidateResultSAW method returns value(s),
             * this(these) value(s) (include: application id, globalBroker id,
             * cloudProvider id that can meet application requirements,
             * collectiveCost, collectiveResponseTime, taskType and score )
             * add to candidateResultForEachAppSAW ArrayList
             */
            if (!candidateResultForEachAppSAW.isEmpty()) {
                /**
                 * if candidateResultForEachAppSAW.size()==1 then save it to mostAppropriateResultSAW_G
                 * else call findCandidateCloudWithMaxScore method to find alternative with maximum score
                 * and then save it to mostAppropriateResultSAW_G
                 */
                mostAppropriateResultSAW_G = null;
                if (candidateResultForEachAppSAW.size() == 1) {
                    mostAppropriateResultSAW_G = candidateResultForEachAppSAW.get(0);
                } else {
                    mostAppropriateResultSAW_G = findCandidateCloudWithMaxScore(candidateResultForEachAppSAW);
                    /* if there is more than 2 candidateResult check ranking abnormality and measure the difference of
                    ranking values*/
                    if (candidateResultForEachAppSAW.size() > 2) {

                        //==============================================================================================
                        //=================================  Ranking abnormality  ======================================
                        //========================================== AND  ==============================================
                        //=================================   Precision of SAW  ========================================
                        //==============================================================================================

                        // Sort candidateResult based score (in Descending order)
                        firstSortResultSAW_G = sortCloudBasedScore(candidateResultForEachAppSAW);
                        print.print_Rank(firstSortResultSAW_G, true, 0);
                        // Remove candidateResult with least score
                        firstSortResultSAW_G.remove(firstSortResultSAW_G.size() - 1);

                        secondSortResultSAW_G.clear();
                        secondSortResultSAW_G.addAll(firstSortResultSAW_G.stream().map(candid -> new CandidateResult(
                                candid.getApplicationId(), candid.getBrokerId(), candid.getCloudId(),
                                candid.getTotalCost(), candid.getTotalResponseTime(), candid.getTaskType())).collect(Collectors.toList()));
                        // Run again SAW method
                        int[] no = {0, 0};
                        ArrayList<CandidateResult> secondCandidateResultForEachAppSAW = calculateCandidateResultSAW(false, 0, 0, 0,
                                no, "null", false, secondSortResultSAW_G);
                        // Sort second candidateResult based score (in Descending order)
                        secondSortResultSAW_G = sortCloudBasedScore(secondCandidateResultForEachAppSAW);
                        print.print_Rank(secondSortResultSAW_G, false, 0);
                        System.out.println();
                        for (int j = 0; j < firstSortResultSAW_G.size(); j++) {
                            System.out.println("=*=  FF(SAW)  =*=  " + firstSortResultSAW_G.get(j).getTotalCost() + "   " + firstSortResultSAW_G.get(j).getScore()+
                            "   =*= SS(SAW) =*=  "+ secondSortResultSAW_G.get(j).getTotalCost() + "    " + secondSortResultSAW_G.get(j).getScore());
                        }
                        print.printRankinAbnormalityAndUniformity(firstSortResultSAW_G, secondSortResultSAW_G);
                        write.writingFile_SAW_G_Rank(firstSortResultSAW_G, secondSortResultSAW_G);
                        print.printPrecision(firstSortResultSAW_G, 0);
                        write.writingFile_Precision(firstSortResultSAW_G, 0);
                    }
                }

                //print most appropriate result in globalBroker
                print.gBPrintSecondCalculateResultSAW(this);

                for (int k = 0; k < BasicInformation.getAppNumber(); k++) {
                    if (mostAppropriateResultSAW_G.getApplicationId() == manager.getApplication(k).getId()) {
                        /**
                         * each of application receive zero or one result
                         * from globalBroker, if result
                         * exist in globalBroker for this application,
                         * this(these) result save in application
                         */
                        manager.getApplication(k).setResultFromGlobalBrokerSAW(mostAppropriateResultSAW_G);
                    }
                }
            } else { //else of   if (!candidateResultForEachAppSAW.isEmpty())
                print.printNoResultForGlobalBroker(i, this);
            }
        }

        //==============================================================================================================
        //==========================================   method 2_1:(TOPSIS Local)   =====================================
        //==============================================================================================================

        System.out.println("****************************************  method 2_1:(TOPSIS Local)  ****************************************");
        /**
         * one of MADM methods is Technique for Order Preference  by  Similarity to  Ideal  Solution (TOPSIS)
         */
        for (int i = 0; i < BasicInformation.getBrokerNumber(); i++) {
            for (int j = 0; j < manager.getBroker(i).getApplications().size(); j++) {
                /**
                 * each of brokers call calculateCandidateResultTOPSIS and maybe call
                 * findCandidateCloudWithMaxScore methods for each application that connect to it,
                 * Eighth parameter not used
                 */
                ArrayList<CandidateResult> candidateResultForEachAppTOPSIS = calculateCandidateResultTOPSIS(false, i,
                        manager.getBroker(i).getApplications().get(j).getId(),
                        manager.getBroker(i).getApplications().get(j).getCost(),
                        manager.getBroker(i).getApplications().get(j).getResponseTime(),
                        manager.getBroker(i).getApplications().get(j).getTaskType(), true, null, false);
                /**
                 * if calculateCandidateResultTOPSIS method returns value(s),
                 * this(these) value(s) (include: application id, broker id,
                 * cloudProvider id that can meet application requirements,
                 * collectiveCost, collectiveResponseTime, taskType and score )
                 * add to candidateResultForEachAppTOPSIS ArrayList
                 */
                if (!candidateResultForEachAppTOPSIS.isEmpty()) {
                    /**
                     * if candidateResultForEachAppTOPSIS.size()==1 then save it to mostAppropriateResultTOPSIS_L
                     * else call findCandidateCloudWithMaxScore method to find alternative with maximum score
                     * and then save it to mostAppropriateResultTOPSIS_L
                     */
                    mostAppropriateResultTOPSIS_L = null;
                    if (candidateResultForEachAppTOPSIS.size() == 1) {
                        mostAppropriateResultTOPSIS_L = candidateResultForEachAppTOPSIS.get(0);
                    } else {
                        mostAppropriateResultTOPSIS_L = findCandidateCloudWithMaxScore(candidateResultForEachAppTOPSIS);
                    }

                    //print most appropriate result in each of brokers
                    print.printSecondCalculateResultTOPSIS(this);

                    for (int k = 0; k < BasicInformation.getAppNumber(); k++) {
                        if (manager.getApplication(k).getId() == mostAppropriateResultTOPSIS_L.getApplicationId()) {
                            /**
                             * each of application receive zero or one result
                             * from each of brokers that connect to it, if result
                             * exist in brokers for this application,
                             * this(these) result save in application
                             */
                            manager.getApplication(k).setResultFromEachBrokerTOPSIS(mostAppropriateResultTOPSIS_L);
                        }
                    }
                } else { //else of   if (!candidateResultForEachAppTOPSIS.isEmpty())
                    print.printNoResultForEachBroker(i, j, this);
                }
            }
        }
        //==============================================================================================================
        //====================================   method 2_2:(TOPSIS & ETOPSIS Global)   ================================
        //==============================================================================================================

        System.out.println("************************************  method 2_2:(TOPSIS & ETOPSIS Global)  ************************************");
        for (int j = 0; j < globalBroker.getApplications().size(); j++) {

            /**
             * globalBroker call calculateCandidateResultTOPSIS and maybe call findCandidateCloudWithMaxScore
             * methods for all application that connect to it,
             * Eighth parameter not used
             */
            ArrayList<CandidateResult> candidateResultForEachAppTOPSIS = calculateCandidateResultTOPSIS(true, globalBroker.getId(),
                    globalBroker.getApplications().get(j).getId(),
                    globalBroker.getApplications().get(j).getCost(),
                    globalBroker.getApplications().get(j).getResponseTime(),
                    globalBroker.getApplications().get(j).getTaskType(), true, null, true);

            /**
             * if calculateCandidateResultTOPSIS method returns value(s),
             * this(these) value(s) (include: application id, globalBroker id,
             * cloud provider id that can meet application requirements,
             * collectiveCost, collectiveResponseTime, task type and score )
             * add to candidateResultForEachAppTOPSIS ArrayList
             */
            if (!candidateResultForEachAppTOPSIS.isEmpty()) {

                /**
                 * if candidateResultForEachAppTOPSIS.size()==1 then save it to mostAppropriateResultTOPSIS_G
                 * else call findCandidateCloudWithMaxScore method to find alternative with maximum score
                 * and then save it to mostAppropriateResultTOPSIS_G
                 */
                mostAppropriateResultTOPSIS_G = null;
                if (candidateResultForEachAppTOPSIS.size() == 1) {
                    mostAppropriateResultTOPSIS_G = candidateResultForEachAppTOPSIS.get(0);
                } else {
                    mostAppropriateResultTOPSIS_G = findCandidateCloudWithMaxScore(candidateResultForEachAppTOPSIS);
                    /* if there is more than 2 candidateResult check ranking abnormality and measure the difference of
                    ranking values*/
                    if (candidateResultForEachAppTOPSIS.size() > 2) {
                        //==============================================================================================
                        //=================================  Ranking abnormality  ======================================
                        //========================================== AND  ==============================================
                        //================================   Precision of TOPSIS========================================

                        // Sort candidateResult based score (in Descending order)
                        firstSortResultTOPSIS_G = sortCloudBasedScore(candidateResultForEachAppTOPSIS);
                        print.print_Rank(firstSortResultTOPSIS_G, true, 1);
                        // Remove candidateResult with least score
                        firstSortResultTOPSIS_G.remove(firstSortResultTOPSIS_G.size() - 1);
//                        for (CandidateResult candid : firstSortResultTOPSIS_G) {
//                            secondSortResultTOPSIS_G.add(new CandidateResult(candid.getApplicationId(),candid.getBrokerId(), candid.getCloudId(),
//                                    candid.getTotalCost(), candid.getTotalResponseTime(), candid.getTaskType()));
//                        }
                        secondSortResultTOPSIS_G.clear();
                        secondSortResultTOPSIS_G.addAll(firstSortResultTOPSIS_G.stream().map(candid -> new CandidateResult(
                                candid.getApplicationId(), candid.getBrokerId(), candid.getCloudId(),
                                candid.getTotalCost(), candid.getTotalResponseTime(), candid.getTaskType())).collect(Collectors.toList()));
                        // Run again TOPSIS method
                        int[] no = {0, 0};
                        ArrayList<CandidateResult> secondCandidateResultForEachAppTOPSIS = calculateCandidateResultTOPSIS(
                                false, 0, 0, 0, no, "null", false, secondSortResultTOPSIS_G, true);
                        // Sort second candidateResult based score (in Descending order)
                        secondSortResultTOPSIS_G = sortCloudBasedScore(secondCandidateResultForEachAppTOPSIS);
                        print.print_Rank(secondSortResultTOPSIS_G, false, 1);

                        for (int i = 0; i < firstSortResultTOPSIS_G.size(); i++) {
                            System.out.println("FF==  " + firstSortResultTOPSIS_G.get(i).getTotalCost() + "    " + firstSortResultTOPSIS_G.get(i).getScore()
                                    + " (SS==  " + secondSortResultTOPSIS_G.get(i).getTotalCost() + "    " + secondSortResultTOPSIS_G.get(i).getScore());
                        }
                        print.printRankinAbnormalityAndUniformity(firstSortResultTOPSIS_G, secondSortResultTOPSIS_G);
                        write.writingFile_TOPSIS_G_Rank(firstSortResultTOPSIS_G, secondSortResultTOPSIS_G);
                        print.printPrecision(firstSortResultTOPSIS_G, 1);
                        write.writingFile_Precision(firstSortResultTOPSIS_G, 1);


                        //==============================================================================================
                        //===================================  Ranking abnormality  ====================================
                        //=======================================  of E_TOPSIS  ========================================
                        //==============================================================================================

                        firstSortResult_E_TOPSIS_G.clear();
                        firstSortResult_E_TOPSIS_G.addAll(firstSortResultTOPSIS_G.stream().map(candid -> new CandidateResult(
                                candid.getApplicationId(), candid.getBrokerId(), candid.getCloudId(),
                                candid.getTotalCost(), candid.getTotalResponseTime(), candid.getTaskType(), candid.getLambdaScore())).collect(Collectors.toList()));
                        firstSortResult_E_TOPSIS_G = sortCloudBasedLambdaScore(firstSortResult_E_TOPSIS_G);
                        print.print_Rank(firstSortResult_E_TOPSIS_G, true, 2);

                        secondSortResult_E_TOPSIS_G.clear();
                        secondSortResult_E_TOPSIS_G.addAll(secondSortResultTOPSIS_G.stream().map(candid -> new CandidateResult(
                                candid.getApplicationId(), candid.getBrokerId(), candid.getCloudId(),
                                candid.getTotalCost(), candid.getTotalResponseTime(), candid.getTaskType(), candid.getLambdaScore())).collect(Collectors.toList()));
                        secondSortResult_E_TOPSIS_G = sortCloudBasedLambdaScore(secondSortResult_E_TOPSIS_G);
                        print.print_Rank(secondSortResult_E_TOPSIS_G, false, 2);

                        print.printRankinAbnormalityAndUniformityETOPSIS(firstSortResult_E_TOPSIS_G, secondSortResult_E_TOPSIS_G);
                        write.writingFile_E_TOPSIS_G_Rank(firstSortResult_E_TOPSIS_G, secondSortResult_E_TOPSIS_G);
                        print.printPrecision(firstSortResult_E_TOPSIS_G,2);
                        write.writingFile_Precision(firstSortResult_E_TOPSIS_G, 2);
                    }
                }

                //print most appropriate result in globalBroker
                print.gBPrintSecondCalculateResultTOPSIS(this);

                for (int k = 0; k < BasicInformation.getAppNumber(); k++) {
                    if (mostAppropriateResultTOPSIS_G.getApplicationId() == manager.getApplication(k).getId()) {

                        /**
                         * each of application receive zero or one result
                         * from globalBroker that connect to it, if result
                         * exist in globalBroker for this application,
                         * this(these) result save in application
                         */
                        manager.getApplication(k).setResultFromGlobalBrokerTOPSIS(mostAppropriateResultTOPSIS_G);
                    }
                }
            } else { //else of   if (!candidateResultForEachAppTOPSIS.isEmpty())
                print.printNoResultForGlobalBroker(j, this);
            }
        }

        write.writingFileSAW_L_Max(this);
        write.writingFileSAW_L(this);
        write.writingFileSAW_G(this);
        write.writingFileTOPSIS_L_Max(this);
        write.writingFileTOPSIS_L(this);
        write.writingFileTOPSIS_G(this);
        print.printFirstCalculateResult(this);
    }

    /**
     * setAttributesSensorNetworks method has not input parameter and set attributes of sensorNetworks
     */
    private void setAttributesSensorNetworks() {
        for (int i = 0; i < BasicInformation.getSensorNumber(); i++) {
            // X and Y is Coordinates the starting point
            manager.getSensorNetwork(i).setX((float) (75 * (i + 1)));
            if (i % 2 == 0) {
                manager.getSensorNetwork(i).setY(generateRandomNumber(510, 530));
            } else {
                manager.getSensorNetwork(i).setY(generateRandomNumber(550, 570));
            }
        }
    }

    /**
     * setAttributesCloudProviders method has not input parameter and set attributes of cloudProviders
     */
    private void setAttributesCloudProviders() {
        for (int i = 0; i < BasicInformation.getCloudNumber(); i++) {
            // find sensorNetwork(s) that connect to cloudProvider and get cost, Response time and taskType from it(them)
            ArrayList<SensorNetwork> sensorNetworkCloud = searchForCloudSensor(i);
            // add cost, response time of sensor to cloudProvider's cost and response time
            manager.getCloudProvider(i).setSensorNetworks(sensorNetworkCloud);
            // X and Y is Coordinates the starting point
            manager.getCloudProvider(i).setX((float) (90 * (i + 1)));
            if (i % 2 == 0) {
                manager.getCloudProvider(i).setY(generateRandomNumber(350, 370));
            } else {
                manager.getCloudProvider(i).setY(generateRandomNumber(371, 390));
            }

        }
    }

    /**
     * setAttributesBrokers method has not input parameter and set attributes of brokers
     */
    private void setAttributesBrokers() {
        for (int i = 0; i < BasicInformation.getBrokerNumber(); i++) {

            // find cloudProvider(s) that connect to each of broker and get id, TotalCost and taskType from it(them)
            ArrayList<ArrayList> cloudProviderBroker = searchForBrokerCloud(i);
            manager.getBroker(i).setCloudProviders(cloudProviderBroker);


            // find cloudProvider(s) that connect to each of broker and get responseTime from it(them)
            ArrayList<ArrayList<Integer[]>> cloudBroker = searchForBrokerCloudRt(i);
            manager.getBroker(i).setRtCloudProviders(cloudBroker);
            // find application(s) that connect to each of broker and get cost, responseTime and taskType from it(them)
            ArrayList<Application> applicationBroker = searchForBrokerApp(i);
            manager.getBroker(i).setApplications(applicationBroker);
            // X and Y is Coordinates the starting point
            manager.getBroker(i).setX((float) (105 * (i + 1)));
            if (i % 2 == 0) {
                manager.getBroker(i).setY(generateRandomNumber(180, 200));
            } else {
                manager.getBroker(i).setY(generateRandomNumber(201, 220));
            }
        }
    }

    /**
     * setAttributesApplications method has not input parameter and set attributes of applications
     */
    private void setAttributesApplications() {
        for (int i = 0; i < BasicInformation.getAppNumber(); i++) {
            // X and Y is Coordinates the starting point
            manager.getApplication(i).setX(100 * (i + 1));
            if (i % 2 == 0) {
                manager.getApplication(i).setY(30);
            } else {
                manager.getApplication(i).setY(50);
            }
        }
    }

    /**
     * setAttributesGlobalBroker method has not input parameter and set attributes
     * of globalBroker, globalBroker access to all cloudProviders and all applications
     */
    private void setAttributesGlobalBroker() {
        globalBroker.setId(99999);
        globalBroker.setCost(baseCost);
        //globalBroker connect to all cloudProviders
        ArrayList<ArrayList> allCloudOneBroker = allCloudProvider();
        globalBroker.setCloudProviders(allCloudOneBroker);
        ArrayList<ArrayList<Integer[]>> allCloudOneBrokerRt = allCloudProviderRt();
        globalBroker.setRtCloudProviders(allCloudOneBrokerRt);
        //globalBroker connect to all applications
        ArrayList<Application> allApplicationOneBroker = allApplication();
        globalBroker.setApplications(allApplicationOneBroker);
    }

    /**
     * searchForCloudSensor method get id of cloudProvider and return ArrayList
     * of SensorNetwork that connect to it
     *
     * @param i index of cloudProvider
     * @return ArrayList<SensorNetwork> that connect to cloudProvider
     */
    private ArrayList<SensorNetwork> searchForCloudSensor(int i) {

        ArrayList<SensorNetwork> cloudSensor = new ArrayList<>();
        for (int j = 0; j < BasicInformation.getSensorNumber(); j++) {
            String[] cloudSensorsCons = getManager().getSensorNetwork(j).getCloudCon().split(",");
            for (String cloudSensorsCon : cloudSensorsCons) {
                if (Integer.parseInt(cloudSensorsCon) == i) {
                    cloudSensor.add(getManager().getSensorNetwork(j));
                    break;
                }
            }
        }
        return cloudSensor;
    }

    /**
     * searchForBrokerCloud method get id of broker and return ArrayList of
     * ArrayList of cloudProvider(s)that connect to this broker.
     *
     * @param i index of broker
     * @return ArrayList<ArrayList> that contain id, collective cost and taskType of cloudProvider(s)
     */
    private ArrayList<ArrayList> searchForBrokerCloud(int i) {
        ArrayList<ArrayList> brokerCloud = new ArrayList<>();
        for (int j = 0; j < BasicInformation.getCloudNumber(); j++) {
            String[] cloudBrokersCons = getManager().getCloudProvider(j).getBrokerCon().split(",");
            ArrayList<Integer> cCostPlusBCost = new ArrayList<>();
            int numberOfCloudCon = (manager.getBroker(i).getCloudCon().length() + 1) / 2;
            manager.getBroker(i).setCost(baseCost);
            for (String cloudBrokersCon : cloudBrokersCons) {
                if (Integer.parseInt(cloudBrokersCon) == i) {
                    ArrayList<Integer> cloudId = new ArrayList<>();
                    cloudId.add(getManager().getCloudProvider(j).getId());
                    brokerCloud.add(cloudId);
                    brokerCloud.add(getManager().getCloudProvider(j).getCostForSensorCon());
                    brokerCloud.add(getManager().getCloudProvider(j).getTaskTypeForSensorCon());
                    for (int k = 0; k < getManager().getCloudProvider(j).getCostForSensorCon().size(); k++) {
                        Integer totCost = (Integer) getManager().getCloudProvider(j).getCostForSensorCon().get(k) +
                                getManager().getBroker(i).getCost();
                        cCostPlusBCost.add(totCost);
                    }
                    brokerCloud.add(cCostPlusBCost);
                    break;
                }
            }
        }
        return brokerCloud;
    }


    /**
     * searchForBrokerCloudRt method get id of broker and return
     * ArrayList<ArrayList<Integer[]>> that contain Response time of
     * cloudProvider(s) that connect to this broker
     *
     * @param i index of broker
     * @return ArrayList<ArrayList<Integer[]>> that contains minRt and maxRt of cloudProvider(s) that connect to broker
     */
    private ArrayList<ArrayList<Integer[]>> searchForBrokerCloudRt(int i) {
        ArrayList<ArrayList<Integer[]>> brokerCloud = new ArrayList<>();
        for (int j = 0; j < BasicInformation.getCloudNumber(); j++) {
            String[] cloudBrokersCons = getManager().getCloudProvider(j).getBrokerCon().split(",");
            for (String cloudBrokersCon : cloudBrokersCons) {
                if (!cloudBrokersCon.isEmpty() && Integer.parseInt(cloudBrokersCon) == i) {
                    brokerCloud.add(getManager().getCloudProvider(j).getResponseTimeForSensorCon());
                    break;
                }
            }
        }
        return brokerCloud;
    }


    /**
     * searchForBrokerApp method get id of broker and return ArrayList of
     * Application that connect with this broker
     *
     * @param i index of broker
     * @return ArrayList<Application> that contains application
     */
    private ArrayList<Application> searchForBrokerApp(int i) {
        ArrayList<Application> brokerApp = new ArrayList<>();
        for (int j = 0; j < BasicInformation.getAppNumber(); j++) {
            String[] brokerAppCons = getManager().getApplication(j).getBrokerCon().split(",");
            for (String brokerAppCon : brokerAppCons) {
                if (Integer.parseInt(brokerAppCon) == i) {
                    brokerApp.add(getManager().getApplication(j));
                    break;
                }
            }
        }

        return brokerApp;
    }

    /**
     * allCloudProvider method has not input parameter
     *
     * @return ArrayList<ArrayList> that contains collective information about all of cloudProvider
     */
    private final ArrayList<ArrayList> allCloudProvider() {
        ArrayList<ArrayList> globalBrokerCloud = new ArrayList<>();
        for (int i = 0; i < BasicInformation.getCloudNumber(); i++) {
            ArrayList<Integer> cloudId = new ArrayList<>();
            ArrayList<Integer> cCostPlusGBCost = new ArrayList<>();
            cloudId.add(i);
            globalBrokerCloud.add(cloudId);
            globalBrokerCloud.add(getManager().getCloudProvider(i).getCostForSensorCon());
            globalBrokerCloud.add(getManager().getCloudProvider(i).getTaskTypeForSensorCon());
            for (int k = 0; k < getManager().getCloudProvider(i).getCostForSensorCon().size(); k++) {
                Integer totCost = (Integer) getManager().getCloudProvider(i).getCostForSensorCon().get(k) +
                        globalBroker.getCost();
                cCostPlusGBCost.add(totCost);
            }
            globalBrokerCloud.add(cCostPlusGBCost);
        }
        return globalBrokerCloud;
    }

    /**
     * allCloudProviderRt method has not input parameter
     *
     * @return ArrayList<ArrayList<Integer[]>> that contains collective minRt and collective maxRt in all of cloudProviders
     */
    private final ArrayList<ArrayList<Integer[]>> allCloudProviderRt() {
        ArrayList<ArrayList<Integer[]>> globalBrokerCloud = new ArrayList<>();
        for (int i = 0; i < BasicInformation.getCloudNumber(); i++) {
            globalBrokerCloud.add(getManager().getCloudProvider(i).getResponseTimeForSensorCon());
        }
        return globalBrokerCloud;
    }

    /**
     * allApplication method has not input parameter
     *
     * @return ArrayList of Application that contains return all of Application
     */
    private ArrayList<Application> allApplication() {
        ArrayList<Application> allApp = new ArrayList<>();
        for (int j = 0; j < BasicInformation.getAppNumber(); j++) {
            allApp.add(getManager().getApplication(j));
        }
        return allApp;
    }

    /**
     * getBroker method get id of broker and return broker with this id
     *
     * @param i id of broker
     * @return broker
     */
    public Broker getBroker(int i) {
        return getManager().getBroker(i);
    }

    /**
     * getCloud method get id of cloudProvider and return cloudProvider with this id
     *
     * @param i id of cloudProvider
     * @return cloudProvider
     */
    public CloudProvider getCloud(int i) {
        return getManager().getCloudProvider(i);
    }

    /**
     * getSensor method get id of sensorNetwork and return sensorNetwork with this id
     *
     * @param i id of sensorNetwork
     * @return sensorNetwork
     */
    public SensorNetwork getSensor(int i) {
        return getManager().getSensorNetwork(i);
    }

    /**
     * getSensor method get id of application and return application with this id
     *
     * @param i id of application
     * @return application
     */
    public Application getApp(int i) {
        return getManager().getApplication(i);
    }

    /**
     * calculateCandidateResultSAW method get sla parameters of application,
     * and call potentialCandidate method with these sla parameters.
     * potentialCandidate method with respect to sla parameters
     * that get from calculateCandidateResultSAW,
     * return candidateResult which meet the application conditions.
     * ( potentialCandidate method determine that weather input of it can satisfying application request or can not?)
     * calculateCandidateResultSAW method, after collect candidateResults for each of application, consider score for each of candidateResults.
     * method for consider score is SAW with Linear scale transformation.
     *
     * @param gBOrB              boolean variable for determining caller of this method is broker or globalBroker,
     *                           true means caller is globalBroker and false means caller  is broker
     * @param bId                id of broker that call this method
     * @param appId              id of application that connect to broker
     * @param cost               cost that application up to this amount can be paid
     * @param responseTime       Upper and lower limit of responseTime that application can tolerate it
     * @param taskType           taskType that application request it
     * @param appOrB             boolean variable for determining that caller of this method is broker/globalBroker or application/globalBrokerForRank,
     *                           true means caller is broker/globalBroker and false means caller is application
     * @param appCandidateResult if caller of this method is application send this ArrayList
     *                           (contains results that application get from broker/brokers connect to it/them)
     *                           to find most appropriate cloudProvider between results that get from diverse brokers
     * @return ArrayList of CandidateResult
     */
    ArrayList<CandidateResult> calculateCandidateResultSAW(boolean gBOrB, int bId, int appId, int cost, int[] responseTime, String taskType,
                                                           boolean appOrB, ArrayList<CandidateResult> appCandidateResult) {
        // candidateResults ArrayList keep potential candidates
        ArrayList<CandidateResult> candidateResults = new ArrayList<>();
        //if caller is broker
        if (!gBOrB & appOrB) {
            //for example if  getManager().getBroker(BId).getCloudProviders().size() is 12 then  12/4 = 3 that means broker connect to three cloudProvider
            for (int i = 0; i < getManager().getBroker(bId).getCloudProviders().size() / 4; i++) {
                // number of item(sensorNetwork) in each of cloudProvider that connect to broker
                for (int j = 0; j < getManager().getBroker(bId).getCloudProviders().get(i * 4 + 2).size(); j++) {
                    CandidateResult potentialCandidate;
                    potentialCandidate = potentialCandidate(false, i, j, bId, appId, cost, responseTime, taskType);
                    if (potentialCandidate != null) {
                        candidateResults.add(potentialCandidate);
                    }
                }
            }
            //if caller is globalBroker
        } else if (gBOrB & appOrB) {
            for (int i = 0; i < globalBroker.getCloudProviders().size() / 4; i++) {
                for (int j = 0; j < globalBroker.getCloudProviders().get(i * 4 + 2).size(); j++) {
                    CandidateResult potentialCandidate;
                    potentialCandidate = potentialCandidate(true, i, j, bId, appId, cost, responseTime, taskType);
                    if (potentialCandidate != null) {
                        candidateResults.add(potentialCandidate);
                    }
                }
            }
            //if caller is application
        } else if (!appOrB) {
            System.out.println("App App App App");
            for (int i = 0; i < appCandidateResult.size(); i++) {
                candidateResults.add(appCandidateResult.get(i));
            }
        }
        if (!candidateResults.isEmpty()) {
            //ToDo: this sout and for loop must be delete
            System.out.println("candidate result method SAW:");
            for (int k = 0; k < candidateResults.size(); k++) {
                System.out.println(candidateResults.get(k).getTotalCost() + "  " +
                        Arrays.toString(candidateResults.get(k).getTotalResponseTime()) + " BId  " + candidateResults.get(k).getBrokerId()
                        + " AppId  " + candidateResults.get(k).getApplicationId() + " cloudId " + candidateResults.get(k).getCloudId());

            }
            int[] min;
            // call findMinAlt method finding the lowest value of cost, minRt and maxRt that use in Linear scale transformation
            min = findMinALt(candidateResults);
            System.out.println("minimum:             " + min[0] + " " + min[1] + " " + min[2]);
            // if broker find an alternative thar all of attributes of it, is equal to min use of SAW method is not necessary
            boolean AltWithAllAttMin = false;
            for (int i = 0; i < candidateResults.size(); i++) {
                if (min[0] == candidateResults.get(i).getTotalCost() & min[1] == candidateResults.get(i).getTotalResponseTime()[0] &
                        min[2] == candidateResults.get(i).getTotalResponseTime()[1]) {
                    candidateResults.get(i).setScore(1);
                    AltWithAllAttMin = true;
                } else {
                    candidateResults.get(i).setScore(0);
                }
            }
            if (!AltWithAllAttMin) {
                int index = candidateResults.get(0).getApplicationId();
                for (int i = 0; i < candidateResults.size(); i++) {
                    candidateResults.get(i).setScore(score_SAW_LinearScaleTransformation(min[0], min[1], min[2],
                            candidateResults.get(i).getTotalCost(),
                            candidateResults.get(i).getTotalResponseTime()[0],
                            candidateResults.get(i).getTotalResponseTime()[1],
                            getManager().getApplication(index).getWeightCost(),
                            getManager().getApplication(index).getWeightMinRt(),
                            getManager().getApplication(index).getWeightMaxRt()
                    ));
                }
            }
        }

        //TODO: delete this for loop
        for (int k = 0; k < candidateResults.size(); k++) {
            System.out.println("score:         " + candidateResults.get(k).getScore());

        }
        return candidateResults;
    }

    /**
     * potentialCandidate method with respect to sla parameter that
     * get from calculateCandidateResultSAW method, return candidateResult which meet the application conditions.
     * (indeed determine that input of it can meet the application conditions or can not).
     * CandidateResult should have these conditions:
     * taskType of it must equals to taskType in application's request,
     * cost of it must smaller than or equal to cost in application's request and
     * responseTime of it must in the range of responseTime in application's request.
     *
     * @param gBOrB         boolean variable for determining caller of this method is broker or globalBroker,
     *                      true means caller is globalBroker and false means caller is broker
     * @param i             index of first for loop in callers
     * @param j             index of second for loop in callers
     * @param brokerId      id of broker that call this method
     * @param applicationId id of application that connect to broker
     * @param cost          cost that application up to this amount can be paid
     * @param responseTime  Upper and lower limit of responseTime that application can tolerate it
     * @param taskType      taskType that application request it
     * @return CandidateResult result that have above conditions
     */
    private CandidateResult potentialCandidate(boolean gBOrB, int i, int j, int brokerId, int applicationId, int cost, int[] responseTime, String taskType) {
        CandidateResult candidate = new CandidateResult();
        if (!gBOrB) {
            //whether taskType of application equal to taskType of cloudProviders that exist in broker?
            if (getManager().getBroker(brokerId).getCloudProviders().get(i * 4 + 2).get(j).equals(taskType)
                    && //whether cost of application larger than cost of cloudProviders that exist in broker?
                    (cost >= (Integer) getManager().getBroker(brokerId).getCloudProviders().get(i * 4 + 3).get(j))
                    && //check responseTime
                    ((responseTime[0] >= getManager().getBroker(brokerId).getRtCloudProviders().get(i).get(j)[0]
                            && responseTime[1] >= getManager().getBroker(brokerId).getRtCloudProviders().get(i).get(j)[1])
                            || (responseTime[0] < getManager().getBroker(brokerId).getRtCloudProviders().get(i).get(j)[0]
                            && responseTime[1] >= getManager().getBroker(brokerId).getRtCloudProviders().get(i).get(j)[1]))) {
                //if  potentialCandidates is exist, save it's property in candidate and then return it to caller
                candidate.setApplicationId(applicationId);
                candidate.setBrokerId(getManager().getBroker(brokerId).getId());
                candidate.setCloudId((Integer) getManager().getBroker(brokerId).getCloudProviders().get(i * 4).get(0));
                candidate.setTaskType((String) getManager().getBroker(brokerId).getCloudProviders().get(i * 4 + 2).get(j));
                candidate.setTotalCost((Integer) getManager().getBroker(brokerId).getCloudProviders().get(i * 4 + 3).get(j));
                candidate.setTotalResponseTime(getManager().getBroker(brokerId).getRtCloudProviders().get(i).get(j));
            } else {
                return null;
            }
        } else {
            if ( //whether taskType of application equals to taskType of cloudProviders that exist in globalBroker?
                    globalBroker.getCloudProviders().get(i * 4 + 2).get(j).equals(taskType)
                            && //whether cost of application larger than cost of cloudProviders that exist in globalBroker?
                            (cost >= (Integer) globalBroker.getCloudProviders().get(i * 4 + 3).get(j))
                            && //check responseTime
                            ((responseTime[0] >= globalBroker.getRtCloudProviders().get(i).get(j)[0]
                                    && responseTime[1] >= globalBroker.getRtCloudProviders().get(i).get(j)[1])
                                    || (responseTime[0] < globalBroker.getRtCloudProviders().get(i).get(j)[0]
                                    && responseTime[1] >= globalBroker.getRtCloudProviders().get(i).get(j)[1]))) {
                //if  potentialCandidates is exist, save it's property in candidate and then return it to caller
                candidate.setApplicationId(applicationId);
                candidate.setBrokerId(globalBroker.getId());
                candidate.setCloudId(
                        (Integer) globalBroker.getCloudProviders().get(i * 4).get(0));
                candidate.setTaskType(
                        (String) globalBroker.getCloudProviders().get(i * 4 + 2).get(j));
                candidate.setTotalCost(
                        (Integer) globalBroker.getCloudProviders().get(i * 4 + 3).get(j));
                candidate.setTotalResponseTime(globalBroker.getRtCloudProviders().get(i).get(j));
            } else {
                return null;
            }
        }
        return candidate;
    }


    /**
     * findCandidateCloudWithMaxScore method get ArrayList of candidateResults( from brokers or applications )
     * and find alternative(s) with maximum score between them, then return it
     *
     * @param candidateResult ArrayList of candidateResults for each applications
     * @return CandidateResult one of the alternative with maximum score
     */
    CandidateResult findCandidateCloudWithMaxScore(ArrayList<CandidateResult> candidateResult) {
        return (findCloudWithMaxScore(candidateResult));
    }

    /**
     * findCloudWithMaxScore method get ArrayList of candidateResult
     * and find alternative with maximum score between them, then return on of
     * the candidateResult with maximum score
     *
     * @param results ArrayList of CandidateResult
     * @return CandidateResult one of the alternative with maximum score
     */
    private CandidateResult findCloudWithMaxScore(ArrayList<CandidateResult> results) {
        Double score[] = new Double[results.size()];
        //fill score Array with score of each alternative for finding maximum between them
        for (int k = 0; k < results.size(); k++) {
            score[k] = results.get(k).getScore();
        }
        // find maximum between score
        Double maximumScore = Double.MIN_VALUE;
        for (int k = 0; k < score.length; k++) {
            if (score[k] > maximumScore) {
                maximumScore = score[k];
            }
        }
        ArrayList<CandidateResult> resultsWithMaxScore = new ArrayList<>();
        // find alternative that score of them is equal to maximumScore
        for (int k = 0; k < results.size(); k++) {
            if (results.get(k).getScore() == maximumScore) {
                resultsWithMaxScore.add(results.get(k));
            }
        }

        /**
         * if finding more than one alternative that score of them are
         * equal to maximumScore, randomly choose one of them else
         * if just finding one alternative that score of it are
         * equal to maximumScore return it
         */
        CandidateResult resultWithMaxScore;
        if (resultsWithMaxScore.size() > 1) {
            int randomNumber = generateRandomNumber(resultsWithMaxScore.size() - 1);
            resultWithMaxScore = resultsWithMaxScore.get(randomNumber);
        } else {
            resultWithMaxScore = resultsWithMaxScore.get(0);
        }
        return resultWithMaxScore;
    }

    /**
     * sortCloudBasedScore method receive ArrayList of CandidateResult and sort it by score
     * global broker call this method
     *
     * @param results ArrayList<CandidateResult>
     * @return sorted ArrayList<CandidateResult> by score
     */
    private ArrayList<CandidateResult> sortCloudBasedScore(ArrayList<CandidateResult> results) {
        Collections.sort(results);
        return results;
    }

    /**
     * sortCloudBasedLambdaScore method receive ArrayList of CandidateResult and sort it by LambdaScore
     * global broker call this method
     *
     * @param results ArrayList<CandidateResult>
     * @return sorted ArrayList<CandidateResult> by LambdaScore
     */
    private ArrayList<CandidateResult> sortCloudBasedLambdaScore(ArrayList<CandidateResult> results) {
        Collections.sort(results);
        return results;
    }

    /**
     * weightedDecisionMatrix method Construct  the  weighted  normalized:
     * first each of Sla parameters (attribute) normalize with vector normalization method then these multiply with weight
     *
     * @param sumTotalCost  sum of squares values of the first column (cost) in decision matrix
     * @param sumTotalMinRt sum of squares values of the second column (minRt) in decision matrix
     * @param sumTotalMaxRt sum of squares values of the third column (maxRt) in decision matrix
     * @param totalCost     cost that caller send it
     * @param totalMinRt    minRt that caller send it
     * @param totalMaxRt    maxRt that caller send it
     * @param weightCost    weight of cost tha application announce it
     * @param weightMinRt   weight of cost tha application announce it
     * @param weightMaxRT   weight of cost tha application announce it
     * @return double[] one row of weighted decision matrix
     */
    private double[] weightedDecisionMatrix(double sumTotalCost, double sumTotalMinRt, double sumTotalMaxRt,
                                            double totalCost, double totalMinRt, double totalMaxRt,
                                            double weightCost, double weightMinRt, double weightMaxRT) {

        double VectorNormalization[] = new double[3];
        // calculate vector Normalization total cost and then multiply with weightCost
        VectorNormalization[0] = (totalCost / Math.sqrt(sumTotalCost)) * weightCost;
        System.out.println("********** cost ************          " + VectorNormalization[0]);
        //calculate vector Normalization total minRt and then multiply with weightMinRt
        VectorNormalization[1] = (totalMinRt / Math.sqrt(sumTotalMinRt)) * weightMinRt;
        System.out.println("********* MinRt ************          " + VectorNormalization[1]);
        //calculate vector Normalization total maxRt and then multiply with weightMaxRT
        VectorNormalization[2] = (totalMaxRt / Math.sqrt(sumTotalMaxRt)) * weightMaxRT;
        System.out.println("********** MaxRt ***********          " + VectorNormalization[2]);

        return VectorNormalization;
    }


    /**
     * score_SAW_LinearScaleTransformation method get 9 number and then calculate score for each of alternatives,
     * with respect to minTotalCost, minTotalMinRt and minTotalMaxRt Linear scale transformation and
     * score in SAW method are calculated
     *
     * @param minTotalCost  minimum of totalCost
     * @param minTotalMinRt minimum of totalMinRt
     * @param minTotalMaxRt minimum of totalMaxRt
     * @param totalCost     cost of one alternative
     * @param totalMinRt    minRt of one alternative
     * @param totalMaxRt    maxRt of one alternative
     * @param weightCost    weightCost that one Application announce
     * @param weightMinRt   weightMinRt that Application announce
     * @param weightMaxRT   weightMaxRT that Application announce
     * @return double score for one alternative
     */
    private double score_SAW_LinearScaleTransformation(double minTotalCost, double minTotalMinRt, double minTotalMaxRt,
                                                       int totalCost, int totalMinRt, int totalMaxRt, double weightCost,
                                                       double weightMinRt, double weightMaxRT) {
        // calculate dimensionless totalCost
        double dimLessTotCost = minTotalCost / totalCost;
        // calculate dimensionless totalMinRt
        double dimLessTotMinRt = minTotalMinRt / totalMinRt;
        // calculate dimensionless totalMaxRt
        double dimLessTotMaxRt = minTotalMaxRt / totalMaxRt;
        /**
         * calculate SAW (simple additive weighting) formula.
         * in this formula weights that application declare for cost, minRt and maxRt multiply by
         * dimLessTotCost, dimLessTotMinRt and dimLessTotMaxRt respectively and then,
         * sum of this three values is calculate
         */
        return (double) Math.round(((weightCost * dimLessTotCost) + (weightMinRt * dimLessTotMinRt) + (weightMaxRT * dimLessTotMaxRt)) * 10000) / 10000;
    }

    /**
     * calculateCandidateResultTOPSIS method get sla parameters of application,
     * and call potentialCandidate method with these sla parameters.
     * potentialCandidate method with respect to sla parameters
     * that get from calculateCandidateResultTOPSIS,
     * ( potentialCandidate method determine that weather input of it can satisfying application request or can not?)
     * return candidateResult which meet the application conditions.
     * calculateCandidateResultTOPSIS method, after collect candidateResults for each of application, consider score for each of candidateResults.
     * with use of scoreWithVectorNormalization method.
     * method for consider score is TOPSIS with Vector normalization.
     *
     * @param gBOrB              boolean variable for determining caller of this method is broker or globalBroker,
     *                           true means caller is globalBroker and false means caller is broker
     * @param bId                id of broker that call this method
     * @param appId              id of application that connect to broker
     * @param cost               cost that application up to this amount can be paid
     * @param responseTime       Upper and lower limit response time that application can tolerate it
     * @param taskType           task type that application request it
     * @param appOrB             boolean variable for determining caller of this method is broker/globalBroker or application,
     *                           true means caller is broker/globalBroker and false means caller is application
     * @param callFor_E_TOPSIS   boolean variable for determining caller of this method is globalBroker or broker/application,
     *                           true means caller is globalBroker and use this method for calculate E-TOPSIS with lambda1 and lambda2
     *                           false means caller is application or broker
     * @param appCandidateResult if caller is application send it to find mostAppropriate between result that get from diverse brokers
     * @return ArrayList of CandidateResult
     */
    ArrayList<CandidateResult> calculateCandidateResultTOPSIS(boolean gBOrB, int bId, int appId, int cost, int[] responseTime, String taskType,
                                                              boolean appOrB, ArrayList<CandidateResult> appCandidateResult, boolean callFor_E_TOPSIS) {
        // candidateResults ArrayList keep potential candidates
        ArrayList<CandidateResult> candidateResults = new ArrayList<>();
        //if caller is broker
        if (!gBOrB & appOrB) {
            //for example if manager.broker.get(brokerId).getCloudProviders().size() is 12 then  12/4 = 3 that means broker connect to four cloud
            for (int i = 0; i < getManager().getBroker(bId).getCloudProviders().size() / 4; i++) {
                // number of item (sensorNetwork)in each of cloudProvider that connect to broker
                for (int j = 0; j < getManager().getBroker(bId).getCloudProviders().get(i * 4 + 2).size(); j++) {
                    CandidateResult potentialCandidate;
                    potentialCandidate = potentialCandidate(false, i, j, bId, appId, cost, responseTime, taskType);
                    if (potentialCandidate != null) {
                        candidateResults.add(potentialCandidate);
                    }
                }
            }
            //if caller is globalBroker
        } else if (gBOrB & appOrB) {
            for (int i = 0; i < globalBroker.getCloudProviders().size() / 4; i++) {
                for (int j = 0; j < globalBroker.getCloudProviders().get(i * 4 + 2).size(); j++) {
                    CandidateResult potentialCandidate;
                    potentialCandidate = potentialCandidate(true, i, j, bId, appId, cost, responseTime, taskType);
                    if (potentialCandidate != null) {
                        candidateResults.add(potentialCandidate);
                    }
                }
            }
            //if caller is application
        } else if (!appOrB) {
            System.out.println("App TOPSIS");
            for (int i = 0; i < appCandidateResult.size(); i++) {
                candidateResults.add(appCandidateResult.get(i));
            }
        }
        if (!candidateResults.isEmpty()) {
            //ToDo: this for loop must be delete
            for (int k = 0; k < candidateResults.size(); k++) {
                System.out.println("candidate result method TOPSIS:       " + candidateResults.get(k).getTotalCost() + "  " +
                        Arrays.toString(candidateResults.get(k).getTotalResponseTime()) + " BId  " + candidateResults.get(k).getBrokerId()
                        + " AppId  " + candidateResults.get(k).getApplicationId() + " cloudId " + candidateResults.get(k).getCloudId());

            }
            if (candidateResults.size() == 1) {
                candidateResults.get(0).setScore(1);
            } else {
                int[] min;
                // call findMinAlt method for finding min of cost, minRt and maxRt
                min = findMinALt(candidateResults);
                // if broker find an alternative thar all of attributes of it, is equal to min use of TOPSIS Technique is not necessary
                boolean AltWithAllAttMin = false;
                    for (int i = 0; i < candidateResults.size(); i++) {
                        if (min[0] == candidateResults.get(i).getTotalCost() & min[1] == candidateResults.get(i).getTotalResponseTime()[0] &
                                min[2] == candidateResults.get(i).getTotalResponseTime()[1]) {
                            candidateResults.get(i).setScore(1);
                            if(candidateResults.get(i).getTaskType().equals("Area monitoring")){
                                candidateResults.get(i).setLambdaScore(0.8);
                            }
                            if(candidateResults.get(i).getTaskType().equals("Health care monitoring")){
                                candidateResults.get(i).setLambdaScore(0.889);
                            }
                            AltWithAllAttMin = true;
                        } else {
                            candidateResults.get(i).setScore(0);
                            candidateResults.get(i).setLambdaScore(0);
                        }
                    }
                if (!AltWithAllAttMin) {
                    // call calcSumOfAtt method for calculate sum of squares of cost, minRt and maxRt of candidateResults that use in vector normalization
                    int[] sum = calcSumOfAtt(candidateResults);
                    int index = candidateResults.get(0).getApplicationId();
                    // step 1 & 2 of of TOPSIS algorithm: Construct the normalized decision matrix, Construct the weighted normalized decision matrix
                    for (int i = 0; i < candidateResults.size(); i++) {
                        candidateResults.get(i).setWeightedNormal(weightedDecisionMatrix(sum[0], sum[1], sum[2],
                                candidateResults.get(i).getTotalCost(),
                                candidateResults.get(i).getTotalResponseTime()[0],
                                candidateResults.get(i).getTotalResponseTime()[1],
                                getManager().getApplication(index).getWeightCost(),
                                getManager().getApplication(index).getWeightMinRt(),
                                getManager().getApplication(index).getWeightMaxRt()
                        ));
                    }
                    // step 3 of TOPSIS algorithm: Determine ideal and negative-ideal solutions
                    double[] ideal = determineIdeal(candidateResults);
                    double[] negativeIdeal = determineNegativeIdeal(candidateResults);
                    // step 4  & 5 of TOPSIS algorithm:
                    double sepMeasureFromIdeal, sepMeasureFromNegativeIdeal;
                    for (int i = 0; i < candidateResults.size(); i++) {
                        //step 4: Calculate the separation measure
                        sepMeasureFromIdeal = separationMeasure(true, candidateResults.get(i).getWeightedNormal()[0],
                                candidateResults.get(i).getWeightedNormal()[1], candidateResults.get(i).getWeightedNormal()[2],
                                ideal);
                        sepMeasureFromNegativeIdeal = separationMeasure(false, candidateResults.get(i).getWeightedNormal()[0],
                                candidateResults.get(i).getWeightedNormal()[1], candidateResults.get(i).getWeightedNormal()[2],
                                negativeIdeal);
                        //step 5: Calculate the relative closeness to the ideal solution
                        candidateResults.get(i).setScore((double)
                                Math.round((sepMeasureFromNegativeIdeal / (sepMeasureFromIdeal + sepMeasureFromNegativeIdeal)) * 10000) / 10000);
                        candidateResults.get(i).setLambdaScore(-1);
                        if (callFor_E_TOPSIS) {
                            if (candidateResults.get(i).getTaskType().equals("Area monitoring")) {
                                candidateResults.get(i).setLambdaScore((double)
                                        Math.round((1-((((0.800 * sepMeasureFromIdeal) + (0.200 * sepMeasureFromNegativeIdeal)) /
                                                (sepMeasureFromIdeal + sepMeasureFromNegativeIdeal)))) * 10000) / 10000);
                            } else if (candidateResults.get(i).getTaskType().equals("Health care monitoring")) {
                                candidateResults.get(i).setLambdaScore((double)
                                        Math.round((1-((((0.889 * sepMeasureFromIdeal) + (0.111 * sepMeasureFromNegativeIdeal)) /
                                                (sepMeasureFromIdeal + sepMeasureFromNegativeIdeal)))) * 10000) / 10000);
                            }
                        }
                    }//end of for (int i = 0; i < candidateResults.size(); i++)
                } // end else of !AlternativeWithAllAttMin
            } //end else of candidateResults.size == 1
        } // end else of candidateResults.isEmpty()

        for (int k = 0; k < candidateResults.size(); k++) {
            System.out.println("score TOPSIS:         " + candidateResults.get(k).getTotalCost() + "  " +
                    " " + candidateResults.get(k).getScore());
        }
        return candidateResults;
    }

    /**
     * separationMeasure method get alternative (contain totalCost, totalMinRt and totalMaxRt),
     * then Calculate  the  separation measure and return it
     *
     * @param idealOrN_Ideal       boolean variable, true means caller want to calculate separation measure from ideal and
     *                             false means caller want to calculate separation measure from negative-ideal
     * @param wN_Cost              first attr of alternative
     * @param wN_MinRt             second attr of alternative
     * @param wN_MaxRt             third attr of alternative
     * @param idealOrNegativeIdeal if idealOrN_Ideal is true  caller send Ideal array else caller send
     *                             NegativeIdeal array for calculating separation measure
     * @return double separation measure from Ideal or from negativeIdeal
     */
    private double separationMeasure(boolean idealOrN_Ideal, double wN_Cost, double wN_MinRt, double wN_MaxRt, double[] idealOrNegativeIdeal) {
        double separation;
        /**
         * The separation between each alternative can be measured by the n-dimensional Euclidean distance
         * */
        if (idealOrN_Ideal) {
            double costSM = Math.pow(wN_Cost - idealOrNegativeIdeal[0], 2);
            double minRtSM = Math.pow(wN_MinRt - idealOrNegativeIdeal[1], 2);
            double maxRtSM = Math.pow(wN_MaxRt - idealOrNegativeIdeal[2], 2);
            separation = Math.sqrt(costSM + minRtSM + maxRtSM);
            System.out.println("///  idealSeparation /// " + separation);

        } else {
            double costSM = Math.pow(wN_Cost - idealOrNegativeIdeal[0], 2);
            double minRtSM = Math.pow(wN_MinRt - idealOrNegativeIdeal[1], 2);
            double maxRtSM = Math.pow(wN_MaxRt - idealOrNegativeIdeal[2], 2);
            separation = Math.sqrt(costSM + minRtSM + maxRtSM);
            System.out.println("///  negativeSeparation /// " + separation);
            System.out.println();
        }

        return separation;
    }

    /**
     * determineIdeal method get candidateResults ArrayList
     * from each brokers for each application that connect to them,
     * then find the lowest value of normalized and weighted cost, minRt and maxRt between them
     * indeed find ideal solution
     *
     * @param candidateResults broker send candidateResults to this method for Determine the idealSolution
     * @return double[] idealSolution array that contain the lowest value of normalize and weighted cost, minRt and maxRt respectively
     */
    private double[] determineIdeal(ArrayList<CandidateResult> candidateResults) {
        double idealCost = Double.MAX_VALUE, ideaMinRt = Double.MAX_VALUE, idealMaxRt = Double.MAX_VALUE;
        double[] idealSolution = new double[3];
        for (int i = 0; i < candidateResults.size(); i++) {
            if (candidateResults.get(i).getWeightedNormal()[0] < idealCost) {
                idealCost = candidateResults.get(i).getWeightedNormal()[0];
            }
            if (candidateResults.get(i).getWeightedNormal()[1] < ideaMinRt) {
                ideaMinRt = candidateResults.get(i).getWeightedNormal()[1];
            }
            if (candidateResults.get(i).getWeightedNormal()[2] < idealMaxRt) {
                idealMaxRt = candidateResults.get(i).getWeightedNormal()[2];
            }
        }
        idealSolution[0] = idealCost;
        idealSolution[1] = ideaMinRt;
        idealSolution[2] = idealMaxRt;
        System.out.println("+ + Ideal + +    " + Arrays.toString(idealSolution));
        return idealSolution;
    }

    /**
     * determineNegativeIdeal method get candidateResults ArrayList
     * from each brokers for each application that connect to them,
     * then find the biggest value of normalize and weighted cost, minRt and maxRt between them
     * indeed find negativeIdeal solution
     *
     * @param candidateResults broker send candidateResults to this method for Determine the negativeIdealSolution solution
     * @return double[] negativeIdealSolution array that contain biggest value of normalize and weighted cost, minRt and maxRt respectively
     */

    private double[] determineNegativeIdeal(ArrayList<CandidateResult> candidateResults) {
        double negativeIdealCost = Double.MIN_VALUE, negativeIdeaMinRt = Double.MIN_VALUE, negativeIdealMaxRt = Double.MIN_VALUE;
        double[] negativeIdealSolution = new double[3];
        for (int i = 0; i < candidateResults.size(); i++) {
            if (candidateResults.get(i).getWeightedNormal()[0] > negativeIdealCost) {
                negativeIdealCost = candidateResults.get(i).getWeightedNormal()[0];
            }
            if (candidateResults.get(i).getWeightedNormal()[1] > negativeIdeaMinRt) {
                negativeIdeaMinRt = candidateResults.get(i).getWeightedNormal()[1];
            }
            if (candidateResults.get(i).getWeightedNormal()[2] > negativeIdealMaxRt) {
                negativeIdealMaxRt = candidateResults.get(i).getWeightedNormal()[2];
            }
        }
        negativeIdealSolution[0] = negativeIdealCost;
        negativeIdealSolution[1] = negativeIdeaMinRt;
        negativeIdealSolution[2] = negativeIdealMaxRt;
        System.out.println("- - nIdeal - -     " + Arrays.toString(negativeIdealSolution));
        return negativeIdealSolution;
    }

    /**
     * findMinALt method get candidateResults ArrayList
     * from each brokers for each application that connect to them,
     * then find the lowest value of cost, minRt and maxRt (the lowest value of each attributes) between them
     *
     * @param candidateResults caller(broker) send candidateResults to this method for find the lowest value of sla parameter
     * @return int[] array that contain the lowest value of cost, minRt and maxRt respectively
     */
    private int[] findMinALt(ArrayList<CandidateResult> candidateResults) {
        int lowCostAlt = Integer.MAX_VALUE, lowMinRtAlt = Integer.MAX_VALUE, lowMaxRtAlt = Integer.MAX_VALUE;
        int[] min = new int[3];
        for (int i = 0; i < candidateResults.size(); i++) {
            if (candidateResults.get(i).getTotalCost() < lowCostAlt) {
                lowCostAlt = candidateResults.get(i).getTotalCost();
            }
            if (candidateResults.get(i).getTotalResponseTime()[0] < lowMinRtAlt) {
                lowMinRtAlt = candidateResults.get(i).getTotalResponseTime()[0];
            }
            if (candidateResults.get(i).getTotalResponseTime()[1] < lowMaxRtAlt) {
                lowMaxRtAlt = candidateResults.get(i).getTotalResponseTime()[1];
            }
        }
        min[0] = lowCostAlt;
        min[1] = lowMinRtAlt;
        min[2] = lowMaxRtAlt;
        return min;
    }

    /**
     * calcSumOfAtt method get candidateResults ArrayList
     * from each brokers for each application that connect to them,
     * then calculate sum of squares of cost, minRt and maxRt
     *
     * @param candidateResults ArrayList of candidateResults in each of brokers for each application that connect to it
     * @return int[] array that contain sum of squares of cost, minRt and maxRt respectively
     */
    private int[] calcSumOfAtt(ArrayList<CandidateResult> candidateResults) {
        int sumCost = 0, sumMinRt = 0, sumMaxRt = 0;
        int[] sum = new int[3];
        for (int i = 0; i < candidateResults.size(); i++) {
            sumCost += Math.pow(candidateResults.get(i).getTotalCost(), 2);
            sumMinRt += Math.pow(candidateResults.get(i).getTotalResponseTime()[0], 2);
            sumMaxRt += Math.pow(candidateResults.get(i).getTotalResponseTime()[1], 2);
        }
        sum[0] = sumCost;
        sum[1] = sumMinRt;
        sum[2] = sumMaxRt;
        return sum;
    }


}