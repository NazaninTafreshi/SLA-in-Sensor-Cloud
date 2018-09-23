package project;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.util.ArrayList;

import static project.BasicInformation.*;
import static project.Configurer.generateRandomNumber;
import static project.Configurer.generateTaskType;

/**
 * Created by Ayoubi on 27/04/2016.
 */
public class Manager {


    private ArrayList<Application> application;
    private ArrayList<Broker> broker;
    private ArrayList<CloudProvider> cloudProvider;
    private ArrayList<SensorNetwork> sensorNetwork;
    JSONArray appArray = null;
    JSONArray brokerArray = null;
    JSONArray cloudArray = null;
    JSONArray sensorArray = null;
    StaticObjects staticObjects;

    /* constructor for produce each object */
    public Manager() {
        application = new ArrayList<>();
        broker = new ArrayList<>();
        cloudProvider = new ArrayList<>();
        sensorNetwork = new ArrayList<>();
        staticObjects = new StaticObjects();
        JSONParser parser = new JSONParser();


        try {
            Object object = parser.parse(new FileReader(BasicInformation.getJsonFile()));

            JSONObject jsonObject = (JSONObject) object;
            BasicInformation.setAppNumber(Integer.parseInt((String) jsonObject.get("applicationNumber")));
            BasicInformation.setBrokerNumber(Integer.parseInt((String) jsonObject.get("brokerNumber")));
            BasicInformation.setCloudNumber(Integer.parseInt((String) jsonObject.get("cloudNumber")));
            BasicInformation.setSensorNumber(Integer.parseInt((String) jsonObject.get("sensorNetworkNumber")));

            /* Array for each Entity in json file */
            appArray = (JSONArray) jsonObject.get("application");
            brokerArray = (JSONArray) jsonObject.get("broker");
            cloudArray = (JSONArray) jsonObject.get("cloudProvider");
            sensorArray = (JSONArray) jsonObject.get("sensorNetwork");

            determineStaticOrVarier();
            setPrimAttrBroker();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * determineStaticOrVarier method is used for determining checkbox of which entity is selected
     * if checkbox select then attribute of that entity kept constant
     */
    private void determineStaticOrVarier() {
        // true means objects is constant
        if (staticObjects.isSensor()) {
            if (!staticObjects.isSnFlag()) {
                attrConsSensor();
            }
            attrSensorFromConsSensor();
            staticObjects.setSnFlag(true);
        } else {
            setPrimAttrSensor();
        }

        if (staticObjects.isCloud()) {
            if (!staticObjects.isClFlag()) {
                attrConsCloud();
            }
            attrCloudFromConsCloud();
            staticObjects.setClFlag(true);

        } else {
            setPrimAttrCloud();
        }

        if (staticObjects.isApp()) {
            if (!staticObjects.isAppFlag()) {
                attrConsApp();
            }
            attrAppFromConsApp();
            staticObjects.setAppFlag(true);
        } else {
            setPrimAttrApp();
        }
    }

    /**
     * set attributes of constant sensor networks
     */
    private void attrConsSensor() {
        for (int i = 0; i < BasicInformation.getSensorNumber(); i++) {
            Configurer configurerSn = new Configurer();
            configurerSn.setTaskType(generateTaskType());
            ConstantSensorNetwork sensorTemp = new ConstantSensorNetwork(generateRandomNumber(minCostSn, maxCostSn),
                    new int[]{generateRandomNumber(lowMinRtSn, upMinRtSn), generateRandomNumber(lowMaxRtSn, upMaxRtSn)},
                    configurerSn.getTaskType());
            StaticObjects.constantSensorNetworks.add(sensorTemp);
        }
    }

    /**
     * set attributes of sensor networks
     * with calling this method we create constant sensor network
     */
    private void attrSensorFromConsSensor() {
        for (int i = 0; i < BasicInformation.getSensorNumber(); i++) {
            SensorNetwork sensorTemp = new SensorNetwork(i);
            JSONObject sensorObject = (JSONObject) sensorArray.get(i);
            sensorTemp.setCloudCon((String) sensorObject.get("cloudProvider"));
            sensorNetwork.add(sensorTemp);
            sensorTemp.setCost(StaticObjects.constantSensorNetworks.get(i).getCost());
            sensorTemp.setResponseTime(StaticObjects.constantSensorNetworks.get(i).getResponseTime());
            sensorTemp.setTaskType(StaticObjects.constantSensorNetworks.get(i).getTaskType());
        }
    }

    /**
     * set attributes of sensor networks
     * with calling this method we create variable sensor networks
     */
    private void setPrimAttrSensor() {
        for (int i = 0; i < BasicInformation.getSensorNumber(); i++) {
            SensorNetwork sensorTemp = new SensorNetwork(i);
            JSONObject sensorObject = (JSONObject) sensorArray.get(i);
            sensorTemp.setCloudCon((String) sensorObject.get("cloudProvider"));
            sensorNetwork.add(sensorTemp);
            sensorTemp.setCost(generateRandomNumber(minCostSn, maxCostSn));
            sensorTemp.setResponseTime(
                    new int[]{generateRandomNumber(lowMinRtSn, upMinRtSn), generateRandomNumber(lowMaxRtSn, upMaxRtSn)});
            Configurer configurerSn = new Configurer();
            configurerSn.setTaskType(generateTaskType());
            sensorTemp.setTaskType(configurerSn.getTaskType());
        }
    }

    /**
     * set attributes of constant cloud providers
     */
    private void attrConsCloud() {
        for (int i = 0; i < BasicInformation.getCloudNumber(); i++) {
            ConstantCloudProvider cloudTemp = new ConstantCloudProvider(generateRandomNumber(lowCostCP, upCostCP),
                    new int[]{generateRandomNumber(lowMinRtCP, upMinRtCP), generateRandomNumber(lowMaxRtCP, upMaxRtCP)});
            StaticObjects.constantCloudProviders.add(cloudTemp);
        }
    }

    /**
     * set attributes of cloud providers
     * with calling this method we create constant cloud providers
     */
    private void attrCloudFromConsCloud() {
                    /* set primary attributes for cloudProviders*/
        for (int i = 0; i < BasicInformation.getCloudNumber(); i++) {
            CloudProvider cloudTemp = new CloudProvider(i);
            JSONObject cloudObject = (JSONObject) cloudArray.get(i);
            cloudTemp.setBrokerCon((String) cloudObject.get("broker"));
            cloudTemp.setSensorCon((String) cloudObject.get("sensorNetwork"));
            cloudProvider.add(cloudTemp);
            cloudTemp.setCost(StaticObjects.constantCloudProviders.get(i).getCost());
            cloudTemp.setResponseTime(StaticObjects.constantCloudProviders.get(i).getResponseTime());
        }
    }

    /**
     * set primary attributes of cloud providers
     */
    private void setPrimAttrCloud() {
        for (int i = 0; i < BasicInformation.getCloudNumber(); i++) {
            CloudProvider cloudTemp = new CloudProvider(i);
            JSONObject cloudObject = (JSONObject) cloudArray.get(i);
            cloudTemp.setBrokerCon((String) cloudObject.get("broker"));
            cloudTemp.setSensorCon((String) cloudObject.get("sensorNetwork"));
            cloudProvider.add(cloudTemp);
            cloudTemp.setCost(generateRandomNumber(lowCostCP, upCostCP));
            cloudTemp.setResponseTime(
                    new int[]{generateRandomNumber(lowMinRtCP, upMinRtCP), generateRandomNumber(lowMaxRtCP, upMaxRtCP)});
        }
    }

    /**
     * set attributes of constant applications
     */
    private void attrConsApp() {
        for (int i = 0; i < BasicInformation.getAppNumber(); i++) {
            Configurer configurerApp = new Configurer();
            configurerApp.setTaskType(generateTaskType());
            ConstantApplication appTemp = new ConstantApplication(generateRandomNumber(minCostApp, maxCostApp), configurerApp.getTaskType(),
                    new int[]{generateRandomNumber(lowMinRtApp, upMinRtApp), generateRandomNumber(lowMaxRtApp, upMaxRtApp)});
            StaticObjects.constantApplications.add(appTemp);
        }
    }

    /**
     * set attributes of applications
     * with calling this method we create constant applications
     */
    private void attrAppFromConsApp() {
        for (int i = 0; i < BasicInformation.getAppNumber(); i++) {
            Application appTemp = new Application(i);
            // for example appObject is {"broker":"1,2"}
            JSONObject appObject = (JSONObject) appArray.get(i);
            // for example appObject.get("broker") is: 1,2
            appTemp.setBrokerCon((String) appObject.get("broker"));
            appTemp.setCost(StaticObjects.constantApplications.get(i).getCost());
            appTemp.setResponseTime(StaticObjects.constantApplications.get(i).getResponseTime());
            appTemp.setTaskType(StaticObjects.constantApplications.get(i).getTaskType());
            appTemp.setWeightCost(StaticObjects.constantApplications.get(i).getWeightCost());
            appTemp.setWeightMinRt(StaticObjects.constantApplications.get(i).getWeightMinRt());
            appTemp.setWeightMaxRt(StaticObjects.constantApplications.get(i).getWeightMaxRt());
            application.add(appTemp);
        }
    }

    /**
     * set primary attributes of applications
     */
    private void setPrimAttrApp() {
        for (int i = 0; i < BasicInformation.getAppNumber(); i++) {
            Application appTemp = new Application(i);
            // for example appObject is {"broker":"1,2"}
            JSONObject appObject = (JSONObject) appArray.get(i);
            // for example appObject.get("broker") is: 1,2
            appTemp.setBrokerCon((String) appObject.get("broker"));
            Configurer configurerApp = new Configurer();
            configurerApp.setTaskType(generateTaskType());
            appTemp.setCost(generateRandomNumber(minCostApp, maxCostApp));
            appTemp.setResponseTime(
                    new int[]{generateRandomNumber(lowMinRtApp, upMinRtApp), generateRandomNumber(lowMaxRtApp, upMaxRtApp)});
            appTemp.setTaskType(configurerApp.getTaskType());

            // weight for importance of sla parameter include weight of cost, minRt and maxRt
            double[] weight;
            weight = appTemp.generateWeight(appTemp.getCost(), appTemp.getResponseTime()[0], appTemp.getResponseTime()[0]);
            appTemp.setWeightCost(weight[0]);
            appTemp.setWeightMinRt(weight[1]);
            appTemp.setWeightMaxRt(weight[2]);
            application.add(appTemp);
        }
    }

    /**
     * set primary attributes of brokers
     */
    private void setPrimAttrBroker() {
        for (int i = 0; i < BasicInformation.getBrokerNumber(); i++) {
            Broker brokTemp = new Broker(i);
            JSONObject brokerObject = (JSONObject) brokerArray.get(i);
            brokTemp.setAppCon((String) brokerObject.get("application"));
            brokTemp.setCloudCon((String) brokerObject.get("cloudProvider"));
            broker.add(brokTemp);
        }
    }

    public Application getApplication(int i) {
        return application.get(i);
    }

    public Broker getBroker(int i) {
        return broker.get(i);
    }

    public CloudProvider getCloudProvider(int i) {
        return cloudProvider.get(i);
    }

    public SensorNetwork getSensorNetwork(int i) {
        return sensorNetwork.get(i);
    }

}
