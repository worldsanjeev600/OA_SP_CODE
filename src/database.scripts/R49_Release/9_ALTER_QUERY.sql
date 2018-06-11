ALTER SESSION SET CURRENT_SCHEMA=ONEASSIST_SP;
ALTER TABLE OA_DOC_TYPE_MST MODIFY IS_MANDATORY VARCHAR(10) ;

ALTER TABLE OA_DOC_TYPE_MST  ADD  DOC_KEY VARCHAR2(200) ; 
COMMENT ON COLUMN "OA_DOC_TYPE_MST"."DOC_KEY" IS 'Document Key';

ALTER TABLE OA_SERV_REQ_STAGE_MST ADD  STAGE_OBJECT_NAME VARCHAR2(100) ;
COMMENT ON COLUMN "OA_SERV_REQ_STAGE_MST"."STAGE_OBJECT_NAME" IS 'Stage Object Name';

commit;