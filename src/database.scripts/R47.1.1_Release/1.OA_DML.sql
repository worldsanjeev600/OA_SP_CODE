alter session set current_schema=oneassist;
insert into oa_claim_hub_mst(HUB_ID_PK,HUB_NAME,HUB_ADDRESS,HUB_CITY,HUB_STATE,HUB_REGION,
HUB_EMAIL,HUB_MOBILE,HUB_CREATED_DATE,HUB_CREATED_BY,HUB_PINCODE) values(21,'XIAOMI NORTH','Yaantra Building , 33/33A - Rama Road ,Near Grand dreams Banquet,kirti nagar',
'NDLW','DL','NORTH','vikas.lakhar@yaantra.com',9868875807,sysdate,'Script',110015);

insert into oa_claim_hub_mst(HUB_ID_PK,HUB_NAME,HUB_ADDRESS,HUB_CITY,HUB_STATE,HUB_REGION,
HUB_EMAIL,HUB_MOBILE,HUB_CREATED_DATE,HUB_CREATED_BY,HUB_PINCODE) values(22,'XIAOMI WEST','ITTECHIES SERVICES PVT. LTD,11/84, MOTILAL NAGAR NO 3,SHREE GAJANAN CHS LTD, M. G. ROAD,GOREGAON (W)',
'MCNW','MH','WEST','sangita.patel@ittechies.co.in',9967062323,sysdate,'Script',400104);



INSERT INTO OA_PARTNER_BU_MST (PARTNER_BU_CODE,PARTNER_CODE,BU_CODE,BU_NAME,CORPORATE_EMAILID,
CORPORATE_ALT_EMAILID,CORPORATE_LL_NO,CORPORATE_MOBILE_NO,CORPORATE_FAX_NO,STATUS,AUTH_BY,AUTH_DATE,CREATED_BY,CREATED_DATE,
MODIFIED_BY,MODIFIED_DATE,LINE1,LINE2,LANDMARK,STATE,CITY,PINCODE,INDUSTRY,BUSINESS_UNIT_TYPE) values (PARTNERBU_SEQ.NEXTVAL,
(SELECT PARTNER_CODE FROM OA_PARTNER_MST where partner_type=1 and partner_name='SERVICE CENTER' and business_partner_type='SERVICE_CENTER'),null,'Gadgetwood - Delhi',
'vikas.lakhar@yaantra.com',null,null,9868875807,null,'A',null,null,'Script',
sysdate,null,null,'Yaantra Building , 33/33A - Rama Road ,Near Grand dreams Banquet,kirti nagar',null,null,NULL,'NDLW',
null,null,'SERVICE_CENTER');


INSERT INTO OA_PARTNER_BU_MST (PARTNER_BU_CODE,PARTNER_CODE,BU_CODE,BU_NAME,CORPORATE_EMAILID,
CORPORATE_ALT_EMAILID,CORPORATE_LL_NO,CORPORATE_MOBILE_NO,CORPORATE_FAX_NO,STATUS,AUTH_BY,AUTH_DATE,CREATED_BY,CREATED_DATE,
MODIFIED_BY,MODIFIED_DATE,LINE1,LINE2,LANDMARK,STATE,CITY,PINCODE,INDUSTRY,BUSINESS_UNIT_TYPE) values (PARTNERBU_SEQ.NEXTVAL,
(SELECT PARTNER_CODE FROM OA_PARTNER_MST where partner_type=1 and partner_name='SERVICE CENTER' and business_partner_type='SERVICE_CENTER'),null,'ITTechies - Mumbai',
'sangita.patel@ittechies.co.in',null,null,9967062323,null,'A',null,null,'Script',
sysdate,null,null,'ITTECHIES SERVICES PVT. LTD,11/84, MOTILAL NAGAR NO 3,SHREE GAJANAN CHS LTD, M. G. ROAD,GOREGAON (W)',null,null,NULL,'NDLW',
null,null,'SERVICE_CENTER');

INSERT INTO OA_PARTNER_BU_HUB_RLN (PARTNER_BU_CODE, HUB_ID, SRV_CNTR_BRAND_NAME)
values ((select PARTNER_BU_CODE from OA_PARTNER_BU_MST where BU_NAME = 'Gadgetwood - Delhi' and CORPORATE_EMAILID ='vikas.lakhar@yaantra.com'),
21,'XIAOMI');


INSERT INTO OA_PARTNER_BU_HUB_RLN (PARTNER_BU_CODE, HUB_ID, SRV_CNTR_BRAND_NAME)
values ((select PARTNER_BU_CODE from OA_PARTNER_BU_MST where BU_NAME = 'ITTechies - Mumbai' and CORPORATE_EMAILID ='sangita.patel@ittechies.co.in'),
22,'XIAOMI');


commit;