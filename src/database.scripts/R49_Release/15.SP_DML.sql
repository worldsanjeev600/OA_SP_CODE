insert into ONEASSIST_SP.OA_GENERIC_KEYSET_MST(KEYSET_ID,KEYSET_NAME,KEYSET_DESCRIPTION,STATUS,CREATED_ON,CREATED_BY) 
values (SEQ_OA_GENERIC_KEYSET_MST.nextval,'PE_DEVICE_CONDITION','Question on device condition','A',SYSDATE,'SCRIPT');

insert into ONEASSIST_SP.OA_GENERIC_KEYSET_VALUE_DTL(VALUE_ID,KEYSET_ID,KEY,VALUE,STATUS,CREATED_ON,CREATED_BY)
values(SEQ_OA_GENERIC_KEYSET_VALUE.nextval,(select KEYSET_ID from OA_GENERIC_KEYSET_MST where KEYSET_NAME='PE_DEVICE_CONDITION'),'damageIsSwitchedOn','Are you able to switch on your device?','A',SYSDATE,'SCRIPT');

insert into ONEASSIST_SP.OA_GENERIC_KEYSET_VALUE_DTL(VALUE_ID,KEYSET_ID,KEY,VALUE,STATUS,CREATED_ON,CREATED_BY)
values(SEQ_OA_GENERIC_KEYSET_VALUE.nextval,(select KEYSET_ID from OA_GENERIC_KEYSET_MST where KEYSET_NAME='PE_DEVICE_CONDITION'),'touchScreenStatus','Is you device''s touchscreen working?','A',SYSDATE,'SCRIPT');

commit;