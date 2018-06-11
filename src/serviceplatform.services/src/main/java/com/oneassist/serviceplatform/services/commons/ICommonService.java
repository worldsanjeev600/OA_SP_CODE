package com.oneassist.serviceplatform.services.commons;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;
import com.oneassist.serviceplatform.commons.entities.ShipmentReassignEntity;
import com.oneassist.serviceplatform.commons.enums.MasterData;
import com.oneassist.serviceplatform.commons.exception.BusinessServiceException;
import com.oneassist.serviceplatform.contracts.dtos.PartnerEventDetailDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ServiceScheduleDto;
import com.oneassist.serviceplatform.contracts.dtos.shipment.ShipmentDetailsDto;
import com.oneassist.serviceplatform.contracts.response.base.ResponseDto;
import com.oneassist.serviceplatform.externalcontracts.ClaimLifecycleEvent;
import com.oneassist.serviceplatform.externalcontracts.HubMasterDto;
import com.oneassist.serviceplatform.externalcontracts.PincodeMasterDto;
import com.oneassist.serviceplatform.externalcontracts.ServicePartner;

public interface ICommonService {

    public Map<String, Object> getAllData(List<String> dropdowns) throws Exception;

    public ResponseDto<ServiceScheduleDto> getAvailableScheduleSlotsForInspection(ServiceScheduleDto serviceScheduleDto) throws BusinessServiceException;

    public HubMasterDto getHubDetails(String hubId) throws Exception;

    public PincodeMasterDto getPincodeMaster(String pincode) throws Exception;

    public String getStateCode(String stateCode) throws Exception;

    public PartnerEventDetailDto getPartnerEventMst(String eventCode) throws Exception;

    public void storeThirdPartyInteractionDetailsInMongo(Long srNo, ClaimLifecycleEvent event, Object requestJsonPayloadString, Object responseJsonPayloadString, String error);

    public boolean storeMongoRefInDoc(Long serviceRequestId, String trackingNumber, File label) throws Exception;

    public void updateShipmentFailReason(Long shipmentId, String failedReason, Exception exception, String modifiedBy);

    public void updateShipmentAWB(Long shipmentId, String awb, String partnerCode, String modifiedBy) throws Exception;

    public void updateShipmentStage(ShipmentReassignEntity shipment, String notificationType, String awbNumber) throws Exception;

    public Long getPartnerCodeByName(ServicePartner servicePartner);

    public List<ShipmentDetailsDto> getShipmentsForTracking(Long partnerCode);

    public String getOneassistShipmentStage(String statusCode, String partnerCode, Integer shipmentType);

    public void updateShipmentStage(Long shipmentId, String stageName, Date updatedOn, String modifiedBy) throws Exception;

    public ShipmentReassignEntity getShipmentEntityByAWBNumber(String awbNumber);

    public PincodeMasterDto getStateAndCity(String pincode) throws Exception;

    public List<Map<String, Object>> filterMasterData(MasterData masterData, String partnerCodes);

    public String getInvoiceValue(Double shipmentDeclareValue);

    public void clearCache(String cacheName);

}
