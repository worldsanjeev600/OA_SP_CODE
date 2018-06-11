ALTER SESSION SET CURRENT_SCHEMA=ONEASSIST_SP;
insert into OA_DOC_TYPE_MST values(SEQ_OA_DOC_TYPE_MST.nextval,'PURCHASE_INVOICE', 'N','Purchase Invoice', 'A',SYSDATE,null,'oneassist',null,null);
insert into OA_DOC_TYPE_MST values(SEQ_OA_DOC_TYPE_MST.nextval,'ADDITIONAL_DOCUMENT1', 'N','Additional Document1', 'A',SYSDATE,null,'oneassist',null,null);
insert into OA_DOC_TYPE_MST values(SEQ_OA_DOC_TYPE_MST.nextval,'DEC_FORM', 'N','Declaration Form', 'A',SYSDATE,null,'oneassist',null,null);
insert into OA_DOC_TYPE_MST values(SEQ_OA_DOC_TYPE_MST.nextval,'SIM_BAR', 'N','Application for Barring Sim Card', 'A',SYSDATE,null,'oneassist',null,null);
insert into OA_DOC_TYPE_MST values(SEQ_OA_DOC_TYPE_MST.nextval,'CHEQUE_COPY', 'N','Cancelled cheque', 'A',SYSDATE,null,'oneassist',null,null);
insert into OA_DOC_TYPE_MST values(SEQ_OA_DOC_TYPE_MST.nextval,'LOSS_FIR', 'N','Copy of FIR or duly acknowledged police intimation', 'A',SYSDATE,null,'oneassist',null,null);
insert into OA_DOC_TYPE_MST values(SEQ_OA_DOC_TYPE_MST.nextval,'DISCHARGE_VOUCHER', 'N','Discharge voucher from the logistic partner', 'A',SYSDATE,null,'oneassist',null,null);
insert into OA_DOC_TYPE_MST values(SEQ_OA_DOC_TYPE_MST.nextval,'DULY_SIGNED_CLAIM_FORM', 'N','Signed claim form from the customer', 'A',SYSDATE,null,'oneassist',null,null);
insert into OA_DOC_TYPE_MST values(SEQ_OA_DOC_TYPE_MST.nextval,'REPLACEMENT_LETTER', 'N','Replacement Letter', 'A',SYSDATE,null,'oneassist',null,null);
insert into OA_DOC_TYPE_MST values(SEQ_OA_DOC_TYPE_MST.nextval,'ADDITIONAL_DOCUMENT2', 'N','Additional Document2', 'A',SYSDATE,null,'oneassist',null,null);
insert into OA_DOC_TYPE_MST values(SEQ_OA_DOC_TYPE_MST.nextval,'ID_PROOF', 'N','Government-issued ID Proof', 'A',SYSDATE,null,'oneassist',null,null);
insert into OA_DOC_TYPE_MST values(SEQ_OA_DOC_TYPE_MST.nextval,'BACKPANEL_IMAGE', 'N','Image of device back panel,Image of right side of the laptop', 'A',SYSDATE,null,'oneassist',null,null);
insert into OA_DOC_TYPE_MST values(SEQ_OA_DOC_TYPE_MST.nextval,'SCREEN_IMAGE', 'N','Image of damaged part of device,Image of the front of the laptop with an open lid showing the keyboard and the screen,Image of the front of the wearable', 'A',SYSDATE,null,'oneassist',null,null);
insert into OA_DOC_TYPE_MST values(SEQ_OA_DOC_TYPE_MST.nextval,'IMEI_IMAGE', 'N','Image of the device with IMEI number displayed on the screen, device back image clearly showing the serial number of the laptop, device image with serial number displayed on the screen', 'A',SYSDATE,null,'oneassist',null,null);
insert into OA_DOC_TYPE_MST values(SEQ_OA_DOC_TYPE_MST.nextval,'SIM_TRAY_IMAGE', 'N','Image of the sim tray or the device after removing the battery,Image of left side of the laptop', 'A',SYSDATE,null,'oneassist',null,null);
insert into OA_DOC_TYPE_MST values(SEQ_OA_DOC_TYPE_MST.nextval,'ESTIMATE_DOCUMENT', 'N','Repair estimate from ASC', 'A',SYSDATE,null,'oneassist',null,null);
insert into OA_DOC_TYPE_MST values(SEQ_OA_DOC_TYPE_MST.nextval,'DEVICE_REPLACEMENT_LETTER', 'N','Device Replacement Letter', 'A',SYSDATE,null,'oneassist',null,null);
insert into OA_DOC_TYPE_MST values(SEQ_OA_DOC_TYPE_MST.nextval,'REPAIR_INVOICE', 'N','Repair Invoice Letter', 'A',SYSDATE,null,'oneassist',null,null);


insert into OA_DOC_TYPE_MST ( DOC_TYPE_ID,  DOC_NAME, IS_MANDATORY, DESCRIPTION, STATUS,CREATED_ON,CREATED_BY) values( SEQ_OA_DOC_TYPE_MST.nextval, 'DAMAGED_PART1','Y','Damage Part 1','A',sysdate,'oneassist');
insert into OA_DOC_TYPE_MST ( DOC_TYPE_ID,  DOC_NAME, IS_MANDATORY, DESCRIPTION, STATUS,CREATED_ON,CREATED_BY) values( SEQ_OA_DOC_TYPE_MST.nextval, 'DAMAGED_PART2','Y','Damage Part 2','A',sysdate,'oneassist');

UPDATE OA_DOC_TYPE_MST SET DOC_NAME = DOC_KEY ;
commit;