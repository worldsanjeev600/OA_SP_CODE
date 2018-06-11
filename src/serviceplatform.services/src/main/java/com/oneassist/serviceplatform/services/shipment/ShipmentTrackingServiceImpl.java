/**
 * 
 */
package com.oneassist.serviceplatform.services.shipment;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.oneassist.serviceplatform.commons.cache.PartnerMasterCache;
import com.oneassist.serviceplatform.commons.cache.SystemConfigMasterCache;
import com.oneassist.serviceplatform.commons.constants.CacheConstants;
import com.oneassist.serviceplatform.commons.entities.ServiceAddressEntity;
import com.oneassist.serviceplatform.commons.entities.ShipmentEntity;
import com.oneassist.serviceplatform.commons.entities.ShipmentStageEntity;
import com.oneassist.serviceplatform.commons.enums.LogisticResponseCodes;
import com.oneassist.serviceplatform.commons.exception.BusinessServiceException;
import com.oneassist.serviceplatform.commons.repositories.LogisticShipmentRepository;
import com.oneassist.serviceplatform.commons.utils.ServiceRequestHelper;
import com.oneassist.serviceplatform.commons.validators.ShipmentRequestValidator;
import com.oneassist.serviceplatform.contracts.dtos.ErrorInfoDto;
import com.oneassist.serviceplatform.contracts.dtos.shipment.ShipmentStatusHistoryDto;
import com.oneassist.serviceplatform.contracts.dtos.shipment.ShipmentTrackingResponseDto;
import com.oneassist.serviceplatform.contracts.response.base.ResponseDto;
import com.oneassist.serviceplatform.externalcontracts.PartnerMasterDto;
import com.oneassist.serviceplatform.externalcontracts.ServicePartner;
import com.oneassist.serviceplatform.externalcontracts.SystemConfigDto;
import com.oneassist.serviceplatform.services.constant.ResponseConstant;
import com.oneassist.serviceplatform.services.logisticpartner.services.ILogisticPartnerService;

/**
 * @author priya.prakash
 *
 */
@Service
@SuppressWarnings("rawtypes")
public class ShipmentTrackingServiceImpl implements IShipmentTrackingService {

    private static final Logger logger = LoggerFactory.getLogger(ShipmentTrackingServiceImpl.class);

    @Autowired
    ServiceRequestHelper serviceRequestHelper;

    @Autowired
    private ShipmentRequestValidator shipmentRequestValidator;

    @Autowired
    private LogisticShipmentRepository logisticShipmentRepository;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private SystemConfigMasterCache systemConfigMasterCache;

    @Autowired
    @Qualifier("logisticProviders")
    private HashMap<String, ILogisticPartnerService> logisticProviders;
    
    @Autowired
    private PartnerMasterCache partnerMasterCache;

    @Override
    public Object trackShipment(HashMap trackRequest, String partnerCode) throws Exception {
        Object response = new Object();
        if (!CollectionUtils.isEmpty(trackRequest) && !StringUtils.isEmpty(partnerCode)) {
        	PartnerMasterDto partnerMasterDto = partnerMasterCache.get(String.valueOf(partnerCode));
            ServicePartner servicePartner = ServicePartner.valueOf(partnerMasterDto.getPartnerName());
            if(servicePartner != null){
            	 switch (servicePartner) {
                 case LOGINEXT:
                 case ECOM:
                 case DHL:
                     response = logisticProviders.get(partnerMasterDto.getPartnerName()).trackShipment(trackRequest);
                     break;
                 default:
                     logger.error("tracking  event is not configured for the partner ::" + partnerCode);
                     break;
            	 }
            }
        }

        return response;
    }

