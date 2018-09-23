/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Lighting;
import javafx.scene.effect.Shadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import org.json.simple.parser.JSONParser;
import project.*;
import utility.UtilColor;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import static javafx.scene.control.ContentDisplay.CENTER;

/**
 * @author Nazanin Tafreshi
 */
public class ShowEntitySc implements Initializable {

    Leader leader = new Leader();
    JSONParser parser = new JSONParser();
    Rectangle rect;
    Label lbl;
    Label[] appLabel = new Label[BasicInformation.getAppNumber()];
    Label[] brokerLabel;
    Label[] cloudLabel = new Label[BasicInformation.getCloudNumber()];
    Label[] sensorLabel = new Label[BasicInformation.getSensorNumber()];

    Line[][] appBrokerLines = new Line[BasicInformation.getAppNumber()][BasicInformation.getBrokerNumber()];
    Line[][] brokerCloudLines = new Line[BasicInformation.getBrokerNumber()][BasicInformation.getCloudNumber()];

    @FXML
    private AnchorPane pane;

    // length & width of labels
    int appLength = 62, appWidth = 80;
    int brokerLength = 62, brokerWidth = 50;
    int cloudLength = 62, cloudWidth = 65;
    int sensorLength = 62, sensorWidth = 80;
    Timeline secondsWonder;
    AnimationTimer SAWanimationTimer, TOPSISanimationTimer;
    StaticObjects staticObj = new StaticObjects();

    @FXML
    private void SAWButtonAction(ActionEvent SAWevent) {
        if (TOPSISanimationTimer != null) {
            TOPSISanimationTimer.stop();
        }
        for (int i = 0; i < BasicInformation.getAppNumber(); i++) {
            if (!leader.getManager().getApplication(i).getReceiveResultFromBrokersTOPSIS().isEmpty()) {
                getFinalResultTOPSIS(i, Color.rgb(251, 194, 26),
                        Color.rgb(255, 55, 155), Color.rgb(0, 100, 240), 1.7f);
            } else {
                ((Rectangle) appLabel[i].getGraphic()).setFill(Color.rgb(251, 194, 26));
                appLabel[i].setEffect(new DropShadow(3, Color.rgb(251, 194, 26)));
            }
        }
        SAWanimationTimer = new AnimationTimer() {
            long lastTime;
            int count = 0;

            @Override
            public void handle(long now) {
                if (now - lastTime > 100_000_0000L) {
                    lastTime = now;
                } else {
                    return;
                }
                if (count < BasicInformation.getAppNumber()) {
                    utility.UtilColor obj = new UtilColor();
                    getFinalResultSAW(count, obj.getColor(count + 7), obj.getColor(count + 7), obj.getColor(count + 7), 2.1f);
                    count++;
                } else {
                    stop();
                }
            }
        };
        SAWanimationTimer.start();
    }

    @FXML
    private void TOPSISbuttonAction(ActionEvent TOPSISevent) {
        if (SAWanimationTimer != null) {
            SAWanimationTimer.stop();
        }
        for (int i = 0; i < BasicInformation.getAppNumber(); i++) {
            if (!leader.getManager().getApplication(i).getReceiveResultFromBrokersSAW().isEmpty()) {
                getFinalResultSAW(i, Color.rgb(251, 194, 26),
                        Color.rgb(255, 55, 155), Color.rgb(0, 100, 240), 1.7f);
            } else {
                ((Rectangle) appLabel[i].getGraphic()).setFill(Color.rgb(251, 194, 26));
                appLabel[i].setEffect(new DropShadow(3, Color.rgb(251, 194, 26)));
            }
        }
        TOPSISanimationTimer = new AnimationTimer() {
            long lastTime;
            int count = 0;

            @Override
            public void handle(long now) {
                if (now - lastTime > 100_000_0000L) {
                    lastTime = now;
                } else {
                    return;
                }
                if (count < BasicInformation.getAppNumber()) {
                    utility.UtilColor obj = new UtilColor();
                    getFinalResultTOPSIS(count, obj.getColor(count), obj.getColor(count), obj.getColor(count), 2.1f);
                    count++;
                } else {
                    stop();
                }
            }
        };
        TOPSISanimationTimer.start();
    }

