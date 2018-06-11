package com.oneassist.serviceplatform.services.logisticpartner.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.google.common.base.Strings;
import com.oneassist.serviceplatform.commons.entities.ShipmentReassignEntity;
import com.oneassist.serviceplatform.commons.enums.LogisticResponseCodes;
import com.oneassist.serviceplatform.commons.repositories.LogisticShipmentReassignmentRepository;
import com.oneassist.serviceplatform.commons.validators.InputValidator;
import com.oneassist.serviceplatform.contracts.dtos.ErrorInfoDto;
import com.oneassist.serviceplatform.contracts.response.base.ResponseDto;
import com.oneassist.serviceplatform.services.commons.ICommonService;
import com.oneassist.serviceplatform.services.constant.ResponseConstant;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

public abstract class BaseLogisticPartnerService implements ILogisticPartnerService {

    private final Logger log = Logger.getLogger(BaseLogisticPartnerService.class);

    @Autowired
    private MessageSource messageSource;

    @Autowired
    @Qualifier("commonService")
    private ICommonService commonService;

    @Autowired
    private InputValidator inputValidator;

    @Autowired
    private LogisticShipmentReassignmentRepository logisticShipmentRepository;

    private static final String AWB_PARAM_NAME = "awb";

    private static final String REASON_CODE_NUMBER = "reason_code_number";
    private static final String ORDER_NUMBER_PARAM = "order_number";

    @SuppressWarnings("rawtypes")
    @Override
    public Object trackShipment(HashMap trackingPayload) {
        List<ErrorInfoDto> errorInfoList = doValidateShipmentTrackingRequest(trackingPayload);
        ResponseDto<Object> response = new ResponseDto<>();
        try {
            if (null != errorInfoList && errorInfoList.size() > 0) {
                response.setStatus(ResponseConstant.FAILED);
                response.setInvalidData(errorInfoList);
            } else {
                String awbNumber = (String) trackingPayload.get(AWB_PARAM_NAME);
                String reasonCode = (String) trackingPayload.get(REASON_CODE_NUMBER);
                String orderNumber = (String) trackingPayload.get(ORDER_NUMBER_PARAM);
                log.info("Shipment id for tracking is ::" + awbNumber + " or " + orderNumber);
                ShipmentReassignEntity shipment = null;
                if (!Strings.isNullOrEmpty(orderNumber)) {
                    shipment = logisticShipmentRepository.findOne(Long.parseLong(orderNumber));
                } else if (!Strings.isNullOrEmpty(awbNumber)) {
                    shipment = commonService.getShipmentEntityByAWBNumber(awbNumber);
                }
                if (shipment != null) {
                    commonService.updateShipmentStage(shipment, reasonCode, awbNumber);
                    response.setStatus(ResponseConstant.SUCCESS);
                    response.setMessage(messageSource.getMessage((String.valueOf(LogisticResponseCodes.SHIPMENT_TRACKINGHISTORY_SUCCESS.getErrorCode())), new Object[] { "" }, null));
                } else {
                    errorInfoList = new ArrayList<ErrorInfoDto>();
                    ErrorInfoDto errorInfo = new ErrorInfoDto(LogisticResponseCodes.SHIPMENT_DOESNOT_EXIST.getErrorCode(), messageSource.getMessage(
                            (String.valueOf(LogisticResponseCodes.SHIPMENT_DOESNOT_EXIST.getErrorCode())), new Object[] { "" }, null));
                    errorInfoList.add(errorInfo);
                    response.setInvalidData(errorInfoList);
                    response.setStatus(ResponseConstant.FAILED);
                    response.setStatusCode(String.valueOf(LogisticResponseCodes.REASSIGN_SHIPMENT_REQUEST_FAILED.getErrorCode()));
                }
            }
        } catch (Exception e) {
            log.error("Exception occurred while getting Shipment Tracking History::" + trackingPayload, e);
            response.setStatus(ResponseConstant.FAILED);
            response.setMessage(messageSource.getMessage((String.valueOf(LogisticResponseCodes.SHIPMENT_TRACKINGHISTORY_FAILED.getErrorCode())), new Object[] { "" }, null));
        }
        return response;
    }

    @SuppressWarnings("rawtypes")
    public List<ErrorInfoDto> doValidateShipmentTrackingRequest(HashMap trackingRequest) {
        log.info(">>> Validation for Mandatory Data Track Shipment :" + trackingRequest);
        List<ErrorInfoDto> errorInfoDtoList = new ArrayList<ErrorInfoDto>();
        if (!CollectionUtils.isEmpty(trackingRequest)) {
            String awb = (String) trackingRequest.get(AWB_PARAM_NAME);
            String reasonCode = (String) trackingRequest.get(REASON_CODE_NUMBER);

            if (StringUtils.isEmpty(awb)) {
                inputValidator.populateMandatoryFieldError("awb", errorInfoDtoList);
            }
            if (StringUtils.isEmpty(reasonCode)) {
                inputValidator.populateMandatoryFieldError("reasonCode", errorInfoDtoList);
            }

        } else {
            inputValidator.populateMandatoryFieldError("trackingRequest", errorInfoDtoList);
        }

        return errorInfoDtoList;
    }
}
