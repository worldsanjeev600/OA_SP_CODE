CREATE SEQUENCE ONEASSIST.OA_COMM_EVENT_MST_SEQ START WITH 2100 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE ONEASSIST.OA_COMM_TEMPLATE_SEQ START WITH 2100 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE ONEASSIST.OA_COMM_CONFIG_DTL_SEQ START WITH 2100 INCREMENT BY 1 NOCACHE NOCYCLE;

insert into ONEASSIST.OA_COMM_EVENT_MST(COMM_EVENT_ID,EVENT_CODE,EVENT_DESC,MODULE_NAME,PRIORITY,STATUS,CREATED_BY,CREATED_DATE,MODIFIED_DATE)
values(OA_COMM_EVENT_MST_SEQ.nextval,'SP_VERIFICATION_SUCCESSFUL','SP_VERIFICATION_SUCCESSFUL','Service Platform','M','A','SCRIPT',SYSDATE,SYSDATE);

insert into ONEASSIST.OA_COMM_TEMPLATE(COMM_TEMP_ID,COMM_MODE,TEMPLATE_NAME,SUBJECT,CREATED_BY,CREATED_DATE,MODIFIED_DATE,SMS_TEMPLATE_TEXT,COMM_STATUS)
values (OA_COMM_TEMPLATE_SEQ.nextval,'sms','SP_VERIFICATION_SUCCESSFUL','SP_VERIFICATION_SUCCESSFUL','SCRIPT',SYSDATE,SYSDATE,'Dear $!model.addresseeFullName, Your service request $!model.refPrimaryTrackingNo has been confirmed for $dateUtil.format(''dd-MM-yyyy HH:mm'',$dateUtil.toDate(''dd-MMM-yyyy HH:mm:ss'', $!model.scheduleSlotStartDateTime)). We shall get back to you with the Expert details shortly. Please share OTP to start/end repair to Expert. STARTOTP : $!model.workflowData.visit.serviceStartCode and END OTP : $!model.workflowData.repair.serviceEndCode -  Team OneAssist
','A');

insert into ONEASSIST.OA_COMM_CONFIG_DTL(COMM_CONFIG_ID,COMM_EVENT_ID,SMS_TEMPLATE_ID,STATUS,CREATED_BY,CREATED_DATE,MODIFIED_DATE)
values(OA_COMM_CONFIG_DTL_SEQ.nextval, (select COMM_EVENT_ID from OA_COMM_EVENT_MST where EVENT_CODE='SP_VERIFICATION_SUCCESSFUL'),(select COMM_TEMP_ID from OA_COMM_TEMPLATE where TEMPLATE_NAME='SP_VERIFICATION_SUCCESSFUL'),'A','SCRIPT',SYSDATE,SYSDATE);

insert into ONEASSIST.OA_COMM_EVENT_MST(COMM_EVENT_ID,EVENT_CODE,EVENT_DESC,MODULE_NAME,PRIORITY,STATUS,CREATED_BY,CREATED_DATE,MODIFIED_DATE)
values(OA_COMM_EVENT_MST_SEQ.nextval,'SP_COMPLETE_DOCUMENT_UPLOAD','SP_COMPLETE_DOCUMENT_UPLOAD','Service Platform','M','A','SCRIPT',SYSDATE,SYSDATE);

insert into ONEASSIST.OA_COMM_TEMPLATE(COMM_TEMP_ID,COMM_MODE,TEMPLATE_NAME,SUBJECT,CREATED_BY,CREATED_DATE,MODIFIED_DATE,SMS_TEMPLATE_TEXT,COMM_STATUS)
values (OA_COMM_TEMPLATE_SEQ.nextval,'sms','SP_COMPLETE_DOCUMENT_UPLOAD','SP_COMPLETE_DOCUMENT_UPLOAD','SCRIPT',SYSDATE,SYSDATE,'Dear $!model.addresseeFullName, Your request has been received. Uploaded documents are under verification. We shall get back to you with the request details shortly. -  Team OneAssist','A');

insert into ONEASSIST.OA_COMM_CONFIG_DTL(COMM_CONFIG_ID,COMM_EVENT_ID,SMS_TEMPLATE_ID,STATUS,CREATED_BY,CREATED_DATE,MODIFIED_DATE)
values(OA_COMM_CONFIG_DTL_SEQ.nextval, (select COMM_EVENT_ID from OA_COMM_EVENT_MST where EVENT_CODE='SP_COMPLETE_DOCUMENT_UPLOAD'),(select COMM_TEMP_ID from OA_COMM_TEMPLATE where TEMPLATE_NAME='SP_COMPLETE_DOCUMENT_UPLOAD'),'A','SCRIPT',SYSDATE,SYSDATE);

update ONEASSIST.OA_COMM_TEMPLATE SET SMS_TEMPLATE_TEXT = 'Dear $!model.addresseeFullName, SR $!model.refPrimaryTrackingNo has been re-scheduled by you for $dateUtil.toDate(''dd-MMM-yyyy HH:mm:ss'', $!model.scheduleSlotStartDateTime)). - Team OneAssist'
WHERE COMM_TEMP_ID = 
(SELECT SMS_TEMPLATE_ID FROM OA_COMM_CONFIG_DTL WHERE COMM_EVENT_ID = 
(SELECT COMM_EVENT_ID FROM OA_COMM_EVENT_MST WHERE EVENT_CODE = 'SP_RESCHEDULED_POST_TECH_ALLOCATION'));	

insert into ONEASSIST.OA_COMM_EVENT_MST(COMM_EVENT_ID,EVENT_CODE,EVENT_DESC,MODULE_NAME,PRIORITY,STATUS,CREATED_BY,CREATED_DATE,MODIFIED_DATE)
values(OA_COMM_EVENT_MST_SEQ.nextval,'SP_IC_APPROVAL_WAITING','SP_IC_APPROVAL_WAITING','Service Platform','M','A','SCRIPT',SYSDATE,SYSDATE);

insert into ONEASSIST.OA_COMM_TEMPLATE(COMM_TEMP_ID,COMM_MODE,TEMPLATE_NAME,SUBJECT,CREATED_BY,CREATED_DATE,MODIFIED_DATE,SMS_TEMPLATE_TEXT,COMM_STATUS)
values (OA_COMM_TEMPLATE_SEQ.nextval,'sms','SP_IC_APPROVAL_WAITING','SP_IC_APPROVAL_WAITING ','SCRIPT',SYSDATE,SYSDATE,'Dear $!model.addresseeFullName, Your request is in process. We shall get back to you with the details shortly .Plan TnC apply - Team OneAssist','A');

insert into ONEASSIST.OA_COMM_CONFIG_DTL(COMM_CONFIG_ID,COMM_EVENT_ID,SMS_TEMPLATE_ID,STATUS,CREATED_BY,CREATED_DATE,MODIFIED_DATE)
values(OA_COMM_CONFIG_DTL_SEQ.nextval,(select COMM_EVENT_ID from OA_COMM_EVENT_MST where EVENT_CODE='SP_IC_APPROVAL_WAITING'),(select COMM_TEMP_ID from OA_COMM_TEMPLATE where TEMPLATE_NAME='SP_IC_APPROVAL_WAITING'),'A','SCRIPT',SYSDATE,SYSDATE);

insert into ONEASSIST.OA_COMM_EVENT_MST(COMM_EVENT_ID,EVENT_CODE,EVENT_DESC,MODULE_NAME,PRIORITY,STATUS,CREATED_BY,CREATED_DATE,MODIFIED_DATE)
values(OA_COMM_EVENT_MST_SEQ.nextval,'SP_IC_DECISION_APPROVED','SP_IC_DECISION_APPROVED','Service Platform','M','A','SCRIPT',SYSDATE,SYSDATE);

insert into ONEASSIST.OA_COMM_TEMPLATE(COMM_TEMP_ID,COMM_MODE,TEMPLATE_NAME,SUBJECT,CREATED_BY,CREATED_DATE,MODIFIED_DATE,SMS_TEMPLATE_TEXT,COMM_STATUS)
values (OA_COMM_TEMPLATE_SEQ.nextval,'sms','SP_IC_DECISION_APPROVED','SP_IC_DECISION_APPROVED','SCRIPT',SYSDATE,SYSDATE,'Dear $!model.addresseeFullName, Your service request has been approved. Kindly logon to our OneAssist APP to reschedule your request or call us on 1800 123 3330 - Team OneAssist','A');

insert into ONEASSIST.OA_COMM_CONFIG_DTL(COMM_CONFIG_ID,COMM_EVENT_ID,SMS_TEMPLATE_ID,STATUS,CREATED_BY,CREATED_DATE,MODIFIED_DATE)
values(OA_COMM_CONFIG_DTL_SEQ.nextval,(select COMM_EVENT_ID from OA_COMM_EVENT_MST where EVENT_CODE='SP_IC_DECISION_APPROVED'),(select COMM_TEMP_ID from OA_COMM_TEMPLATE where TEMPLATE_NAME='SP_IC_DECISION_APPROVED'),'A','SCRIPT',SYSDATE,SYSDATE);

update ONEASSIST.OA_COMM_TEMPLATE SET SMS_TEMPLATE_TEXT = 'Dear $!model.addresseeFullName, Our Expert visited as per appointment but could not service you due to $!model.workflowData.visit.serviceCancelReason. Kindly logon to our OneAssist APP to reschedule your request or call us on 1800 123 3330 - Team OneAssist'
WHERE COMM_TEMP_ID = 
(SELECT SMS_TEMPLATE_ID FROM OA_COMM_CONFIG_DTL WHERE COMM_EVENT_ID = 
(SELECT COMM_EVENT_ID FROM OA_COMM_EVENT_MST WHERE EVENT_CODE = 'SP_TECH_UNABLE_TO_SERVICE'));


update ONEASSIST.OA_COMM_TEMPLATE SET SMS_TEMPLATE_TEXT = 'Dear $!model.addresseeFullName, Product inspected by Expert against SR $!model.refPrimaryTrackingNo , Your Amount payable is INR $!model.balanceCostToCusotmer.- Team OneAssist'
WHERE COMM_TEMP_ID = 
(SELECT SMS_TEMPLATE_ID FROM OA_COMM_CONFIG_DTL WHERE COMM_EVENT_ID = 
(SELECT COMM_EVENT_ID FROM OA_COMM_EVENT_MST WHERE EVENT_CODE = 'SP_RAPAIR_ASSESSMENT_COMPLETED'));


update ONEASSIST.OA_COMM_TEMPLATE SET SMS_TEMPLATE_TEXT = 'Dear $!model.addresseeFullName, Sorry to inform that estimate for SR $!model.refPrimaryTrackingNo has been rejected. Please refer email for the details or call us on 1800 123 3330-Team OneAssist'
WHERE COMM_TEMP_ID = 
(SELECT SMS_TEMPLATE_ID FROM OA_COMM_CONFIG_DTL WHERE COMM_EVENT_ID = 
(SELECT COMM_EVENT_ID FROM OA_COMM_EVENT_MST WHERE EVENT_CODE = 'SP_IC_DECISION_REJECTED'));



update ONEASSIST.OA_COMM_TEMPLATE SET SMS_TEMPLATE_TEXT = 'Dear $!model.addresseeFullName, We thank you for the opportunity to serve you. Your SR $!model.refPrimaryTrackingNo has been completed successfully, Please rate us on our OneAssist App as your feedback is important to us - Team OneAssist'
WHERE COMM_TEMP_ID = 
(SELECT SMS_TEMPLATE_ID FROM OA_COMM_CONFIG_DTL WHERE COMM_EVENT_ID = 
(SELECT COMM_EVENT_ID FROM OA_COMM_EVENT_MST WHERE EVENT_CODE = 'SP_REPAIR_SUCCESSFUL'));


update ONEASSIST.OA_COMM_TEMPLATE SET SMS_TEMPLATE_TEXT = 'Dear $!model.addresseeFullName,You have asked for REFUND against SR $!model.refPrimaryTrackingNo.  We are processing the same and shall get back to you with the details shortly .Plan TnC apply - Team OneAssist'
WHERE COMM_TEMP_ID = 
(SELECT SMS_TEMPLATE_ID FROM OA_COMM_CONFIG_DTL WHERE COMM_EVENT_ID = 
(SELECT COMM_EVENT_ID FROM OA_COMM_EVENT_MST WHERE EVENT_CODE = 'SP_CUSTOMER_ASKS_REFUND'));


update ONEASSIST.OA_COMM_TEMPLATE SET SMS_TEMPLATE_TEXT = 'Dear $!model.addresseeFullName,Your product should be picked up for further repair at service center. We shall do our best to complete as early as possible. Team OneAssist'
WHERE COMM_TEMP_ID = 
(SELECT SMS_TEMPLATE_ID FROM OA_COMM_CONFIG_DTL WHERE COMM_EVENT_ID = 
(SELECT COMM_EVENT_ID FROM OA_COMM_EVENT_MST WHERE EVENT_CODE = 'SP_TRANSPORTATION_REQUIRED'));