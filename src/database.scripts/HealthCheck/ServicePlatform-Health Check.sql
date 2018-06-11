ALTER SESSION SET CURRENT_SCHEMA=oneassist_sp;
--To identify the non-ascii chars in the HUB address
SELECT REGEXP_REPLACE(chm.hub_address,'[^ -~]',' [~XX~] ') hub_address_withnon_ascii, chm.* 
FROM oa_claim_hub_mst chm
WHERE chm.hub_address != asciistr(chm.hub_address);

--To identify the non-ascii chars in the shipments addresses
SELECT REGEXP_REPLACE(addr.address_line_1, '[^ -~]',' [~XX~] ') addressline1_with_non_ascii, addr.* 
FROM oneassist_sp.oa_service_address_dtl addr
WHERE addr.address_line_1 != asciistr(addr.address_line_1) and addr.created_on >= sysdate-1;

--Find failure case of yesterday
SELECT
  DECODE (sd.LOGISTIC_PARTNER_CODE, '233', 'ECOM', '245', 'FEDEX', '276', 'LOGINEXT', '201', 'Plada','308','DHL', sd.LOGISTIC_PARTNER_CODE) LOGISTIC_PARTNER,
  sd.FAILURE_REASON,
  COUNT(*)
FROM 
  oa_shipment_dtl sd
  LEFT  JOIN oa_service_doc_dtl sdd on sd.service_request_id = sdd.SERVICE_REQUEST_ID
WHERE
  --sd.created_on >= to_date('20-NOV-2017', 'DD-MON-YYYY')
  sd.created_on >= sysdate-1
  AND sd.status = 'P'
  AND sd.LOGISTIC_PARTNER_CODE IN (233,245,276,308)
  AND sd.FAILURE_REASON IS NOT NULL
GROUP BY sd.LOGISTIC_PARTNER_CODE, sd.FAILURE_REASON
ORDER BY sd.LOGISTIC_PARTNER_CODE, sd.FAILURE_REASON;


--AWB not generated
select 
count(*)
from 
  oa_shipment_dtl sd
  --INNER JOIN oa_service_request_dtl sr ON sd.service_request_id = sr.service_request_id
  --LEFT  JOIN oa_service_doc_dtl sdd on sd.service_request_id = sdd.SERVICE_REQUEST_ID
where 
 sd.created_on >= sysdate-1
--  sd.created_on >= to_date('16-NOV-2017', 'DD-MON-YYYY')
  and sd.status ='P'
  and sd.LOGISTIC_PARTNER_REF_TRACK_NO is null 
  and sd.LOGISTIC_PARTNER_CODE in (233,245,308)
  --AND sdd.FILE_NAME is null
order by sd.created_on desc;
	
-- list of logistic partners	

select distinct logistic_partner_code from oa_shipment_dtl sd;

--select * from oa_service_doc_dtl
--where service_request_id in ()

--where 
--created_on > to_date('17-NOV-2017', 'DD-MON-YYYY');
--.created_on >= sysdate-1;


-- Getting list of Failed shipments (because of validation)
SELECT
  sd.FAILURE_REASON,
  COUNT(*)
FROM 
  oa_shipment_dtl sd
WHERE
  sd.created_on >= sysdate-1
  AND sd.status = 'F'
GROUP BY sd.FAILURE_REASON
ORDER BY sd.FAILURE_REASON;


--- Not serviceable SRs

SELECT
  sr.SERVICE_REQUEST_ID
FROM 
  oa_service_request_dtl sr
WHERE
  sr.created_on >= sysdate-1
  AND sr.status = 'P' 
  AND SR.SERVICE_PARTNER_CODE IS NULL
  AND SR.SERVICE_PARTNER_BU_CODE IS NULL;

  
--- DOCUMENTS NOT INSERTED/UPDATED IN MONGO

select * 
from 
	OA_SERVICE_DOC_DTL 
where 
	STORAGE_REF_ID IS NULL
	and created_on >= sysdate-1;
	
	
---- WORKFLOW DATA NULL 
SELECT SERVICE_REQUEST_ID
FROM 
oa_service_request_dtl
WHERE 
(created_on >= sysdate-1
OR MODIFIED_on >= sysdate-1)
AND WORKFLOW_DATA IS NULL
 AND SERVICE_REQUEST_TYPE_ID NOT IN (1,2,3,4,5,6);

-- TO CHECK MULTIPLE PARTNERS HAS BEEN ASSIGNED TO SAME PINCODE WITH SAME PRIORITY IN FULFILMENT MASTER
SELECT SERVICE_REQUEST_TYPE_ID,PINCODE,SUBCATEGORY_CODE, PARTNER_PRIORITY FROM OA_PINCODE_SERV_FULFILMENT_MST 
WHERE STATUS='A' AND PARTNER_PRIORITY IS NOT NULL
GROUP BY SERVICE_REQUEST_TYPE_ID,PINCODE,SUBCATEGORY_CODE, PARTNER_PRIORITY
HAVING COUNT(*) > 1 ORDER BY SERVICE_REQUEST_TYPE_ID,PINCODE,SUBCATEGORY_CODE, PARTNER_PRIORITY;


ALTER SESSION SET CURRENT_SCHEMA=oneassist;
 --INSPECTION FAILED REASON
 
 SELECT c.FAIL_REASON,COUNT(*)
 FROM
	OA_CUSTOMER_TMP c
WHERE c.TASK_NAME = 'INSPECTION'
AND c.created_date >= sysdate-1
AND c.FAIL_REASON IS NOT NULL
GROUP BY c.FAIL_REASON
ORDER BY c.FAIL_REASON;