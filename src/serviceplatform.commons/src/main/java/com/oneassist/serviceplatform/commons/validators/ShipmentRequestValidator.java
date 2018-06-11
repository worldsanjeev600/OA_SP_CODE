package com.oneassist.serviceplatform.commons.validators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.oneassist.serviceplatform.commons.constants.Constants;
import com.oneassist.serviceplatform.commons.enums.GenericResponseCodes;
import com.oneassist.serviceplatform.commons.enums.ShipmentAddressee;
import com.oneassist.serviceplatform.commons.enums.ShipmentType;
import com.oneassist.serviceplatform.commons.exception.InputValidationException;
import com.oneassist.serviceplatform.contracts.dtos.ErrorInfoDto;
import com.oneassist.serviceplatform.contracts.dtos.shipment.DashBoardRequestDto;
import com.oneassist.serviceplatform.contracts.dtos.shipment.ShipmentAssetDetailsDto;
import com.oneassist.serviceplatform.contracts.dtos.shipment.ShipmentReassignRequestDto;
import com.oneassist.serviceplatform.contracts.dtos.shipment.ShipmentRequestDto;
import com.oneassist.serviceplatform.contracts.dtos.shipment.ShipmentSearchRequestDto;
import com.oneassist.serviceplatform.contracts.dtos.shipment.ShipmentUpdateRequestDto;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * @author Satish.Kumar This class will validate the Shipment Request Parameter
 */
@Component
public class ShipmentRequestValidator extends InputValidator {

    private static final Logger logger = Logger.getLogger(ShipmentRequestValidator.class);
    private static final String SYSTEM_CONFIG_SHIPMENT_CURRENT_STAGE = "SHIPMENT_STAGE";
    private static final String SYSTEM_CONFIG_SHIPMENT_CURRENT_STATUS = "SHIPMENT_STATUS";

    private ShipmentRequestValidator() {
    }

    public List<ErrorInfoDto> doValidateRaiseShipmentRequest(ShipmentRequestDto request) {
        logger.info(">>> Validation for Mandatory Data Raise Shipment Request :" + request);
        return validateCommonFields(request);
    }

