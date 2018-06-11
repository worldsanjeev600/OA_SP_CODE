ALTER SESSION SET CURRENT_SCHEMA=oneassist_sp;
CREATE TABLE  "OA_SERV_REQ_STAGE_MST" 
   ("ID" NUMBER(10,0) NOT NULL ENABLE, 
	"SERVICE_REQUEST_TYPE" VARCHAR2(50 BYTE) NOT NULL ENABLE, 
	"STAGE_CODE" VARCHAR2(10 BYTE) NOT NULL ENABLE, 
	"STAGE_NAME" VARCHAR2(100 BYTE) NOT NULL ENABLE, 
	"STAGE_ORDER" NUMBER(10,0), 
	"STATUS" VARCHAR2(5 BYTE), 
	"CREATED_BY" VARCHAR2(200 BYTE), 
	"CREATED_ON" TIMESTAMP (6) DEFAULT sysdate, 
	"MODIFIED_BY" VARCHAR2(200 BYTE), 
	"MODIFIED_ON" TIMESTAMP (6) DEFAULT sysdate, 
	 CONSTRAINT "OA_SERV_REQ_STAGE_MST_PK" PRIMARY KEY ("ID"), 
	 CONSTRAINT "OA_SERV_REQ_STAGE_MST_UK1" UNIQUE ("SERVICE_REQUEST_TYPE", "STAGE_CODE")
   );

   COMMENT ON COLUMN  "OA_SERV_REQ_STAGE_MST"."ID" IS 'Primary Key';
   COMMENT ON COLUMN  "OA_SERV_REQ_STAGE_MST"."SERVICE_REQUEST_TYPE" IS 'Records service requst type code';
   COMMENT ON COLUMN  "OA_SERV_REQ_STAGE_MST"."STAGE_CODE" IS 'Records Stage Code';
   COMMENT ON COLUMN  "OA_SERV_REQ_STAGE_MST"."STAGE_NAME" IS 'Records Stage Name';
   COMMENT ON COLUMN  "OA_SERV_REQ_STAGE_MST"."STAGE_ORDER" IS 'Records stage order';
   COMMENT ON COLUMN  "OA_SERV_REQ_STAGE_MST"."STATUS" IS 'Records status e.g. A-Active; X-Inactive';
   COMMENT ON COLUMN  "OA_SERV_REQ_STAGE_MST"."CREATED_BY" IS 'Records Created By';
   COMMENT ON COLUMN  "OA_SERV_REQ_STAGE_MST"."CREATED_ON" IS 'Records Created Date';
   COMMENT ON COLUMN  "OA_SERV_REQ_STAGE_MST"."MODIFIED_BY" IS 'Records Modified By';
   COMMENT ON COLUMN  "OA_SERV_REQ_STAGE_MST"."MODIFIED_ON" IS 'Records Modified Date';

    GRANT SELECT,INSERT,UPDATE,DELETE ON ONEASSIST_SP.OA_SERV_REQ_STAGE_MST TO ONEASSIST_DML_SP;