    @FXML
    private void BackButtonAction(ActionEvent backEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("ChooseJsonScFXML.fxml"));
            pane.getScene().setRoot(root);
            secondsWonder.stop();
            StaticObjects.constantApplications.clear();
            StaticObjects.constantCloudProviders.clear();
            StaticObjects.constantSensorNetworks.clear();
            StaticObjects.appFlag = false;
            StaticObjects.clFlag = false;
            StaticObjects.snFlag = false;

        } catch (IOException ex) {
            Logger.getLogger(ShowEntitySc.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        secondsWonder = new Timeline(new KeyFrame(Duration.ZERO, new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                //   if (secondsWonder.getCycleCount() != 0) {
                leader = new Leader();
                try {
                    pane.getChildren().clear();
                    appLabel = new Label[BasicInformation.getAppNumber()];
                    brokerLabel = new Label[BasicInformation.getBrokerNumber()];
                    cloudLabel = new Label[BasicInformation.getCloudNumber()];
                    sensorLabel = new Label[BasicInformation.getSensorNumber()];
                    drawingApps();
                    drawingClouds();
                    drawingSensors();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //  }
            }
        }), new KeyFrame(Duration.seconds(5)));
        

        secondsWonder.setCycleCount(staticObj.getCycleC());
        secondsWonder.play();

    }

   // drawing Sensors
    private void drawingSensors() throws NumberFormatException {
        for (int i = 0; i < BasicInformation.getCloudNumber(); i++) {
            String sensorCon[] = leader.getManager().getCloudProvider(i).getSensorCon().split(",");
            for (String sensorCon1 : sensorCon) {
                SensorNetwork sensorTemp = leader.getSensor(Integer.parseInt(sensorCon1));
                if (sensorLabel[sensorTemp.getId()] == null) {
                    String sensorInfo = "SN " + sensorTemp.getId() + "\n" + sensorTemp.getTaskType().charAt(0) + "\n"
                            + sensorTemp.getCost() + " $" + "\n" + Arrays.toString(sensorTemp.getResponseTime());
                    sensorLabel[sensorTemp.getId()] = information(sensorInfo,
                            sensorTemp.getX(), sensorTemp.getY(),
                            sensorLength, sensorWidth, 0, 240, 0,
                            "Sensor network " + sensorTemp.getId()
                                    + "\n=============="
                                    + "\n" + "Task type: " + sensorTemp.getTaskType()
                                    + "\n" + "Cost: " + sensorTemp.getCost() + "\n"
                                    + "Response time: " + Arrays.toString(sensorTemp.getResponseTime()));
                }
                drawLine(cloudLabel[i], sensorLabel[sensorTemp.getId()], cloudLength, cloudWidth, sensorLength, 0, 100, 240);
            }

        }
    }
    // drawing clouds

    private void drawingClouds() throws NumberFormatException {
        for (int i = 0; i < BasicInformation.getBrokerNumber(); i++) {
            String cloudCon[] = leader.getManager().getBroker(i).getCloudCon().split(",");
            for (int j = 0; j < cloudCon.length; j++) {
                CloudProvider cloudTemp = leader.getCloud(Integer.parseInt(cloudCon[j]));

                if (cloudLabel[cloudTemp.getId()] == null) {

                    // ============== collective Information of cloud provider for show in tooltip    ==================
                    ArrayList allRt = new ArrayList();

                    for (int k = 0; k < cloudTemp.getResponseTimeForSensorCon().size(); k++) {
                        allRt.add(Arrays.toString(cloudTemp.getResponseTimeForSensorCon().get(k)));
                    }

                    String allCost = cloudTemp.getCostForSensorCon().toString().substring(1);
                    allCost = allCost.substring(0, allCost.length() - 1);
                    String allRtStr = allRt.toString();
                    allRtStr = allRtStr.substring(1);
                    allRtStr = allRtStr.substring(0, allRtStr.length() - 1);
                    // =================================================================================================

                    String cloudInfo = "CP " + cloudTemp.getId() + "\n "
                            + cloudTemp.getCost() + " $" + "\n" + Arrays.toString(cloudTemp.getResponseTime());
                    cloudLabel[cloudTemp.getId()] = information(cloudInfo,
                            cloudTemp.getX(), cloudTemp.getY(), cloudLength,
                            cloudWidth, 0, 100, 240, "Cloud provider " + cloudTemp.getId()
//                                    + "\n" + "Cost: " + cloudTemp.getCost() + "\n"
//                                    + "Response time: " + Arrays.toString(cloudTemp.getResponseTime())
                                    + "\n=============="
                                    + "\n" + "( CP + SN ) cost: " + "\n" + allCost
                                    + "\n" + "\n" + "( CP + SN ) response time:" + "\n" + allRtStr);
                }
                brokerCloudLines[i][j] = drawLine(brokerLabel[i],
                        cloudLabel[cloudTemp.getId()], brokerLength,
                        brokerWidth, cloudLength, 255, 55, 155);
            }
        }
    }
    // drawing apps

    private void drawingApps() throws NumberFormatException {
        for (int i = 0; i < BasicInformation.getAppNumber(); i++) {
            Application appFeatures = leader.getManager().getApplication(i);
            String appInfo = "App " + appFeatures.getId() + "\n "
                    + appFeatures.getTaskType().charAt(0) + "\n" + appFeatures.getCost()
                    + " $" + "\n" + Arrays.toString(appFeatures.getResponseTime());
            appLabel[i] = information(appInfo, appFeatures.getX(),
                    appFeatures.getY(), appLength, appWidth,
                    251, 194, 26, "Application  " + appFeatures.getId()
                            + "\n=============="
                            + "\n" + "Task type: " + appFeatures.getTaskType() + "\n"
                            + "Cost: " + appFeatures.getCost() + "\n"
                            + "Response time: " + Arrays.toString(appFeatures.getResponseTime())
                            + "\nWeight of cost: " + appFeatures.getWeightCost()
                            + "\nWeight of minRt: " + appFeatures.getWeightMinRt()
                            + "\nWeight of maxRt: " + appFeatures.getWeightMaxRt());
            drawingBrokers(i);
        }
    }
    // drawing brokers

    private void drawingBrokers(int i) throws NumberFormatException {
        String brokerCon[] = leader.getManager().getApplication(i).getBrokerCon().split(",");
        for (int j = 0; j < brokerCon.length; j++) {
            Broker brokerTemp = leader.getBroker(Integer.parseInt(brokerCon[j]));

            // =======================  collective Information of broker for show in tooltip    ========================
            int size = brokerTemp.getCloudProviders().size() / 4;
            ArrayList allCost = new ArrayList<>();

            for (int k = 0; k < size; k++) {
                allCost.add("CP" + brokerTemp.getCloudProviders().get(4 * k).toString() + "z");
                allCost.add(brokerTemp.getCloudProviders().get(3 + (4 * k)).toString());
            }

            String cost = allCost.toString();
            cost = cost.substring(1);
            cost = cost.substring(0, cost.length() - 1);
            cost = cost.replace("CP[", "CP ");
            cost = cost.replace("]z,", ":");
            String newCost = cost.replace("],", "]\n");
            newCost = newCost.replace("]", "");
            newCost = newCost.replace("[", "");
            // =========================================================================================================

            if (brokerLabel[brokerTemp.getId()] == null) {
                String brokerInfo = "Broker " + brokerTemp.getId() + "\n" +
                        brokerTemp.getCost() + " $";

                brokerLabel[brokerTemp.getId()] = information(brokerInfo,
                        brokerTemp.getX(), brokerTemp.getY(), brokerLength,
                        brokerWidth, 255, 55, 155, " Broker " + brokerTemp.getId()
//                                + "\n" + " Cost: " + brokerTemp.getCost() + "$"
                                + "\n============" + "\n" + " Total cost:\n" + " " + newCost);

            }
            appBrokerLines[i][j] = drawLine(appLabel[i], brokerLabel[brokerTemp.getId()],
                    appLength, appWidth, brokerLength, 251, 194, 26);

        }
    }

    /**
     * draw method get parameters and draws rectangle with respect to input
     * parameter
     *
     * @param x Coordinates the starting point
     * @param y Coordinates the starting point
     * @param a length of rectangle
     * @param b width of rectangle
     * @param color1 amount of red color
     * @param color2 amount of green color
     * @param color3 amount of blue color
     * @return Rectangle
     */
    public Rectangle draw(double x, double y, double a, double b, int color1, int color2, int color3) {
        rect = new Rectangle(x, y, a, b); // draw
        rect.setArcWidth(30);
        rect.setArcHeight(30);
        rect.setStrokeWidth(1);
        rect.setStrokeType(StrokeType.OUTSIDE);
        Lighting light = new Lighting();
        light.setSpecularExponent(13.48);
        light.setSurfaceScale(5.23);
        light.setDiffuseConstant(1.3);
        light.setSpecularConstant(1.02);
        light.setBumpInput(new Shadow());

        rect.setEffect(light);
        rect.setFill(Color.rgb(color1, color2, color3)); // set color
        return rect;

    }

    public static void hackTooltipStartTiming(Tooltip tooltip) {
        try {
            Field fieldBehavior = tooltip.getClass().getDeclaredField("BEHAVIOR");
            fieldBehavior.setAccessible(true);
            Object objBehavior = fieldBehavior.get(tooltip);

            Field fieldTimer = objBehavior.getClass().getDeclaredField("activationTimer");
            fieldTimer.setAccessible(true);
            Timeline objTimer = (Timeline) fieldTimer.get(objBehavior);

            objTimer.getKeyFrames().clear();
            objTimer.getKeyFrames().add(new KeyFrame(new Duration(250)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * draw method get parameters and draw label with respect to input parameter
     *
     * @param info informations about labels
     * @param x Coordinates the starting point
     * @param y Coordinates the starting point
     * @param width width of rectangle
     * @param length width of rectangle
     * @param color1 amount of red color
     * @param color2 amount of green color
     * @param color3 amount of blue color
     * @param tooltipInfo other informations about labels which will show in
     * tooltip
     * @return label
     */
    public Label information(String info, double x, double y, double length, double width, int color1, int color2, int color3, String tooltipInfo) {

        Label label = new Label(info, draw(x, y, length, width, color1, color2, color3));
        label.setLayoutX(x);
        label.setLayoutY(y);
        label.setTextAlignment(TextAlignment.CENTER);
        label.setAlignment(Pos.CENTER);
        label.setFont(Font.font("Times New Roman", 12.5));
        label.setEffect(new DropShadow(3, Color.rgb(color1, color2, color3)));
        label.setTextFill(Color.BLACK);
        label.setContentDisplay(CENTER);
        pane.getChildren().add(label);

        label.setOnMouseDragged(eventDrag -> {
            lbl = (Label) eventDrag.getSource();
            lbl.setLayoutX(eventDrag.getSceneX());
            lbl.setLayoutY(eventDrag.getSceneY());
            // lbl.setEffect(new DropShadow(5, Color.rgb(color1, color2, color3)));

        });

        /**
         * Show the features of the object if it is selected
         */
        Tooltip tooltip = new Tooltip(tooltipInfo);
        Tooltip.install(label, tooltip);
        hackTooltipStartTiming(tooltip);
        return label;
    }

    /**
     * Get final result from LG method
     *
     * @param i the ith object
     * @param appColor application's color
     * @param brokerColor broker's color
     * @param cloudColor cloud's color
     * @param strokeNum stroke of the lines
     */
    private void getFinalResultSAW(int i, Color appColor, Color brokerColor, Color cloudColor, float strokeNum) {
        if (!leader.getManager().getApplication(i).getReceiveResultFromBrokersSAW().isEmpty() && leader.ultimateResultSAW_L.get(i) != null) {
            Label ar = appLabel[leader.ultimateResultSAW_L.get(i).getApplicationId()];
            ((Rectangle) ar.getGraphic()).setFill(appColor);
            ar.setEffect(new DropShadow(3, appColor));
            Label br = brokerLabel[leader.ultimateResultSAW_L.get(i).getBrokerId()];
            ((Rectangle) br.getGraphic()).setFill(brokerColor);
            br.setEffect(new DropShadow(3, brokerColor));
            Label cr = cloudLabel[leader.ultimateResultSAW_L.get(i).getCloudId()];
            ((Rectangle) cr.getGraphic()).setFill(cloudColor);
            cr.setEffect(new DropShadow(3, cloudColor));
            String brokerCon[] = leader.getApp(leader.ultimateResultSAW_L.get(i)
                    .getApplicationId()).getBrokerCon().split(",");
            String cloudCon[] = leader.getBroker(leader.ultimateResultSAW_L.get(i)
                    .getBrokerId()).getCloudCon().split(",");

            for (int j = 0; j < brokerCon.length; j++) {
                if (appBrokerLines[i][j].getEndX() == br.getLayoutX() + brokerLength / 2) {
                    appBrokerLines[i][j].setStroke(appColor);
                    appBrokerLines[i][j].setStrokeWidth(strokeNum);
                    for (int k = 0; k < cloudCon.length; k++) {
                        Line BCLine = brokerCloudLines[leader.ultimateResultSAW_L.get(i).getBrokerId()][k];
                        if (BCLine.getEndX() == cr.getLayoutX() + cloudLength / 2) {
                            BCLine.setStroke(brokerColor);
                            BCLine.setStrokeWidth(strokeNum);

                        }
                    }
                    return;
                }
            }

        } else {
            Label ar = appLabel[i];
            ((Rectangle) ar.getGraphic()).setFill(Color.rgb(240, 230, 140));
            ar.setEffect(new DropShadow(3, Color.rgb(240, 230, 140)));

        }
    }

    /**
     * Get final result from LL method
     *
     * @param i the ith object
     * @param appColor application's color
     * @param brokerColor broker's color
     * @param cloudColor cloud's color
     * @param strokeNum stroke of the lines
     */
    private void getFinalResultTOPSIS(int i, Color appColor, Color brokerColor, Color cloudColor, float strokeNum) {
        if (!leader.getManager().getApplication(i).getReceiveResultFromBrokersTOPSIS().isEmpty() && leader.ultimateResultTOPSIS_L.get(i) != null) {
            Label ar = appLabel[leader.ultimateResultTOPSIS_L.get(i).getApplicationId()];
            ((Rectangle) ar.getGraphic()).setFill(appColor);
            ar.setEffect(new DropShadow(3, appColor));
            Label br = brokerLabel[leader.ultimateResultTOPSIS_L.get(i).getBrokerId()];
            ((Rectangle) br.getGraphic()).setFill(brokerColor);
            br.setEffect(new DropShadow(3, brokerColor));
            Label cr = cloudLabel[leader.ultimateResultTOPSIS_L.get(i).getCloudId()];
            ((Rectangle) cr.getGraphic()).setFill(cloudColor);
            cr.setEffect(new DropShadow(3, cloudColor));
            String brokerCon[] = leader.getApp(leader.ultimateResultTOPSIS_L.get(i)
                    .getApplicationId()).getBrokerCon().split(",");
            String cloudCon[] = leader.getBroker(leader.ultimateResultTOPSIS_L.get(i)
                    .getBrokerId()).getCloudCon().split(",");

            for (int j = 0; j < brokerCon.length; j++) {
                if (appBrokerLines[i][j].getEndX() == br.getLayoutX() + brokerLength / 2) {
                    appBrokerLines[i][j].setStroke(appColor);
                    appBrokerLines[i][j].setStrokeWidth(strokeNum);
                    for (int k = 0; k < cloudCon.length; k++) {
                        Line BCLine = brokerCloudLines[leader.ultimateResultTOPSIS_L.get(i).getBrokerId()][k];
                        if (BCLine.getEndX() == cr.getLayoutX() + cloudLength / 2) {
                            BCLine.setStroke(brokerColor);
                            BCLine.setStrokeWidth(strokeNum);

                        }
                    }
                    return;
                }
            }

        } else {
            Label ar = appLabel[i];
            ((Rectangle) ar.getGraphic()).setFill(Color.rgb(240, 230, 140));
            ar.setEffect(new DropShadow(3, Color.rgb(240, 230, 140)));

        }
    }

    /**
     * drawing line between two labels
     *
     * @param firstLabel first label
     * @param secondLabel second label
     * @param firstLength length of first label
     * @param firstWidth width of first label
     * @param secondLength length of second label
     * @param color1 amount of red color
     * @param color2 amount of green color
     * @param color3 amount of blue color
     */
    private Line drawLine(Label firstLabel, Label secondLabel, int firstLength, int firstWidth, int secondLength, int color1, int color2, int color3) {
        Line line = new Line(firstLabel.getLayoutX()
                + (firstLength / 2), firstLabel.getLayoutY()
                + firstWidth, secondLabel.getLayoutX()
                + (secondLength / 2), secondLabel.getLayoutY());
        pane.getChildren().add(line);

        line.toBack();
        line.startXProperty().bind(firstLabel.layoutXProperty().add(firstLength / 2));
        line.startYProperty().bind(firstLabel.layoutYProperty().add(firstWidth));
        line.endXProperty().bind(secondLabel.layoutXProperty().add(secondLength / 2));
        line.endYProperty().bind(secondLabel.layoutYProperty());
        line.setStroke(Color.rgb(color1, color2, color3));
        line.setStrokeWidth(1.7);
        return line;
    }

}