    private List<ErrorInfoDto> validateCommonFields(ShipmentRequestDto element) {
        logger.info(">>> Validation for Common Fields Rasie Shipment Request :" + element);

        List<ErrorInfoDto> errorInfoDtoList = new ArrayList<ErrorInfoDto>();
        try {

            /** shipment request filed **/
            validateMandatoryField("shipmentType", element.getShipmentType(), errorInfoDtoList);
            ShipmentType shipmentType = ShipmentType.getShipmentType(element.getShipmentType());
            if (null == shipmentType) {
                populateInvalidData("shipmentType", errorInfoDtoList);
            }
            validateMandatoryField("sender", element.getSender(), errorInfoDtoList);// need to add validate sender
            ShipmentAddressee sender = ShipmentAddressee.getShipmentAddressee(element.getSender());
            if (null == sender) {
                populateInvalidData("sender", errorInfoDtoList);
            }
            validateMandatoryField("recipient", element.getRecipient(), errorInfoDtoList);// need to validate recipient
            ShipmentAddressee recipient = ShipmentAddressee.getShipmentAddressee(element.getSender());
            if (null == recipient) {
                populateInvalidData("recipient", errorInfoDtoList);
            }
            validateMandatoryField("sentBy", element.getSentBy(), errorInfoDtoList);
            validateMandatoryField("receivedBy", element.getSender(), errorInfoDtoList);
            validateMandatoryField("boxActualLength", element.getBoxActualLength(), errorInfoDtoList);
            validateMandatoryField("boxActualWidth", element.getBoxActualWidth(), errorInfoDtoList);
            validateMandatoryField("boxActualHeight", element.getBoxActualHeight(), errorInfoDtoList);
            validateMandatoryField("boxActualWeight", element.getBoxActualWeight(), errorInfoDtoList);
            validateMandatoryField("createdBy", element.getCreatedBy(), errorInfoDtoList);
            validateMandatoryField("hubId", element.getHubId(), errorInfoDtoList);
            if (element.getHubId() != null && !StringUtils.isEmpty(String.valueOf(element.getHubId()))) {
                validateFromCache("hubId", String.valueOf(element.getHubId()), Constants.HUB_MASTER_CACHE, errorInfoDtoList);
            }

            /** Shipment Asset Details filed **/
            if (CollectionUtils.isEmpty(element.getShipmentAssetDetails())) {
                ;
                errorInfoDtoList.add(new ErrorInfoDto(10, messageSource.getMessage(String.valueOf(GenericResponseCodes.MANDATORY_FIELD.getErrorCode()), new Object[] { "" }, null), "assetDetails"));
            } else {
                for (ShipmentAssetDetailsDto shipmentAssetDetailsDto : element.getShipmentAssetDetails()) {
                    validateMandatoryField("assetCategoryName", shipmentAssetDetailsDto.getAssetCategoryName(), errorInfoDtoList);
                    validateMandatoryField("assetModelName", shipmentAssetDetailsDto.getAssetModelName(), errorInfoDtoList);
                    validateMandatoryField("assetMakeName", shipmentAssetDetailsDto.getAssetMakeName(), errorInfoDtoList);
                    validateMandatoryField("assetPieceCount", shipmentAssetDetailsDto.getAssetPieceCount(), errorInfoDtoList);
                    validateMandatoryField("assetDeclareValue", shipmentAssetDetailsDto.getAssetDeclareValue(), errorInfoDtoList);
                    validateMandatoryField("assetActualLength", shipmentAssetDetailsDto.getAssetActualLength(), errorInfoDtoList);
                    validateMandatoryField("assetActualWidth", shipmentAssetDetailsDto.getAssetActualWidth(), errorInfoDtoList);
                    validateMandatoryField("assetActualHeight", shipmentAssetDetailsDto.getAssetActualHeight(), errorInfoDtoList);
                    validateMandatoryField("assetActualWeight", shipmentAssetDetailsDto.getAssetActualWeight(), errorInfoDtoList);
                    validateMandatoryField("createdBy", shipmentAssetDetailsDto.getCreatedBy(), errorInfoDtoList);
                }
            }

            if (element.getOriginAddressDetails() != null) {
                validateMandatoryField("shipmentAddressLine1", element.getOriginAddressDetails().getAddressLine1(), errorInfoDtoList);
                validateMandatoryField("mobileNo", element.getOriginAddressDetails().getMobileNo(), errorInfoDtoList);// need to add mobile no validation
                validateMandatoryField("pincode", element.getOriginAddressDetails().getPincode(), errorInfoDtoList);
                if (null != element.getOriginAddressDetails().getPincode() && !StringUtils.isEmpty(element.getOriginAddressDetails().getPincode())) {
                    validateFromCache("pincode", element.getOriginAddressDetails().getPincode(), Constants.PINCODE_MASTER_CACHE, errorInfoDtoList);
                }
                validateMandatoryField("createdBy", element.getOriginAddressDetails().getCreatedBy(), errorInfoDtoList);
            } else {
                errorInfoDtoList.add(new ErrorInfoDto(10, messageSource.getMessage(String.valueOf(GenericResponseCodes.MANDATORY_FIELD.getErrorCode()), new Object[] { "" }, null),
                        "orginAddressDetails"));
            }

            if (element.getDestinationAddressDetails() != null) {
                /** Shipment Destination Details filed **/
                validateMandatoryField("shipmentAddressLine1", element.getDestinationAddressDetails().getAddressLine1(), errorInfoDtoList);
                validateMandatoryField("mobileNo", element.getDestinationAddressDetails().getMobileNo(), errorInfoDtoList);// need to add mobile no validation
                validateMandatoryField("pincode", element.getDestinationAddressDetails().getPincode(), errorInfoDtoList);
                if (null != element.getDestinationAddressDetails().getPincode() && !StringUtils.isEmpty(element.getDestinationAddressDetails().getPincode())) {
                    validateFromCache("pincode", element.getDestinationAddressDetails().getPincode(), Constants.PINCODE_MASTER_CACHE, errorInfoDtoList);
                }
                validateMandatoryField("createdBy", element.getDestinationAddressDetails().getCreatedBy(), errorInfoDtoList);
            } else {
                errorInfoDtoList.add(new ErrorInfoDto(10, messageSource.getMessage(String.valueOf(GenericResponseCodes.MANDATORY_FIELD.getErrorCode()), new Object[] { "" }, null),
                        "destincationAddressDetails"));
            }

            validateDoubleFormat("shipmentDeclareValue", String.valueOf(element.getShipmentDeclareValue()), errorInfoDtoList);

        } catch (InputValidationException exception) {
            logger.error("-- Create Shipment request validation failed --");
        }
        logger.info("<< Validation for Common Fields Raise Shipment Request");
        return errorInfoDtoList;

    }

