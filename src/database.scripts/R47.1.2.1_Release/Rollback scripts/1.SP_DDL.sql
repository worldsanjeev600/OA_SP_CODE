ALTER SESSION SET CURRENT_SCHEMA=ONEASSIST_SP;

--------------------------------------------------------
--  File created - Friday-December-22-2017   
--------------------------------------------------------
-- Unable to render VIEW DDL for object ONEASSIST_SP.SHIPMENT_SEARCH_RESULT_VIEW with DBMS_METADATA attempting internal generator.
CREATE OR REPLACE VIEW ONEASSIST_SP.SHIPMENT_SEARCH_RESULT_VIEW
AS SELECT rownum AS ID,
shipment.shipment_id AS shipmentId,
serviceType.SERVICE_REQUEST_TYPE_ID AS serviceRequestTypeId,
service.REF_PRIMARY_TRACKING_NO AS primaryTrackingNumber,
shipment.CURRENT_STAGE AS currentStage,
shipment.logistic_partner_code AS logisticPartnerCode,
shipment.logistic_partner_ref_track_no AS logisticPartnerRefTrackingNo,
shipment.created_on AS createdOn,
shipment.modified_on AS modifiedOn,
shipment.hub_id AS hubId,
shipment.status AS shipmentStatus,
servicetype.service_request_type AS serviceName,
originAddress.PINCODE AS originPincode,
destinationAddress.pincode AS destinationPincode,
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
asset.ASSET_PIECE_COUNT AS assetPieceCount,
asset.ASSET_CATEGORY_NAME AS assetCategoryName,
originAddress.SERVICE_ADDRESS_ID AS originalAddressId,
originAddress.ADDRESS_LINE_1 AS originAddressLine1,
originAddress.ADDRESS_LINE_2 AS originAddressLine2,
originAddress.LANDMARK AS originLandmark,
originAddress.DISTRICT AS originDistrict,
originAddress.MOBILE_NO AS originMobileNO,
originAddress.COUNTRY_CODE AS originCountryCode,
originAddress.EMAIL AS originAddressEmail,
originAddress.ADDRESSEE_FULL_NAME AS originAddresseeFullName,
destinationAddress.SERVICE_ADDRESS_ID AS destinationAddressId,
destinationAddress.ADDRESS_LINE_1 AS destinationAddressLine1,
destinationAddress.ADDRESS_LINE_2 AS destinationAddressLine2,
destinationAddress.LANDMARK AS destinationLandmark,
destinationAddress.DISTRICT AS destinationDistrict,
destinationAddress.MOBILE_NO AS destinationMobileNO,
destinationAddress.COUNTRY_CODE AS destinationCountryCode,
destinationAddress.EMAIL AS destinationAddressEmail,
destinationAddress.ADDRESSEE_FULL_NAME AS destinationAddresseeFullName,
shipment.BOX_COUNT AS shipmentBoxCount,
shipment.SHIPMENT_TYPE AS shipmentType,
shipment.service_request_id AS SERVICE_REQUEST_ID,
shipment.FAILURE_REASON AS reasonForFailure,
asset.ASSET_MODEL_NAME AS ASSET_MODEL_NAME,
asset.ASSET_MAKE_NAME AS ASSET_MAKE_NAME,
label.labelDocId AS labelDocId,
pod.podDocId AS podDocId
FROM ONEASSIST_SP.OA_SHIPMENT_DTL shipment
INNER JOIN ONEASSIST_SP.OA_SERVICE_ADDRESS_DTL originAddress
ON shipment.origin_addrees_id = originAddress.service_address_id
INNER JOIN ONEASSIST_SP.OA_SERVICE_ADDRESS_DTL destinationAddress
ON shipment.DESTINATION_ADDREES_ID = destinationAddress.service_address_id
LEFT JOIN ONEASSIST_SP.OA_SHIPMENT_ASSET_DTL asset
ON shipment.shipment_id = asset.shipment_id
INNER JOIN ONEASSIST_SP.OA_SERVICE_REQUEST_DTL service
ON shipment.SERVICE_REQUEST_ID = service.SERVICE_REQUEST_ID
INNER JOIN ONEASSIST_SP.OA_SERVICE_REQUEST_TYPE_MST serviceType
ON service.SERVICE_REQUEST_TYPE_ID = serviceType.SERVICE_REQUEST_TYPE_ID
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
)label ON shipment.service_request_id = label.labelShipmentId and label.labelFileName LIKE CONCAT(CONCAT('%',shipment.logistic_partner_ref_track_no), '%')
ORDER BY shipment.shipment_id DESC;

