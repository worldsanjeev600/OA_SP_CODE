package com.oneassist.serviceplatform.utilities;

import java.util.HashMap;
import java.util.Map;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;

public class DataSourceProjectMain {

    public static void main(String[] args) {
        TaskService taskService;
        ProcessInstance processInstance;
        try {
            // ProcessEngine processEngine =(ProcessEngine) context.getBean("processEngine");

            // DEV DB
            /*
             * ProcessEngine processEngine = ProcessEngineConfiguration .createStandaloneProcessEngineConfiguration().setJdbcUrl(
             * "jdbc:oracle:thin:@(description=(address_list=(address=(protocol=tcp)(port=1521)(host=10.16.0.47)))(connect_data=(SERVER=DEDICATED)(SERVICE_NAME=OADEVDB)))").
             * setJdbcUsername("oneassist_sp_test").setJdbcPassword("Oneassist_sp#123").setJdbcDriver("oracle.jdbc.driver.OracleDriver"). buildProcessEngine();
             */

            // SIT 19 DB
            /*
             * ProcessEngine processEngine = ProcessEngineConfiguration .createStandaloneProcessEngineConfiguration().setJdbcUrl(
             * "jdbc:oracle:thin:@(description=(address_list=(address=(protocol=tcp)(port=1522)(host=10.22.1.19)))(connect_data=(SERVER=DEDICATED)(SERVICE_NAME=OATESTDB)))").
             * setJdbcUsername("oneassist_spew").setJdbcPassword("password#123").setJdbcDriver("oracle.jdbc.driver.OracleDriver"). buildProcessEngine();
             */

            // DEV DB
            /*
             * ProcessEngine processEngine = ProcessEngineConfiguration .createStandaloneProcessEngineConfiguration().setJdbcUrl(
             * "jdbc:oracle:thin:@(description=(address_list=(address=(protocol=tcp)(port=1521)(host=10.16.0.47)))(connect_data=(SERVER=DEDICATED)(SERVICE_NAME=OADEVDB)))").
             * setJdbcUsername("oneassist_sp_test").setJdbcPassword("Oneassist_sp#123").setJdbcDriver("oracle.jdbc.driver.OracleDriver"). buildProcessEngine();
             */

            // OneAssist SIT DB
            /*
             * ProcessEngine processEngine = ProcessEngineConfiguration .createStandaloneProcessEngineConfiguration().setJdbcUrl(
             * "jdbc:oracle:thin:@(description=(address_list=(address=(protocol=tcp)(port=1521)(host=35.154.164.213)))(connect_data=(SERVER=DEDICATED)(SERVICE_NAME=orcl1)))").
             * setJdbcUsername("oneassist_sp").setJdbcPassword("Oneassistsp123").setJdbcDriver("oracle.jdbc.driver.OracleDriver"). buildProcessEngine();
             */

            // OneAssist UAT1 DB
            /*
             * ProcessEngine processEngine = ProcessEngineConfiguration .createStandaloneProcessEngineConfiguration().setJdbcUrl(
             * "jdbc:oracle:thin:@(description=(address_list=(address=(protocol=tcp)(port=1521)(host=182.18.170.100)))(connect_data=(SERVER=DEDICATED)(SERVICE_NAME=oadbuat)))").
             * setJdbcUsername("oauat_sp").setJdbcPassword("Oauat123").setJdbcDriver("oracle.jdbc.driver.OracleDriver"). buildProcessEngine();
             */

            // OneAssist UAT2 DB
            /*
             * ProcessEngine processEngine = ProcessEngineConfiguration .createStandaloneProcessEngineConfiguration().setJdbcUrl(
             * "jdbc:oracle:thin:@(description=(address_list=(address=(protocol=tcp)(port=1521)(host=54.208.182.25)))(connect_data=(SERVER=DEDICATED)(SERVICE_NAME=ORCL1)))").
             * setJdbcUsername("oauat2_sp").setJdbcPassword("oauat2#sp2").setJdbcDriver("oracle.jdbc.driver.OracleDriver"). buildProcessEngine();
             */

            // OneAssist PROD DB
            /*
             * ProcessEngine processEngine = ProcessEngineConfiguration .createStandaloneProcessEngineConfiguration().setJdbcUrl(
             * "jdbc:oracle:thin:@(DESCRIPTION = (ADDRESS = (PROTOCOL = TCP)(HOST = 192.168.38.202)(PORT = 1523))(LOAD_BALANCE = YES)(CONNECT_DATA =(SERVER = DEDICATED)(SERVICE_NAME = haoadb)))").
             * setJdbcUsername("oneassist_sp_app").setJdbcPassword("OneAssist_5P_app").setJdbcDriver("oracle.jdbc.driver.OracleDriver"). buildProcessEngine();
             */

            // SIT3
            /*
             * ProcessEngine processEngine = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration()
             * .setJdbcUrl("jdbc:oracle:thin:@(description=(address_list=(address=(protocol=tcp)(port=1521)(host=35.154.164.213)))(connect_data=(SERVER=DEDICATED)(SERVICE_NAME=orcl1)))")
             * .setJdbcUsername("OASIT3_SP").setJdbcPassword("OnePlaTf0r##").setDatabaseSchema("OASIT3_SP").setJdbcDriver("oracle.jdbc.driver.OracleDriver") // .setDatabaseSchemaUpdate("true")
             * .buildProcessEngine();
             */

            // SIT4
            ProcessEngine processEngine = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration()
                    .setJdbcUrl("jdbc:oracle:thin:@(description=(address_list=(address=(protocol=tcp)(port=1521)(host=35.154.164.213)))(connect_data=(SERVER=DEDICATED)(SERVICE_NAME=orcl1)))")
                    .setJdbcUsername("OASIT4_SP").setJdbcPassword("OneSeRV$Pl#").setDatabaseSchema("OASIT4_SP").setJdbcDriver("oracle.jdbc.driver.OracleDriver")
                    // .setDatabaseSchemaUpdate("true")
                    .buildProcessEngine();
            RuntimeService runtimeService = processEngine.getRuntimeService();
            taskService = processEngine.getTaskService();

            HistoryService historyService = processEngine.getHistoryService();

            RepositoryService repositoryService = processEngine.getRepositoryService();

            Map<String, Object> activitiMap = new HashMap<String, Object>();

            activitiMap.put("WEBCUSTOMER", "WEBCUSTOMER");
            activitiMap.put("ROLE_ASSIGNED_VERIFICATION", "ClaimsTeam");
            activitiMap.put("DocVerification", "Approved");
            activitiMap.put("ROLE_ASSIGNED_TECHVISIT", "ClaimsTeam");

            activitiMap.put("IS_PIN_CODE_SERVICEABLE", "Y");
            activitiMap.put("serviceRequestId", "332");
            // activitiMap.put("IS_PIN_CODE_SERVICEABLE", "Y");
            // activitiMap.put("ICClausePassed", "Yes");
            activitiMap.put("ICClausePassed", "No");
            // activitiMap.put("isTechVisit", 'N');//Default 'N'
            activitiMap.put("ROLE_ASSIGNED_ICDECISION", "ClaimsTeam");
            activitiMap.put("ICDecision", "Rejected");// 'BER-Approved'
            // activitiMap.put("ICDecision", "Approved");
            activitiMap.put("ROLE_ASSIGNED_REPAIRASSESSMENT", "ClaimsTeam");
            // activitiMap.put("ROLE_ASSIGNED_REPAIR", "ClaimsTeam");
            activitiMap.put("customerAsksRefund", 'N');
            activitiMap.put("isRepairCompleted", 'N');
            activitiMap.put("isEstimatedInvoiceVerified", "N");
            activitiMap.put("ROLE_ASSIGNED_CLAIMSETTLEMENT", "ClaimsTeam");
            activitiMap.put("isCustomerOptForSelfRepair", "N");// its when technician completed repair assessment, & then customer denying service

            // For activiti timer task for technician assigment...
            activitiMap.put("IS_TECHNICIAN_ASSIGNED", "N");
            activitiMap.put("IS_SR_CANCELLED_OR_CLOSED", "N");
            activitiMap.put("TIMER_INTVL_TECH_ASSIGN_SLA", "R3/PT30S");
            activitiMap.put("isAccidentalDamage", "N");

            String deployId = repositoryService.createDeployment().name("Activiti for BD").addClasspathResource("SP_HA_BD.bpmn").deploy().getId();
            processInstance = runtimeService.startProcessInstanceByKey("SP_HA_BD", activitiMap);
            System.out.println("Breakdown ID: " + deployId);

            deployId = repositoryService.createDeployment().name("Activiti for EW").addClasspathResource("SP_HA_EW.bpmn").deploy().getId();
            processInstance = runtimeService.startProcessInstanceByKey("SP_HA_EW", activitiMap);
            System.out.println("Extended Warranty Id: " + deployId);

            deployId = repositoryService.createDeployment().name("Activiti for AD").addClasspathResource("SP_HA_AD.bpmn").deploy().getId();
            processInstance = runtimeService.startProcessInstanceByKey("SP_HA_AD", activitiMap);
            System.out.println("Accidental Damage Id: " + deployId);

            deployId = repositoryService.createDeployment().name("Activiti for BR").addClasspathResource("SP_HA_BR.bpmn").deploy().getId();
            processInstance = runtimeService.startProcessInstanceByKey("SP_HA_BR", activitiMap);
            System.out.println("Burglary Id: " + deployId);

            deployId = repositoryService.createDeployment().name("Activiti for FR").addClasspathResource("SP_HA_FR.bpmn").deploy().getId();
            processInstance = runtimeService.startProcessInstanceByKey("SP_HA_FR", activitiMap);
            System.out.println("Fire Id: " + deployId);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("Exception is here: " + e.getMessage());
        }

    }

}
