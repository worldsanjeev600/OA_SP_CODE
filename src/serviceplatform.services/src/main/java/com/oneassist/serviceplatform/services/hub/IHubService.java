package com.oneassist.serviceplatform.services.hub;

import java.util.List;
import com.oneassist.serviceplatform.commons.exception.BusinessServiceException;
import com.oneassist.serviceplatform.contracts.dtos.hub.HubAllocationDto;
import com.oneassist.serviceplatform.contracts.dtos.hub.HubAllocationRequestDto;
import com.oneassist.serviceplatform.contracts.dtos.hub.HubAllocationSearchReasultDto;
import com.oneassist.serviceplatform.contracts.dtos.hub.HubAllocationSearchRequestDto;
import com.oneassist.serviceplatform.contracts.response.base.PageResponseDto;
import org.springframework.data.domain.PageRequest;

public interface IHubService {

    public HubAllocationDto getHubByPincodeAndMake(HubAllocationDto hubAllocationRequestDto) throws BusinessServiceException;

    public void editHubAllocation(HubAllocationRequestDto hubAllocationRequest);

    public PageResponseDto<List<HubAllocationSearchReasultDto>> getHubAllocation(HubAllocationSearchRequestDto hubAllocationSearchRequestDto, PageRequest pageRequest);

}