    public List<ErrorInfoDto> doValidateRasieShipmentRequest(ShipmentReassignRequestDto request) {
        logger.info(">>> Validation for Mandatory Data Reassign Shipment Request :" + request);
        return validateCommonFields(request);
    }

    private List<ErrorInfoDto> validateCommonFields(ShipmentReassignRequestDto element) {
        logger.info(">>> Validation for Common Fields Reassign Shipment Request :" + element);
        List<ErrorInfoDto> errorInfoDtoList = new ArrayList<ErrorInfoDto>();

        try {
            /** shipment request filed **/
            validateRequiredField("assignlogisticPartnerCode", element.getAssignlogisticPartnerCode(), errorInfoDtoList);
            validateFromCache("assignlogisticPartnerCode", element.getAssignlogisticPartnerCode(), Constants.PARTNER_MASTER_CACHE, errorInfoDtoList);
            validateRequiredField("modifiedBy", element.getModifiedBy(), errorInfoDtoList);
        } catch (InputValidationException exception) {
            logger.info("-- Shipment Reassign Request validation failed --");
        }

        logger.info("<< Validation for Common Fields Raise Shipment Request");
        return errorInfoDtoList;
    }

    public List<ErrorInfoDto> doValidateUpdateShipmentRequest(Long shipmentId, String fieldtoupdate, ShipmentUpdateRequestDto request) {
        logger.info(">>> Validation for Mandatory Data Update Shipment Request :" + request);
        return validateCommonFields(shipmentId, fieldtoupdate, request);
    }

    private List<ErrorInfoDto> validateCommonFields(Long shipmentId, String fieldtoupdate, ShipmentUpdateRequestDto element) {
        logger.info(">>> Validation for Common Fields Update Shipment Request :" + element);
        List<ErrorInfoDto> errorInfoDtoList = new ArrayList<ErrorInfoDto>();

        try {
            if (StringUtils.isBlank(fieldtoupdate)) {
                populateMandatoryFieldError(fieldtoupdate, errorInfoDtoList);
            } else {
                if (fieldtoupdate.equalsIgnoreCase("awb")) {
                    validateRequiredField("awb", element.getLogisticPartnerRefTrackingNumber(), errorInfoDtoList);
                } else if (fieldtoupdate.equalsIgnoreCase("status")) {
                    validateFromSystemConfigCache("status", SYSTEM_CONFIG_SHIPMENT_CURRENT_STATUS, String.valueOf(element.getStatus()), Constants.SYSTEM_CONFIG_MASTER_CACHE, errorInfoDtoList);
                } else if (fieldtoupdate.equalsIgnoreCase("currentstage")) {
                    validateFromSystemConfigCache("currentStage", SYSTEM_CONFIG_SHIPMENT_CURRENT_STAGE, String.valueOf(element.getCurrentStage()), Constants.SYSTEM_CONFIG_MASTER_CACHE,
                            errorInfoDtoList);
                } else if (fieldtoupdate.equalsIgnoreCase("reasonForFailure")) {
                    // Reason for failure can be reset to null in case of sucess.
                } else
                    populateMandatoryFieldError(fieldtoupdate, errorInfoDtoList);
            }

            validateRequiredField("shipmentId", shipmentId, errorInfoDtoList);
        } catch (InputValidationException exception) {
            logger.info("-- Update Shipment Request validation failed --");
        }

        return errorInfoDtoList;
    }

    public List<ErrorInfoDto> doValidateDashBoardRequest(DashBoardRequestDto dashBoardRequestDto) {

        logger.info(">>> Validation for Mandatory Data Update Shipment Request :" + dashBoardRequestDto);
        return validateCommonFields(dashBoardRequestDto);
    }

