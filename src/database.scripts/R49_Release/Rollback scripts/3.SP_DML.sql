ALTER SESSION SET CURRENT_SCHEMA=ONEASSIST_SP;
UPDATE OA_DOC_TYPE_MST set IS_MANDATORY='N' where DOC_NAME='DAMAGED_SPARE_PART';
COMMIT;