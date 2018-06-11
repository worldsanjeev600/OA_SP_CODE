package com.oneassist.serviceplatform.commons.validators;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import com.google.common.base.Strings;
import com.oneassist.serviceplatform.commons.cache.SystemConfigMasterCache;
import com.oneassist.serviceplatform.commons.cache.base.CacheFactory;
import com.oneassist.serviceplatform.commons.constants.Constants;
import com.oneassist.serviceplatform.commons.enums.ErrorCodes;
import com.oneassist.serviceplatform.commons.enums.LogisticResponseCodes;
import com.oneassist.serviceplatform.commons.utils.ServiceRequestHelper;
import com.oneassist.serviceplatform.contracts.dtos.shipment.ShipmentStatusDto;
import com.oneassist.serviceplatform.externalcontracts.PartnerMasterDto;
import com.oneassist.serviceplatform.externalcontracts.SystemConfigDto;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * ShipmentStatusUpdate field validate
 * 
 * @author divya.hl
 */
@Component
public class ShipmentStatusUpdateValidator extends BaseValidator {

    private Logger logger = Logger.getLogger(ShipmentStatusUpdateValidator.class);

    @Autowired
    private CacheFactory cacheFactory;

    @Autowired
    private SystemConfigMasterCache systemConfigMasterCache;

    @Autowired
    private ServiceRequestHelper serviceRequestHelper;

    /**
     * validate ShipmentStatusDto
     * 
     * @param shipmentStatusDto
     * @return error string
     */
    public String validateShipmentStatusUpdateDto(ShipmentStatusDto shipmentStatusDto) {

        logger.info("<<<<<<<<<<<In ShipmentStatusUpdateValidator :validateShipmentStatusUpdateDto():starts ");
        StringBuilder errorText = new StringBuilder();
        errorText.append(validateShipment(shipmentStatusDto.getShipmentId()));
        errorText.append(validatePartner(shipmentStatusDto.getPartnerCode()));
        errorText.append(validateCurrentStage(shipmentStatusDto));
        logger.info("<<<<<<<<<<<In ShipmentStatusUpdateValidator :validateShipmentStatusUpdateDto():ends ");
        return errorText.toString();
    }

    public String validateShipment(Long shipmentId) {

        if (shipmentId == null) {
            return messageSource.getMessage((String.valueOf(LogisticResponseCodes.SHIPMENT_ID_EMPTY.getErrorCode())), new Object[] { "" }, null) + ",";
        }
        return "";
    }

    /**
     * @param partnerCode
     * @return
     */
    public String validatePartner(Long partnerCode) {

        String partner = String.valueOf(partnerCode);

        if (partner == null) {
            return messageSource.getMessage((String.valueOf(LogisticResponseCodes.PARTNER_CODE_EMPTY.getErrorCode())), new Object[] { "" }, null) + ",";
        }

        if (cacheFactory == null) {
            return messageSource.getMessage((String.valueOf(ErrorCodes.UNABLE_LOAD_CACHE_FACTORY.getErrorCode())), new Object[] { "" }, null) + ",";
        }

        PartnerMasterDto partnerMasterDto = (PartnerMasterDto) cacheFactory.get(Constants.PARTNER_MASTER_CACHE).get(partner);

        if (partnerMasterDto == null) {
            return messageSource.getMessage((String.valueOf(ErrorCodes.INVALID_PARTNER_CODE.getErrorCode())), new Object[] { "" }, null) + ",";
        }

        return "";
    }

    public String validateCurrentStage(ShipmentStatusDto shipmentStatusDto) {

        String currentStage = shipmentStatusDto.getCurrentStage();

        try {
            if (Strings.isNullOrEmpty(currentStage)) {
                return messageSource.getMessage((String.valueOf(LogisticResponseCodes.SHIPMENT_CURRENT_STAGE_EMPTY.getErrorCode())), new Object[] { "" }, null) + ",";
            }

            if (systemConfigMasterCache == null) {
                return messageSource.getMessage((String.valueOf(ErrorCodes.UNABLE_LOAD_CACHE_FACTORY.getErrorCode())), new Object[] { "" }, null) + ",";
            }

            List<SystemConfigDto> shipmentStatusConfigList = serviceRequestHelper.getShipmentStage(null, 0);

            if (shipmentStatusConfigList == null || shipmentStatusConfigList.isEmpty()) {
                return messageSource.getMessage((String.valueOf(LogisticResponseCodes.SHIPMENT_CURRENT_STAGES_NOTCONFIGUED.getErrorCode())), new Object[] { "" }, null) + ",";
            }

            boolean shipmentStageExists = false;

            for (SystemConfigDto systemConfigDto : shipmentStatusConfigList) {
                if (currentStage.trim().equalsIgnoreCase(systemConfigDto.getParamValue())) {
                    shipmentStageExists = true;
                    shipmentStatusDto.setCurrentStage(systemConfigDto.getParamName());
                }
            }

            if (!shipmentStageExists) {
                return messageSource.getMessage((String.valueOf(LogisticResponseCodes.INVALID_SHIPMENT_CURRENT_STAGE.getErrorCode())), new Object[] { "" }, null) + ",";
            }

        } catch (Exception e) {
            return messageSource.getMessage((String.valueOf(LogisticResponseCodes.SHIPMENT_STAGE_VALIDATION_FAILED.getErrorCode())), new Object[] { "" }, null) + ",";
        }

        return "";
    }

    public String validateShipmentModificationDate(Date shipmentModifiedOn, ShipmentStatusDto shipmentStatusDto) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy h:m a");
        Date shipmentModificationDt = null;

        try {
            shipmentModificationDt = dateFormat.parse(shipmentStatusDto.getStatusUpdateDate() + " " + shipmentStatusDto.getStatusUpdateTime());
        } catch (ParseException e) {
            return messageSource.getMessage((String.valueOf(LogisticResponseCodes.INVALID_SHIPMENT_MODIFICATION_DATE_FORMAT.getErrorCode())), new Object[] { "" }, null) + ",";
        }

        if (shipmentModifiedOn.after(shipmentModificationDt)) {
            return messageSource.getMessage((String.valueOf(LogisticResponseCodes.INVALID_SHIPMENT_MODIFICATION_DATE.getErrorCode())), new Object[] { "" }, null) + ",";
        }

        return "";
    }
}