CREATE OR REPLACE SYNONYM oneassist_sp_app.OA_SERV_REQ_STAGE_MST FOR ONEASSIST_SP.OA_SERV_REQ_STAGE_MST;

  CREATE TABLE "OA_SERV_REQ_STAGE_STATUS_MST" 
   (	"ID" NUMBER(10,0) NOT NULL ENABLE, 
	"SERVICE_REQUEST_TYPE" VARCHAR2(50 BYTE) NOT NULL ENABLE, 
	"STAGE_CODE" VARCHAR2(10 BYTE) NOT NULL ENABLE, 
	"STAGE_STATUS_CODE" VARCHAR2(10 BYTE), 
	"SERVICE_REQUEST_STATUS" VARCHAR2(20 BYTE) NOT NULL ENABLE, 
	"STAGE_STATUS_DISPLAY_NAME" VARCHAR2(200 BYTE), 
	"STATUS" VARCHAR2(5 BYTE), 
	"CREATED_BY" VARCHAR2(200 BYTE), 
	"CREATED_ON" TIMESTAMP (6) DEFAULT SYSDATE, 
	"MODIFIED_BY" VARCHAR2(200 BYTE), 
	"MODIFIED_ON" TIMESTAMP (6) DEFAULT SYSdaTE, 
	 CONSTRAINT "OA_SERV_REQ_STAGE_STATUS_M_PK" PRIMARY KEY ("ID"), 
	 CONSTRAINT "OA_SERV_REQ_STAGE_STATUS__UK1" UNIQUE ("SERVICE_REQUEST_TYPE", "STAGE_STATUS_CODE", "STAGE_CODE")  
   ) ;

   COMMENT ON COLUMN  "OA_SERV_REQ_STAGE_STATUS_MST"."ID" IS 'Primary Key';
   COMMENT ON COLUMN  "OA_SERV_REQ_STAGE_STATUS_MST"."SERVICE_REQUEST_TYPE" IS 'Records Service Reqeust Type';
   COMMENT ON COLUMN  "OA_SERV_REQ_STAGE_STATUS_MST"."STAGE_CODE" IS 'Records Stage Code';
   COMMENT ON COLUMN  "OA_SERV_REQ_STAGE_STATUS_MST"."STAGE_STATUS_CODE" IS 'Records Stage Status Code';
   COMMENT ON COLUMN  "OA_SERV_REQ_STAGE_STATUS_MST"."SERVICE_REQUEST_STATUS" IS 'Records Service Request Status Code; PARTNER_STAGE';
   COMMENT ON COLUMN  "OA_SERV_REQ_STAGE_STATUS_MST"."STAGE_STATUS_DISPLAY_NAME" IS 'Records Stage Status for Partner';
   COMMENT ON COLUMN  "OA_SERV_REQ_STAGE_STATUS_MST"."STATUS" IS 'Records Status';
   COMMENT ON COLUMN  "OA_SERV_REQ_STAGE_STATUS_MST"."CREATED_BY" IS 'Records Created By';
   COMMENT ON COLUMN  "OA_SERV_REQ_STAGE_STATUS_MST"."CREATED_ON" IS 'Records Created Date';
   COMMENT ON COLUMN  "OA_SERV_REQ_STAGE_STATUS_MST"."MODIFIED_BY" IS 'Records Modified By';
   COMMENT ON COLUMN  "OA_SERV_REQ_STAGE_STATUS_MST"."MODIFIED_ON" IS 'Records Modified Date';

   
    GRANT SELECT,INSERT,UPDATE,DELETE ON ONEASSIST_SP.OA_SERV_REQ_STAGE_STATUS_MST TO ONEASSIST_DML_SP;
CREATE OR REPLACE SYNONYM oneassist_sp_app.OA_SERV_REQ_STAGE_STATUS_MST FOR ONEASSIST_SP.OA_SERV_REQ_STAGE_STATUS_MST;

  CREATE TABLE  "OA_SERV_REQ_TRANSITION_CONFIG" 
   (	"ID" NUMBER(10,0) NOT NULL ENABLE, 
	"SERVICE_REQUEST_TYPE" VARCHAR2(50 BYTE) NOT NULL ENABLE, 
	"TRANSITION_FROM_STAGE" VARCHAR2(10 BYTE), 
	"EVENT_NAME" VARCHAR2(200 BYTE) NOT NULL ENABLE, 
	"TRANSITION_TO_STAGE" VARCHAR2(10 BYTE), 
	"TRANSITION_TO_STAGE_STATUS" VARCHAR2(10 BYTE), 
	"STATUS" VARCHAR2(5 BYTE), 
	"CREATED_BY" VARCHAR2(200 BYTE), 
	"CREATED_ON" TIMESTAMP (6) DEFAULT sysdate, 
	"MODIFIED_BY" VARCHAR2(200 BYTE), 
	"MODIFIED_ON" TIMESTAMP (6) DEFAULT sysdate, 
	 CONSTRAINT "OA_SERV_REQ_TRANSITION_CON_PK" PRIMARY KEY ("ID"), 
	 CONSTRAINT "OA_SERV_REQ_TRANSITION_CO_UK1" UNIQUE ("SERVICE_REQUEST_TYPE", "EVENT_NAME")
   );

   COMMENT ON COLUMN  "OA_SERV_REQ_TRANSITION_CONFIG"."ID" IS 'Primary Key';
   COMMENT ON COLUMN  "OA_SERV_REQ_TRANSITION_CONFIG"."SERVICE_REQUEST_TYPE" IS 'Records Service Request Type code';
   COMMENT ON COLUMN  "OA_SERV_REQ_TRANSITION_CONFIG"."TRANSITION_FROM_STAGE" IS 'Records Transition from Stage code';
   COMMENT ON COLUMN  "OA_SERV_REQ_TRANSITION_CONFIG"."EVENT_NAME" IS 'Records Event Name';
   COMMENT ON COLUMN  "OA_SERV_REQ_TRANSITION_CONFIG"."TRANSITION_TO_STAGE" IS 'Records Transition to Stage code; CMS_STAGE';
   COMMENT ON COLUMN  "OA_SERV_REQ_TRANSITION_CONFIG"."TRANSITION_TO_STAGE_STATUS" IS 'Records Transition to Stage Status code; CMS_STAGE_STATUS';
   COMMENT ON COLUMN  "OA_SERV_REQ_TRANSITION_CONFIG"."STATUS" IS 'Records status e.g. A-Active; X-Inactive';
   COMMENT ON COLUMN  "OA_SERV_REQ_TRANSITION_CONFIG"."CREATED_BY" IS 'Records Created By';
   COMMENT ON COLUMN  "OA_SERV_REQ_TRANSITION_CONFIG"."CREATED_ON" IS 'Records Created Date';
   COMMENT ON COLUMN  "OA_SERV_REQ_TRANSITION_CONFIG"."MODIFIED_BY" IS 'Records Modified By';
   COMMENT ON COLUMN  "OA_SERV_REQ_TRANSITION_CONFIG"."MODIFIED_ON" IS 'Records Modified Date';

    GRANT SELECT,INSERT,UPDATE,DELETE ON ONEASSIST_SP.OA_SERV_REQ_TRANSITION_CONFIG TO ONEASSIST_DML_SP;
