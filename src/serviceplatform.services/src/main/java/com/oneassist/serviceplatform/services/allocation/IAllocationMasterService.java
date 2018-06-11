package com.oneassist.serviceplatform.services.allocation;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import com.oneassist.serviceplatform.commons.exception.BusinessServiceException;
import com.oneassist.serviceplatform.contracts.dtos.allocationmaster.PincodeServiceDto;
import com.oneassist.serviceplatform.contracts.dtos.allocationmaster.PincodeServiceFulfilmentDto;
import com.oneassist.serviceplatform.contracts.dtos.allocationmaster.PincodeServiceFulfilmentResponse;
import com.oneassist.serviceplatform.contracts.dtos.allocationmaster.ServicePartnerAllocationRequest;
import com.oneassist.serviceplatform.contracts.dtos.allocationmaster.ServicePartnerAllocationSearchRequest;
import com.oneassist.serviceplatform.contracts.dtos.allocationmaster.ServicePartnerAllocationSearchResult;
import com.oneassist.serviceplatform.contracts.response.base.PageResponseDto;
import com.oneassist.serviceplatform.contracts.response.base.ResponseDto;
import org.springframework.data.domain.PageRequest;

public interface IAllocationMasterService {

    public ResponseDto<List<PincodeServiceDto>> addPincodeServiceMaster(List<PincodeServiceDto> pincodeServiceDtos) throws BusinessServiceException;

    public ResponseDto<List<PincodeServiceFulfilmentDto>> addPincodeServiceFulfilmentMaster(List<PincodeServiceFulfilmentDto> pincodeServiceFulfilmentDtos) throws BusinessServiceException;

    public ResponseDto<List<PincodeServiceFulfilmentResponse>> getPincodeFulfilments(String pinCode, String shipmentId, String serviceId);

    public ResponseDto<Map<String, List<Object>>> loadAllocationMasterData();

    public void modifyServicePartnerAllocation(List<ServicePartnerAllocationRequest> servicePartnerAllocationRequest);

    public PageResponseDto<Collection<ServicePartnerAllocationSearchResult>> getServicePartnerAllocation(ServicePartnerAllocationSearchRequest servicePartnerAllocationSearchRequest,
            PageRequest pageRequest);
}