    public List<ErrorInfoDto> validateCommonFields(DashBoardRequestDto dashBoardRequestDto) {

        List<ErrorInfoDto> errorInfoList = new ArrayList<>();

        try {
            validateRequiredField("status", dashBoardRequestDto.getShipmentStatus(), errorInfoList);
            validateRequiredField("toDate", dashBoardRequestDto.getShipmentModifiedDate(), errorInfoList);
            validateDateFormatAndFutureDate("toDate", dashBoardRequestDto.getShipmentModifiedDate(), "dd-MMM-yyyy", errorInfoList);
        } catch (InputValidationException e) {
            logger.error(">>> Validation for Dashboard Request :", e);
        }

        return errorInfoList;
    }

    public List<ErrorInfoDto> doValidateShipmentTrackingRequest(Long shipmentId) {
        logger.info(">>> Validation for Mandatory Data Track Shipment History :" + shipmentId);
        List<ErrorInfoDto> errorInfoDtoList = new ArrayList<ErrorInfoDto>();

        if (shipmentId == null || shipmentId == 0l) {
            populateMandatoryFieldError("shipmentId", errorInfoDtoList);
        }

        return errorInfoDtoList;
    }

    public List<ErrorInfoDto> doValidateShipmentSearchRequest(ShipmentSearchRequestDto shipmentSearchRequestDto) {

        logger.info(">>> Validation of Shipment search request for input data error:" + shipmentSearchRequestDto);
        return validateCommonFields(shipmentSearchRequestDto);
    }

    public List<ErrorInfoDto> validateCommonFields(ShipmentSearchRequestDto shipmentSearchRequestDto) {

        List<ErrorInfoDto> errorInfoList = new ArrayList<>();

        try {
            if (shipmentSearchRequestDto.getShipmentId() != null)
                validateNumericData("shipmentId", shipmentSearchRequestDto.getShipmentId().toString(), errorInfoList);
            if (shipmentSearchRequestDto.getServiceId() != null)
                validateNumericData("serviceId", shipmentSearchRequestDto.getServiceId().toString(), errorInfoList);
            if (shipmentSearchRequestDto.getHubId() != null)
                validateNumericData("hubId", shipmentSearchRequestDto.getHubId().toString(), errorInfoList);
            if (shipmentSearchRequestDto.getLogisticPartnerCode() != null)
                validateRequiredField("logisticPartnerCode", shipmentSearchRequestDto.getLogisticPartnerCode(), errorInfoList);
            if (shipmentSearchRequestDto.getFromDate() != null)
                validateDateFormat("fromDate", shipmentSearchRequestDto.getFromDate().toString(), "dd-MMM-yyyy", errorInfoList);
            if (shipmentSearchRequestDto.getToDate() != null)
                validateDateFormat("toDate", shipmentSearchRequestDto.getToDate().toString(), "dd-MMM-yyyy", errorInfoList);
        } catch (InputValidationException inputValidationException) {
            logger.error(">>> Validation for Shipment Search Request :", inputValidationException);
        }

        return errorInfoList;
    }

    @SuppressWarnings("rawtypes")
    public List<ErrorInfoDto> doValidateShipmentTrackingRequest(HashMap trackingRequest) {
        logger.info(">>> Validation for Mandatory Data Track Shipment :" + trackingRequest);
        List<ErrorInfoDto> errorInfoDtoList = new ArrayList<ErrorInfoDto>();
        if (!CollectionUtils.isEmpty(trackingRequest)) {
            String notificationType = (String) trackingRequest.get("notificationType");
            String clientShipmentId = (String) trackingRequest.get("clientShipmentId");
            String orderNo = (String) trackingRequest.get("orderNo");
            if (StringUtils.isEmpty(notificationType)) {
                populateMandatoryFieldError("notificationType", errorInfoDtoList);
            }
            if (StringUtils.isEmpty(clientShipmentId) && StringUtils.isEmpty(orderNo)) {
                populateMandatoryFieldError("clientShipmentId/orderNo", errorInfoDtoList);
            }
        } else {
            populateMandatoryFieldError("trackingRequest", errorInfoDtoList);
        }

        return errorInfoDtoList;
    }
}
