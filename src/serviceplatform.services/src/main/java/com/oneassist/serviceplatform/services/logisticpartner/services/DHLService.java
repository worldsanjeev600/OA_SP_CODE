package com.oneassist.serviceplatform.services.logisticpartner.services;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oneassist.serviceplatform.commons.cache.PinCodeMasterCache;
import com.oneassist.serviceplatform.commons.enums.DHLQualityCheckCodes;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestType;
import com.oneassist.serviceplatform.commons.proxies.DHLProxy;
import com.oneassist.serviceplatform.commons.repositories.LogisticShipmentReassignmentRepository;
import com.oneassist.serviceplatform.commons.validators.InputValidator;
import com.oneassist.serviceplatform.contracts.dtos.shipment.ServiceAddressDetailDto;
import com.oneassist.serviceplatform.contracts.dtos.shipment.ShipmentAssetDetailsDto;
import com.oneassist.serviceplatform.contracts.dtos.shipment.ShipmentRequestDto;
import com.oneassist.serviceplatform.externalcontracts.PincodeMasterDto;
import com.oneassist.serviceplatform.externalcontracts.dhl.DHLAddressRequestDto;
import com.oneassist.serviceplatform.externalcontracts.dhl.DHLPickupRequest;
import com.oneassist.serviceplatform.externalcontracts.dhl.QualityCheck;
import com.oneassist.serviceplatform.services.commons.ICommonService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service("dhlService")
public class DHLService extends BaseLogisticPartnerService implements ILogisticPartnerService {

    private final Logger log = Logger.getLogger(DHLService.class);

    @Autowired
    @Qualifier("commonService")
    private ICommonService commonService;

    @Autowired
    private InputValidator inputValidator;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private LogisticShipmentReassignmentRepository logisticShipmentRepository;

    @Autowired
    private PinCodeMasterCache pincodeMasterCache;

    @Autowired
    private DHLProxy dhlProxy;

    @Override
    public Object createShipment(ShipmentRequestDto shipmentRequest) throws Exception {

        if (shipmentRequest != null) {
            if (shipmentRequest.getShipmentType() == 1) {
                DHLPickupRequest dhlPickupRequest = this.populatePickupRequest(shipmentRequest);
                String payload = new ObjectMapper().writeValueAsString(dhlPickupRequest);
                try {
                    dhlProxy.uploadSftpFile(payload, shipmentRequest);
                    commonService.updateShipmentFailReason(shipmentRequest.getShipmentId(), null, null, shipmentRequest.getModifiedBy());
                } catch (Exception ex) {
                    log.error("Error assigning Pickup Request to DHL:: hence retrying", ex);
                    // Removing Retry as the message still exists in the queue in case if its not processed successfully
                    // retry(payload, shipmentRequest);
                }
            }
        }

        return null;
    }

    @Override
    public Object cancelShipment(String logistincPartnerTrackingNumber) throws Exception {
        // This is not supported in Ph1
        return null;
    }

    private DHLPickupRequest populatePickupRequest(ShipmentRequestDto shipmentRequest) throws Exception {

        DHLPickupRequest dhlPickupRequest = new DHLPickupRequest();
        try {
            dhlPickupRequest.setOrderType(ServiceRequestType.PICKUP.getRequestType());
            dhlPickupRequest.setOrderNumber(String.valueOf(shipmentRequest.getShipmentId()));
            dhlPickupRequest.setQuantity(shipmentRequest.getBoxCount() != null ? Integer.parseInt(shipmentRequest.getBoxCount()) : 0);
            dhlPickupRequest.setBoxLength(shipmentRequest.getBoxActualLength());
            dhlPickupRequest.setBoxHeight(shipmentRequest.getBoxActualHeight());
            dhlPickupRequest.setBoxWeight(shipmentRequest.getBoxActualWeight());

            dhlPickupRequest.setOriginAddress(getAddress(shipmentRequest.getOriginAddressDetails()));
            dhlPickupRequest.setDestinationAddress(getAddress(shipmentRequest.getDestinationAddressDetails()));

            List<ShipmentAssetDetailsDto> shipmentAssets = shipmentRequest.getShipmentAssetDetails();

            if (shipmentAssets != null && !shipmentAssets.isEmpty()) {
                ShipmentAssetDetailsDto asset = shipmentAssets.get(0);
                dhlPickupRequest.setItemDescription(asset.getAssetMakeName() + " " + asset.getAssetModelName());
                populateAssetDetails(asset, dhlPickupRequest);

                List<QualityCheck> qualityChecks = new ArrayList<>();
                qualityChecks.add(getQCCheck(asset.getAssetMakeName() + " " + asset.getAssetModelName()));
                qualityChecks.add(getQCCheck(asset.getAssetReferenceNo()));
                dhlPickupRequest.setQc(qualityChecks);
            }
        } catch (Exception e) {
            log.error("Error Populating DHL Pickup Request:", e);
            throw new Exception("Error Populating DHL Pickup Request:" + e);
        }
        return dhlPickupRequest;
    }

    private void populateAssetDetails(ShipmentAssetDetailsDto asset, DHLPickupRequest dhlPickupRequest) {
        dhlPickupRequest.setActualLength(asset.getAssetActualLength());
        dhlPickupRequest.setActualHeight(asset.getAssetActualHeight());
        dhlPickupRequest.setActualWeight(asset.getAssetActualWeight());
        dhlPickupRequest.setAssetValue(Double.valueOf(commonService.getInvoiceValue(asset.getAssetDeclareValue())));
        dhlPickupRequest.setAddtionalInfo(asset.getRemarks());
    }

    private QualityCheck getQCCheck(String qcValue) {
        QualityCheck itemCheck = new QualityCheck();
        itemCheck.setQcCheckCode(DHLQualityCheckCodes.GEN_ITEM_DESC_CHECK.getCheckCode());
        itemCheck.setQcCheckValue(qcValue);
        itemCheck.setDescription(DHLQualityCheckCodes.CHECK.getCheckCode());
        return itemCheck;
    }

    private DHLAddressRequestDto getAddress(ServiceAddressDetailDto address) {
        DHLAddressRequestDto destinationAddress = new DHLAddressRequestDto();
        PincodeMasterDto destPincodeDto = pincodeMasterCache.get(address.getPincode());
        destinationAddress.setCity(destPincodeDto.getCityName());
        destinationAddress.setLine1(address.getAddressLine1() + " " + address.getAddressLine2());
        destinationAddress.setMobile(String.valueOf(address.getMobileNo()));
        destinationAddress.setName(address.getAddresseeFullName());
        destinationAddress.setPincode(Integer.parseInt(destPincodeDto.getPinCode()));
        destinationAddress.setState(destPincodeDto.getStateName());
        return destinationAddress;
    }
}
