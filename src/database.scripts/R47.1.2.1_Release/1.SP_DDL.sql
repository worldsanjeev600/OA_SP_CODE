ALTER SESSION SET CURRENT_SCHEMA=ONEASSIST_SP;

CREATE or replace VIEW SHIPMENT_SEARCH_RESULT_VIEW
AS SELECT rownum AS ID,
shipment.shipment_id AS shipmentId,
service.SERVICE_REQUEST_TYPE_ID AS serviceRequestTypeId,
service.REF_PRIMARY_TRACKING_NO AS primaryTrackingNumber,
shipment.CURRENT_STAGE AS currentStage,
shipment.logistic_partner_code AS logisticPartnerCode,
shipment.logistic_partner_ref_track_no AS logisticPartnerRefTrackingNo,
shipment.created_on AS createdOn,
shipment.modified_on AS modifiedOn,
shipment.hub_id AS hubId,
shipment.status AS shipmentStatus,
null AS originPincode,
null AS destinationPincode,
pod.podDocTypeId AS podDocTypeId,
label.labelDocTypeId AS labelDocTypeId,
label.labelDocName AS lablDocTypeName,
pod.podDocName AS podDocTypeName,
pod.podMongoRefId AS podMongoRefId,
pod.podFileName AS podDocumentName,
label.labelMongoRefId AS labelMongoRefId,
label.labelFileName AS labelDocumentName,
shipment.BOX_ACTUAL_LENGTH AS boxActualLength,
shipment.BOX_ACTUAL_WIDTH AS boxActualWidth,
shipment.BOX_ACTUAL_HEIGHT AS boxActualHeight,
shipment.BOX_ACTUAL_WEIGHT AS boxActualWeight,
shipment.SHIPMENT_DECLARE_VALUE AS shipmentDeclareValue,
shipment.BOX_COUNT AS shipmentBoxCount,
shipment.SHIPMENT_TYPE AS shipmentType,
shipment.service_request_id AS SERVICE_REQUEST_ID,
shipment.FAILURE_REASON AS reasonForFailure,
label.labelDocId AS labelDocId,
pod.podDocId AS podDocId
FROM ONEASSIST_SP.OA_SHIPMENT_DTL shipment
INNER JOIN ONEASSIST_SP.OA_SERVICE_REQUEST_DTL service
ON shipment.SERVICE_REQUEST_ID = service.SERVICE_REQUEST_ID
LEFT JOIN
(SELECT dtl.doc_type_id AS podDocTypeId,
typemst.doc_name AS podDocName,
dtl.file_name AS podFileName,
dtl.service_request_id AS podShipmentId,
dtl.STORAGE_REF_ID AS podMongoRefId,
dtl.DOC_ID as podDocId
FROM OA_SERVICE_DOC_DTL dtl
INNER JOIN oa_doc_type_mst typemst
ON dtl.doc_type_id = typemst.doc_type_id
AND typemst.doc_name ='SHIPMENT_PROOF_OF_DELIVERY'
AND dtl.status ='A'
)pod ON shipment.service_request_id = pod.podShipmentId and pod.podFileName LIKE CONCAT(CONCAT('%',shipment.logistic_partner_ref_track_no), '%')
LEFT OUTER JOIN
(SELECT dtl.doc_type_id AS labelDocTypeId,
typemst.doc_name AS labelDocName,
dtl.file_name AS labelFileName,
dtl.service_request_id AS labelShipmentId,
dtl.STORAGE_REF_ID AS labelMongoRefId,
dtl.DOC_ID as labelDocId
FROM OA_SERVICE_DOC_DTL dtl
INNER JOIN oa_doc_type_mst typemst
ON dtl.doc_type_id = typemst.doc_type_id
AND typemst.doc_name ='SHIPMENT_LABEL'
AND dtl.status ='A'
)label ON shipment.service_request_id = label.labelShipmentId and label.labelFileName LIKE CONCAT(CONCAT('%',shipment.logistic_partner_ref_track_no), '%');

CREATE INDEX SR_TYPE_ID_INDEX ON OA_PINCODE_SERV_FULFILMENT_MST(SERVICE_REQUEST_TYPE_ID);

ALTER TABLE OA_SERVICE_DOC_DTL ADD CONSTRAINT PK_DOC_DTL PRIMARY KEY (DOC_ID);
CREATE INDEX DOC_SR_TYPE_ID_INDEX ON OA_SERVICE_DOC_DTL(SERVICE_REQUEST_ID);