    @Override
    public ResponseDto<List<ShipmentTrackingResponseDto>> getShipmentTrackingHistory(Long shipmentId) throws NoSuchMessageException, BusinessServiceException {

        ResponseDto<List<ShipmentTrackingResponseDto>> response = new ResponseDto<>();
        List<ShipmentStatusHistoryDto> shipmentStatusHistoryList = new ArrayList<>();
        ShipmentTrackingResponseDto shipmentTrackingResponseDto = new ShipmentTrackingResponseDto();
        List<ShipmentTrackingResponseDto> trackingResponseList = new ArrayList<>();

        List<ErrorInfoDto> errorInfoList = shipmentRequestValidator.doValidateShipmentTrackingRequest(shipmentId);

        if (null != errorInfoList && errorInfoList.size() > 0) {
            response.setInvalidData(errorInfoList);
            response.setStatus(ResponseConstant.FAILED);
            response.setMessage("Failed");
        } else {

            ShipmentEntity shipmentEntity = logisticShipmentRepository.findOne(shipmentId);
            if (shipmentEntity == null) {

                throw new BusinessServiceException(LogisticResponseCodes.SHIPMENT_HISTORY_NOTFOUND.getErrorCode());
            } else {

                shipmentTrackingResponseDto.setShipmentId(shipmentEntity.getShipmentId());
                ServiceAddressEntity originAddress = shipmentEntity.getOriginAddressDetails();

                if (originAddress != null) {
                    shipmentTrackingResponseDto.setOriginAddressId(new BigDecimal(originAddress.getServiceAddressId()));
                    shipmentTrackingResponseDto.setOriginAddressLine1(originAddress.getAddressLine1());
                    shipmentTrackingResponseDto.setOriginAddressLine2(originAddress.getAddressLine2());
                    shipmentTrackingResponseDto.setOriginLandmark(originAddress.getLandmark());
                    shipmentTrackingResponseDto.setOriginPincode(originAddress.getPincode());
                    shipmentTrackingResponseDto.setOriginDistrict(originAddress.getDistrict());
                    shipmentTrackingResponseDto.setOriginMobileNo(originAddress.getMobileNo());
                    shipmentTrackingResponseDto.setOriginCountryCode(originAddress.getCountryCode());
                    shipmentTrackingResponseDto.setOrigAddressFullName(originAddress.getAddresseeFullName());
                }

                ServiceAddressEntity destAddress = shipmentEntity.getDestinationAddressDetails();
                if (destAddress != null) {
                    shipmentTrackingResponseDto.setDestAddressId(new BigDecimal(destAddress.getServiceAddressId()));
                    shipmentTrackingResponseDto.setDestAddressLine1(destAddress.getAddressLine1());
                    shipmentTrackingResponseDto.setDestAddressLine2(destAddress.getAddressLine2());
                    shipmentTrackingResponseDto.setDestLandmark(destAddress.getLandmark());
                    shipmentTrackingResponseDto.setDestPincode(destAddress.getPincode());
                    shipmentTrackingResponseDto.setDestDistrict(destAddress.getDistrict());
                    shipmentTrackingResponseDto.setDestMobileNo(destAddress.getMobileNo());
                    shipmentTrackingResponseDto.setDestCountryCode(destAddress.getCountryCode());
                    shipmentTrackingResponseDto.setDestAddressFullName(destAddress.getAddresseeFullName());
                }

                shipmentTrackingResponseDto.setLogisticPartnerRefNum(shipmentEntity.getLogisticPartnerRefTrackingNumber());

                if (!CollectionUtils.isEmpty(shipmentEntity.getShipmentStages())) {
                    for (ShipmentStageEntity shipmentStage : shipmentEntity.getShipmentStages()) {
                        ShipmentStatusHistoryDto shipmentStatusHistoryDto = new ShipmentStatusHistoryDto();
                        String shipmentStageCode = shipmentStage.getShipmentStage();
                        shipmentStatusHistoryDto.setFromDate(shipmentStage.getStartTime());
                        shipmentStatusHistoryDto.setToDate(shipmentStage.getEndTime());

                        for (SystemConfigDto systemConfigDto : systemConfigMasterCache.get(CacheConstants.SHIPMENT_STAGE_PARAMCODE)) {
                            if (systemConfigDto.getParamName().equalsIgnoreCase(shipmentStageCode)) {
                                shipmentStatusHistoryDto.setShipmentStatus(systemConfigDto.getParamValue());
                                break;
                            }
                        }

                        shipmentStatusHistoryList.add(shipmentStatusHistoryDto);
                    }
                }

                if (!CollectionUtils.isEmpty(shipmentStatusHistoryList)) {
                    sortStageByCreatedDate(shipmentStatusHistoryList);
                }

                shipmentTrackingResponseDto.setHistory(shipmentStatusHistoryList);
                trackingResponseList.add(shipmentTrackingResponseDto);
            }

            response.setData(trackingResponseList);
        }

        return response;
    }

    private void sortStageByCreatedDate(List<ShipmentStatusHistoryDto> shipmentStatusHistoryList) {
        try {
            Collections.sort(shipmentStatusHistoryList, new Comparator<ShipmentStatusHistoryDto>() {

                @Override
                public int compare(ShipmentStatusHistoryDto e1, ShipmentStatusHistoryDto e2) {
                    int equality = 0;
                    if (e1.getFromDate() != null && e2.getFromDate() != null) {
                        if (e1.getFromDate().after(e2.getFromDate())) {
                            equality = 1;
                        } else if (e2.getFromDate().after(e1.getFromDate())) {
                            equality = -1;
                        }
                    }
                    return equality;
                }
            });
        } catch (Exception e) {
            logger.error("Exception while sorting stages by time", e);
        }
    }
}