CREATE OR REPLACE SYNONYM oneassist_sp_app.OA_SERV_REQ_TRANSITION_CONFIG FOR ONEASSIST_SP.OA_SERV_REQ_TRANSITION_CONFIG;

CREATE SEQUENCE  "SEQ_OA_SERV_REQ_STAGE_MST"  
MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE;


CREATE SEQUENCE  "SEQ_OA_SERV_REQ_TRANS_CONFIG"  
MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE;

CREATE SEQUENCE  "SEQ_OA_SERV_REQ_STG_STATUS_MST"  
MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE;

--OA_SERVICE_ADDRESS_DTL
ALTER table OA_SERVICE_ADDRESS_DTL modify(CREATED_ON default sysdate);
ALTER table OA_SERVICE_ADDRESS_DTL modify(MODIFIED_ON default sysdate);

--OA_PINCODE_SERV_FULFILMENT_MST
ALTER table OA_PINCODE_SERV_FULFILMENT_MST modify(CREATED_ON default sysdate);
ALTER table OA_PINCODE_SERV_FULFILMENT_MST modify(MODIFIED_ON default sysdate);

--OA_SERVICE_REQUEST_DTL
ALTER table OA_SERVICE_REQUEST_DTL modify(CREATED_ON default sysdate);
ALTER table OA_SERVICE_REQUEST_DTL modify(MODIFIED_ON default sysdate);

--OA_SHIPMENT_ASSET_DTL
ALTER table OA_SHIPMENT_ASSET_DTL modify(CREATED_ON default sysdate);
ALTER table OA_SHIPMENT_ASSET_DTL modify(MODIFIED_ON default sysdate);


 GRANT SELECT,INSERT,UPDATE,DELETE ON ONEASSIST_SP.SEQ_OA_SERV_REQ_STAGE_MST TO ONEASSIST_DML_SP;
CREATE OR REPLACE SYNONYM oneassist_sp_app.SEQ_OA_SERV_REQ_STAGE_MST FOR ONEASSIST_SP.SEQ_OA_SERV_REQ_STAGE_MST;

 GRANT SELECT,INSERT,UPDATE,DELETE ON ONEASSIST_SP.SEQ_OA_SERV_REQ_TRANS_CONFIG TO ONEASSIST_DML_SP;
CREATE OR REPLACE SYNONYM oneassist_sp_app.SEQ_OA_SERV_REQ_TRANS_CONFIG FOR ONEASSIST_SP.SEQ_OA_SERV_REQ_TRANS_CONFIG;

 GRANT SELECT,INSERT,UPDATE,DELETE ON ONEASSIST_SP.SEQ_OA_SERV_REQ_STG_STATUS_MST TO ONEASSIST_DML_SP;
CREATE OR REPLACE SYNONYM oneassist_sp_app.SEQ_OA_SERV_REQ_STG_STATUS_MST FOR ONEASSIST_SP.SEQ_OA_SERV_REQ_STG_STATUS_MST;
