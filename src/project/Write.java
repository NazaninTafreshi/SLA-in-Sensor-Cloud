package project;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Ayoubi on 21/08/2016.
 */
public class Write {

    //==================================================================================================================
    //======================================       method 1_1: SAW_Local_Max       =====================================
    //==================================================================================================================

    /**
     * writingFileSAW_L_Max method write the final answer in file for each of applications
     * in this method applications receive result/s from brokers (base on SAW method) that connect to it/them and select
     * alternative with maximum score
     *
     * @param leader object of Leader class
     */
    void writingFileSAW_L_Max(Leader leader) {
        createDirectory("Dir-SAW-L-Max");
        System.out.println("===============       method 1_1: SAW_Local_Max       ===============");
        // set path for saving file
        Path currentRelativePath = Paths.get("");
        String currentPath = currentRelativePath.toAbsolutePath().toString();
        String newPath = currentPath + "\\Dir-SAW-L-Max";
        File fileSAW_L_M = new File(newPath, System.currentTimeMillis() + "SAW-L-M.csv");
        try (BufferedWriter br = new BufferedWriter(new FileWriter(fileSAW_L_M))) {
            for (int i = 0; i < BasicInformation.getAppNumber(); i++) {
                if (!leader.manager.getApplication(i).getReceiveResultFromBrokersSAW().isEmpty()) {
                    String oneOrMultiple;
                    if (leader.manager.getApplication(i).getReceiveResultFromBrokersSAW().size() == 1) {
                        leader.ultimateResultSAW_L_M.add(i, leader.manager.getApplication(i).getReceiveResultFromBrokersSAW().get(0));
                        oneOrMultiple = "O";
                    } else {
                        leader.ultimateResultSAW_L_M.add(i, leader.findCandidateCloudWithMaxScore(leader.manager.getApplication(i).getReceiveResultFromBrokersSAW()));
                        oneOrMultiple = "M";
                    }
                    leader.print.printUltimateResultSAW_M(i, leader);
                    writeInFile(br, leader.ultimateResultSAW_L_M.get(i), oneOrMultiple);
                } else {
                    leader.ultimateResultSAW_L_M.add(i, null);
                    br.write(leader.manager.getApplication(i).getId() + "," + 0 + "," + 0 + "," + 0 + "," + 0 + "," + 0 + "," + 0 + "," + "E");
                    br.newLine();
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Leader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //==================================================================================================================
    //========================================       method 1_2: SAW_Local       =======================================
    //==================================================================================================================

    /**
     * writingFileSAW_L method write The final answer in file for each of applications
     * in this method applications receive result/s (base on SAW method) from brokers that connect to it/them and
     * run SAW method on receive result again
     *
     * @param leader object of Leader class
     */
    void writingFileSAW_L(Leader leader) {
        createDirectory("Dir-SAW-L");
        System.out.println("===============       method 1_2: SAW_Local       ===============");
        // set path for saving file
        Path currentRelativePath = Paths.get("");
        String currentPath = currentRelativePath.toAbsolutePath().toString();
        String newPath = currentPath + "\\Dir-SAW-L";
        File fileSAW_L = new File(newPath, System.currentTimeMillis() + "SAW-L.csv");
        try (BufferedWriter br = new BufferedWriter(new FileWriter(fileSAW_L))) {
            for (int i = 0; i < BasicInformation.getAppNumber(); i++) {
                if (!leader.manager.getApplication(i).getReceiveResultFromBrokersSAW().isEmpty()) {
                    String oneOrMultiple;
                    if (leader.manager.getApplication(i).getReceiveResultFromBrokersSAW().size() == 1) {
                        leader.ultimateResultSAW_L.add(i, leader.manager.getApplication(i).getReceiveResultFromBrokersSAW().get(0));
                        oneOrMultiple = "O";
                    } else {
                        int[] no = {0, 0};
                        //just the last two parameters of calculateCandidateResultSAW method is used
                        ArrayList<CandidateResult> resultInApp = leader.calculateCandidateResultSAW(
                                false, 0, 0, 0, no, "null", false, leader.manager.getApplication(i).getReceiveResultFromBrokersSAW());
                        leader.ultimateResultSAW_L.add(i, leader.findCandidateCloudWithMaxScore(resultInApp));
                        oneOrMultiple = "M";
                    }
                    leader.print.printUltimateResultSAW(i, leader);
                    writeInFile(br, leader.ultimateResultSAW_L.get(i), oneOrMultiple);
                } else {
                    leader.ultimateResultSAW_L.add(i, null);
                    br.write(leader.manager.getApplication(i).getId() + "," + 0 + "," + 0 + "," + 0 + "," + 0 + "," + 0 + "," + 0 + "," + "E");
                    br.newLine();
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Leader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //==================================================================================================================
    //========================================       method 1_3: SAW_Global       ======================================
    //==================================================================================================================

    /**
     * writingFileSAW_G method write The best final answer in file for each of applications
     * in this method application get best result from globalBroker (base on SAW method)
     *
     * @param leader object of Leader class
     */
    void writingFileSAW_G(Leader leader) {
        System.out.println("===============    method 1_3: SAW_Global   ===============");
        createDirectory("Dir-SAW-G");
        // set path for saving file
        Path currentRelativePath = Paths.get("");
        String currentPath = currentRelativePath.toAbsolutePath().toString();
        String newPath = currentPath + "\\Dir-SAW-G";
        File fileSAW_G = new File(newPath, System.currentTimeMillis() + "SAW-G.csv");
        try (BufferedWriter br = new BufferedWriter(new FileWriter(fileSAW_G))) {
            for (int i = 0; i < BasicInformation.getAppNumber(); i++) {
                if (leader.manager.getApplication(i).getResultFromGlobalBrokerSAW() != null) {
                    leader.ultimateResultSAW_G.add(i, leader.manager.getApplication(i).getResultFromGlobalBrokerSAW());
                    leader.print.printUltimateResultSAWGB(i, leader);
                    writeInFile(br, leader.ultimateResultSAW_G.get(i), "");
                } else {
                    leader.ultimateResultSAW_G.add(i, null);
                    br.write(leader.manager.getApplication(i).getId() + "," + 0 + "," + 0 + "," + 0 + "," + 0 + "," + 0);
                    br.newLine();
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Leader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //==================================================================================================================
    //========================================          Rank: SAW_Global          ======================================
    //==================================================================================================================


    /**
     * writingFile_SAW_G_Rank method receive two ArrayList of candidateResults in globalBroker (base on SAW method)that
     * sort in descending order base on score of each alternative and determining ranking abnormality is exist or not,
     * then write result in file
     *
     * @param firstSortResultSAW  sorted ArrayList<CandidateResult> before delete latest alternative
     * @param secondSortResultSAW sorted ArrayList<CandidateResult> after delete latest alternative and running SAW
     *                            again on it
     */
    public void writingFile_SAW_G_Rank(ArrayList<CandidateResult> firstSortResultSAW, ArrayList<CandidateResult> secondSortResultSAW) {
        createDirectory("Dir-Rank-SAW-G");
        // set path for saving file
        Path currentRelativePath = Paths.get("");
        String currentPath = currentRelativePath.toAbsolutePath().toString();
        String newPath = currentPath + "\\Dir-Rank-SAW-G";
        File fileRank_SAW_G = new File(newPath, System.currentTimeMillis() + "Rank-SAW-G.csv");
        writeRankInFile(fileRank_SAW_G, firstSortResultSAW, secondSortResultSAW, true);
    }

    //==================================================================================================================
    //========================================    method 2_1: TOPSIS_Local_Max    ======================================
    //==================================================================================================================

    /**
     * writingFileTOPSIS_L_Max method write The final answer in file for each of applications
     * in this method applications receive result/s (base on TOPSIS method) from brokers that connect to it/them and
     * select alternative with maximum score
     *
     * @param leader object of Leader class
     */
    void writingFileTOPSIS_L_Max(Leader leader) {
        System.out.println("===============     method 2_1: TOPSIS_Local_Max     ===============");
        createDirectory("Dir-TOPSIS-L-Max");
        // set path for saving file
        Path currentRelativePath = Paths.get("");
        String currentPath = currentRelativePath.toAbsolutePath().toString();
        String newPath = currentPath + "\\Dir-TOPSIS-L-Max";
        File fileTOPSIS_L_M = new File(newPath, System.currentTimeMillis() + "TOPSIS-L-M.csv");
        try (BufferedWriter br = new BufferedWriter(new FileWriter(fileTOPSIS_L_M))) {
            for (int i = 0; i < BasicInformation.getAppNumber(); i++) {
                if (!leader.manager.getApplication(i).getReceiveResultFromBrokersTOPSIS().isEmpty()) {
                    String oneOrMultiple;
                    if (leader.manager.getApplication(i).getReceiveResultFromBrokersTOPSIS().size() == 1) {
                        leader.ultimateResultTOPSIS_L_M.add(i, leader.manager.getApplication(i).getReceiveResultFromBrokersTOPSIS().get(0));
                        oneOrMultiple = "O";
                    } else {
                        leader.ultimateResultTOPSIS_L_M.add(i, leader.findCandidateCloudWithMaxScore(leader.manager.getApplication(i).getReceiveResultFromBrokersTOPSIS()));
                        oneOrMultiple = "M";
                    }
                    leader.print.printUltimateResultTOPSIS_M(i, leader);
                    writeInFile(br, leader.ultimateResultTOPSIS_L_M.get(i), oneOrMultiple);
                } else {
                    leader.ultimateResultTOPSIS_L_M.add(i, null);
                    br.write(leader.manager.getApplication(i).getId() + "," + 0 + "," + 0 + "," + 0 + "," + 0 + "," + 0 + "," + 0 + "," + "E");
                    br.newLine();

                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Leader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //==================================================================================================================
    //========================================      method 2_2: TOPSIS_Local      ======================================
    //==================================================================================================================

    /**
     * writingFileTOPSIS_L method write The final answer in file for each of applications
     * in this method applications receive result/s (base on TOPSIS method) from brokers that connect to it/them and
     * run SAW TOPSIS on receive result again
     *
     * @param leader object of Leader class
     */
    void writingFileTOPSIS_L(Leader leader) {
        System.out.println("===============     method 2_2: TOPSIS_Local     ===============");
        createDirectory("Dir-TOPSIS-L");
        // set path for saving file
        Path currentRelativePath = Paths.get("");
        String currentPath = currentRelativePath.toAbsolutePath().toString();
        String newPath = currentPath + "\\Dir-TOPSIS-L";
        File fileTOPSIS_L = new File(newPath, System.currentTimeMillis() + "TOPSIS-L.csv");
        try (BufferedWriter br = new BufferedWriter(new FileWriter(fileTOPSIS_L))) {
            for (int i = 0; i < BasicInformation.getAppNumber(); i++) {
                if (!leader.manager.getApplication(i).getReceiveResultFromBrokersTOPSIS().isEmpty()) {
                    String oneOrMultiple;
                    if (leader.manager.getApplication(i).getReceiveResultFromBrokersTOPSIS().size() == 1) {
                        leader.ultimateResultTOPSIS_L.add(i, leader.manager.getApplication(i).getReceiveResultFromBrokersTOPSIS().get(0));
                        oneOrMultiple = "O";
                    } else {
                        int[] no = {0, 0};
                        //just the last two parameters of calculateCandidateResultTOPSIS method is used
                        ArrayList<CandidateResult> resultInApp = leader.calculateCandidateResultTOPSIS(
                                false, 0, 0, 0, no, "null", false, leader.manager.getApplication(i).getReceiveResultFromBrokersTOPSIS(), false);
                        leader.ultimateResultTOPSIS_L.add(i, leader.findCandidateCloudWithMaxScore(resultInApp));
                        oneOrMultiple = "M";
                    }
                    leader.print.printUltimateResultTOPSIS(i, leader);
                    writeInFile(br, leader.ultimateResultTOPSIS_L.get(i), oneOrMultiple);
                } else {
                    leader.ultimateResultTOPSIS_L.add(i, null);
                    br.write(leader.manager.getApplication(i).getId() + "," + 0 + "," + 0 + "," + 0 + "," + 0 + "," + 0 + "," + 0 + "," + "E");
                    br.newLine();

                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Leader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //==================================================================================================================
    //========================================      method 2_3: TOPSIS_Global      =====================================
    //==================================================================================================================

    /**
     * writingFileTOPSIS_G method write The best final answer in file for each of applications
     * in this method application get best result from globalBroker (base on TOPSIS method)
     *
     * @param leader object of Leader class
     */
    void writingFileTOPSIS_G(Leader leader) {
        System.out.println("================ method 2_3: TOPSIS_Global ===============");
        createDirectory("Dir-TOPSIS-G");
        // set path for saving file
        Path currentRelativePath = Paths.get("");
        String currentPath = currentRelativePath.toAbsolutePath().toString();
        String newPath = currentPath + "\\Dir-TOPSIS-G";
        File fileTOPSIS_G = new File(newPath, System.currentTimeMillis() + "TOPSIS-G.csv");
        try (BufferedWriter br = new BufferedWriter(new FileWriter(fileTOPSIS_G))) {
            for (int i = 0; i < BasicInformation.getAppNumber(); i++) {
                if (leader.manager.getApplication(i).getResultFromGlobalBrokerTOPSIS() != null) {
                    leader.ultimateResultTOPSIS_G.add(i, leader.manager.getApplication(i).getResultFromGlobalBrokerTOPSIS());
                    leader.print.printUltimateResultTOPSISGB(i, leader);
                    writeInFile(br, leader.ultimateResultTOPSIS_G.get(i), "");
                } else {
                    leader.ultimateResultTOPSIS_G.add(i, null);
                    br.write(leader.manager.getApplication(i).getId() + "," + 0 + "," + 0 + "," + 0 + "," + 0 + "," + 0);
                    br.newLine();

                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Leader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //==================================================================================================================
    //==========================================        Rank: TOPSIS_Global       ======================================
    //==================================================================================================================

    /**
     * writingFile_TOPSIS_G_Rank method receive two ArrayList of candidateResults in globalBroker (base on TOPSIS method)
     * that sort in descending order base on score of each alternative and determining ranking abnormality is exist
     * or not, then write result in file
     *
     * @param firstSortResultTOPSIS_G  sorted ArrayList<CandidateResult> before delete latest alternative
     * @param secondSortResultTOPSIS_G sorted ArrayList<CandidateResult> after delete latest alternative and
     *                                 running TOPSIS again on it
     */
    public void writingFile_TOPSIS_G_Rank(ArrayList<CandidateResult> firstSortResultTOPSIS_G, ArrayList<CandidateResult> secondSortResultTOPSIS_G) {

        createDirectory("Dir-Rank-TOPSIS-G");
        // set path for saving file
        Path currentRelativePath = Paths.get("");
        String currentPath = currentRelativePath.toAbsolutePath().toString();
        String newPath = currentPath + "\\Dir-Rank-TOPSIS-G";
        File fileRank_TOPSIS_G = new File(newPath, System.currentTimeMillis() + "Rank-TOPSIS-G.csv");
        writeRankInFile(fileRank_TOPSIS_G, firstSortResultTOPSIS_G, secondSortResultTOPSIS_G, true);
    }


    /**
     * writingFile_Precision method  create directory and file for each of caller then call writingInFilePrecision method
     *
     * @param sortResult               ArrayList<CandidateResult> that contain first sorted result
     * @param methodSAW_TOPSIS_ETOPSIS 0 means caller is SAW, 1 means caller is TOPSIS and 2 means caller is ETOPSIS
     */
    public void writingFile_Precision(ArrayList<CandidateResult> sortResult, int methodSAW_TOPSIS_ETOPSIS) {
        if (methodSAW_TOPSIS_ETOPSIS == 0) {
            createDirectory("Dir-Precision-SAW-G");

            Path currentRelativePath = Paths.get("");
            String currentPath = currentRelativePath.toAbsolutePath().toString();
            String newPath = currentPath + "\\Dir-Precision-SAW-G";
            File filePrecision_SAW_G = new File(newPath, System.currentTimeMillis() + "precision-SAW-G.csv");
            writingInFilePrecision(filePrecision_SAW_G, sortResult, true);

        } else if (methodSAW_TOPSIS_ETOPSIS == 1) {
            createDirectory("Dir-Precision-TOPSIS-G");
            Path currentRelativePath = Paths.get("");
            String currentPath = currentRelativePath.toAbsolutePath().toString();
            String newPath = currentPath + "\\Dir-Precision-TOPSIS-G";
            File filePrecision_TOPSIS_G = new File(newPath, System.currentTimeMillis() + "precision-TOPSIS-G.csv");
            writingInFilePrecision(filePrecision_TOPSIS_G, sortResult, true);

        } else {
            createDirectory("Dir-Precision-E-TOPSIS-G");
            Path currentRelativePath = Paths.get("");
            String currentPath = currentRelativePath.toAbsolutePath().toString();
            String newPath = currentPath + "\\Dir-Precision-E-TOPSIS-G";
            File filePrecision_E_TOPSIS_G = new File(newPath, System.currentTimeMillis() + "precision-E-TOPSIS-G.csv");
            writingInFilePrecision(filePrecision_E_TOPSIS_G, sortResult, false);
        }

    }

    /**
     * writingFile_E_TOPSIS_G_Rank method receive two ArrayList of candidateResults in globalBroker (base on ETOPSIS method)
     * that sort in descending order base on lambdaScore of each alternative and determining ranking abnormality is exist
     * or not, then write result in file
     *
     * @param firstSortResult_E_TOPSIS_G  sorted ArrayList<CandidateResult> before delete latest alternative
     * @param secondSortResult_E_TOPSIS_G sorted ArrayList<CandidateResult> after delete latest alternative and
     *                                    running ETOPSIS again on it
     */
    public void writingFile_E_TOPSIS_G_Rank(ArrayList<CandidateResult> firstSortResult_E_TOPSIS_G, ArrayList<CandidateResult> secondSortResult_E_TOPSIS_G) {
        createDirectory("Dir-Rank-E-TOPSIS-G");
        // set path for saving file
        Path currentRelativePath = Paths.get("");
        String currentPath = currentRelativePath.toAbsolutePath().toString();
        String newPath = currentPath + "\\Dir-Rank-E-TOPSIS-G";
        File fileRank_E_TOPSIS_G = new File(newPath, System.currentTimeMillis() + "Rank-E-TOPSIS-G.csv");
        writeRankInFile(fileRank_E_TOPSIS_G, firstSortResult_E_TOPSIS_G, secondSortResult_E_TOPSIS_G, false);
    }

    /**
     * writeRankInFile method receive two sorter ArrayList<CandidateResult> from globalBroker an then determine ranking
     * abnormality is exist or not
     * if score of first alternative is 1 it means score of other alternative is 0: in this situation peruse of ranking
     * abnormality is pointless (write ApplicationId and 0000 in file)
     * if order of all of alternative in firstSortResult and secondSortResult is the same there is not ranking
     * abnormality (write ApplicationId and 1111 in file)
     * if order of all one or more alternative in firstSortResult and secondSortResult is different there is ranking
     * abnormality (write ApplicationId and and number of rank/s that different)
     *
     * @param file                        file that must write result in it
     * @param firstSortResult             ArrayList<CandidateResult> that sort in descending order before delete latest alternative
     * @param secondSortResult            ArrayList<CandidateResult> that sort in descending order after delete latest alternative
     * @param methodTOPSISAndSAWorETOPSIS boolean variable true means caller of this method is SAW or TOPSIS and false
     *                                    means caller is ETOPSIS; if caller is SAW or TOPSIS method operate base on score
     *                                    and if caller is ETOPSIS operate base on lambdaScore of each alternative
     */
    private void writeRankInFile(File file, ArrayList<CandidateResult> firstSortResult, ArrayList<CandidateResult> secondSortResult, boolean methodTOPSISAndSAWorETOPSIS) {
        double first = Double.MAX_VALUE;
        double second = Double.MAX_VALUE;
        try (BufferedWriter br = new BufferedWriter(new FileWriter(file))) {
            //if score or lambdaScore of first alternative is 1 write applicationId and 0000 in file
            if (methodTOPSISAndSAWorETOPSIS) {
                first = firstSortResult.get(0).getScore();
                second = secondSortResult.get(0).getScore();
            } else {
                first = firstSortResult.get(0).getLambdaScore();
                second = secondSortResult.get(0).getLambdaScore();
            }
            if ((first == 1 && second == 1) || (first == 0.8 && second == 0.8) || (first == 0.889 && second == 0.889)) {
                br.write(firstSortResult.get(0).getApplicationId() + "," + "0000");
            } else {
                /* if order of one or more alternative is not the same  write applicationId and number of
                number of rank/s that different in file*/
                boolean flag = true;
                for (int i = 0; i < firstSortResult.size(); i++) {
                    int rankingAbnormalityNumber = i + 1;
                    if (firstSortResult.get(i).getCloudId() != secondSortResult.get(i).getCloudId() ||
                            firstSortResult.get(i).getTotalCost() != secondSortResult.get(i).getTotalCost() ||
                            firstSortResult.get(i).getTotalResponseTime() != secondSortResult.get(i).getTotalResponseTime()) {
                        br.write(firstSortResult.get(i).getApplicationId() + "," + secondSortResult.get(i).getApplicationId() + "," + rankingAbnormalityNumber);
                        br.newLine();
                        flag = false;
                    }
                }
                // if order of all alternative is the same write write applicationId and 1111 in file
                if (flag) {
                    int rankingUniformity = 1111;
                    System.out.println("FLAG");
                    br.write(firstSortResult.get(0).getApplicationId() + "," + "1111");
                    br.newLine();
                }
            }
            br.write("*");
        } catch (IOException ex) {
            Logger.getLogger(Leader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * writingInFilePrecision method receive ArrayList<CandidateResult>, determine caller and calculate difference of
     * score or lambdaScore then write results in file
     *
     * @param filePrecision            file that must write result in it
     * @param sortResult               ArrayList<CandidateResult> that sort in descending order base on score or lambdaScore
     * @param methodSAW_TOPSIS_ETOPSIS boolean variable true means caller is SAW or TOPSIS and false means caller
     *                                 is ETOPSIS; if caller is SAW or TOPSIS method operate base on score and if
     *                                 caller is ETOPSIS operate base on lambdaScore of each alternative
     */
    private void writingInFilePrecision(File filePrecision, ArrayList<CandidateResult> sortResult, boolean methodSAW_TOPSIS_ETOPSIS) {
        try (BufferedWriter br = new BufferedWriter(new FileWriter(filePrecision))) {
            //if caller is SAW/TOPSIS calculate difference base on score then write applicationId and differences in file
            if (methodSAW_TOPSIS_ETOPSIS) {
                for (int i = 0; i < sortResult.size() - 1; i++) {
                    int subsequent = i + 1;
                    Double number = (double) Math.round((sortResult.get(i).getScore() - sortResult.get(subsequent).getScore()) * 10000) / 10000;
                    br.write(sortResult.get(i).getApplicationId() + "," + String.valueOf(number));
                    br.newLine();
                }
            } else {
                //if caller is ETOPSIS calculate difference base on lambdaScore then write applicationId and differences in file
                for (int i = 0; i < sortResult.size() - 1; i++) {
                    int subsequent = i + 1;
                    Double number = (double) Math.round((sortResult.get(i).getLambdaScore() - sortResult.get(subsequent).getLambdaScore()) * 10000) / 10000;
                    br.write(sortResult.get(i).getApplicationId() + "," + String.valueOf(number));
                    br.newLine();
                }
            }
            br.write("*");
        } catch (IOException ex) {
            Logger.getLogger(Leader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * writeInFile method receive ultimate result from caller and then write important attribute of it in file
     *
     * @param br              write result in br
     * @param candidateResult an object of candidate result that contain ultimate result
     * @param oneOrMultiple   string that could O,E or M if size of receive results from brokers is zero oneOrMultiple is E,
     *                        if size of it is 1 oneOrMultiple is O, and if size of it is more than 1 oneOrMultiple is M
     */
    private void writeInFile(BufferedWriter br, CandidateResult candidateResult, String oneOrMultiple) {
        try {
            br.write(candidateResult.getApplicationId() + "," +
                    candidateResult.getBrokerId() + "," +
                    candidateResult.getCloudId() + "," +
                    candidateResult.getTotalCost() + "," +
                    candidateResult.getTotalResponseTime()[0] +
                    "," + candidateResult.getTotalResponseTime()[1] +
                    "," + candidateResult.getScore());
            if (!oneOrMultiple.isEmpty()) {
                br.write("," + oneOrMultiple);
            }
            br.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * creteDirectory method get an string and create a directory that name of it is equal to input string
     *
     * @param DirectoryName string as name of directory
     */
    private void createDirectory(String DirectoryName) {
        File theDir = new File(DirectoryName);
        // if the directory does not exist, create it
        if (!theDir.exists()) {
            //System.out.println("creating directory: " + theDir);
            //boolean result = false;

            try {
                theDir.mkdir();
                // result = true;
            } catch (SecurityException se) {
                se.printStackTrace();
            }
//            if (result) {
//                System.out.println("DIR created");
//            }
        }
    }
//    void writingFileSAW_G_Second(Leader leader) {
//        System.out.println("===============    Second Rank: SAW_Global   ===============");
//        createDirectory("Dir-Rank-SAW-S-G");
//        // set path for saving file
//        Path currentRelativePath = Paths.get("");
//        String currentPath = currentRelativePath.toAbsolutePath().toString();
//        String newPath = currentPath + "\\Dir-Rank-SAW-S-G";
//        File fileSecondRank_SAW_G = new File(newPath, System.currentTimeMillis() + "Second-Rank-SAW-G.csv");
//
//        //writeRankInFile(fileSecondRank_SAW_G, firstSortResult_E_TOPSIS_G, secondSortResult_E_TOPSIS_G, false);
//    }

}
