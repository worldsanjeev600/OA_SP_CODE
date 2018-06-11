package com.oneassist.serviceplatform.services.hub;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.oneassist.RuleEngine;
import com.oneassist.enums.RuleName;
import com.oneassist.serviceplatform.commons.cache.HubMasterCache;
import com.oneassist.serviceplatform.commons.cache.PinCodeMasterCache;
import com.oneassist.serviceplatform.commons.constants.Constants;
import com.oneassist.serviceplatform.commons.entities.PincodeServiceEntity;
import com.oneassist.serviceplatform.commons.enums.GenericResponseCodes;
import com.oneassist.serviceplatform.commons.exception.BusinessServiceException;
import com.oneassist.serviceplatform.commons.exception.InputValidationException;
import com.oneassist.serviceplatform.commons.repositories.PincodeServiceRepository;
import com.oneassist.serviceplatform.commons.specifications.ServiceHubMasterSpecifications;
import com.oneassist.serviceplatform.commons.utils.ServiceRequestHelper;
import com.oneassist.serviceplatform.commons.validators.HubAllocationRequestValidator;
import com.oneassist.serviceplatform.contracts.dtos.ErrorInfoDto;
import com.oneassist.serviceplatform.contracts.dtos.hub.HubAllocationDto;
import com.oneassist.serviceplatform.contracts.dtos.hub.HubAllocationRequestDto;
import com.oneassist.serviceplatform.contracts.dtos.hub.HubAllocationSearchReasultDto;
import com.oneassist.serviceplatform.contracts.dtos.hub.HubAllocationSearchRequestDto;
import com.oneassist.serviceplatform.contracts.response.base.PageResponseDto;
import com.oneassist.serviceplatform.externalcontracts.PincodeMasterDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class HubServiceImpl implements IHubService {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private PinCodeMasterCache pinCodeMasterCache;

    @Autowired
    private PincodeServiceRepository pincodeServiceRepository;

    @Autowired
    private HubAllocationRequestValidator hubAllocationRequestValidator;

    @Autowired
    private HubMasterCache hubMasterCache;

    @Autowired
    private ServiceRequestHelper serviceRequestHelper;

    @Override
    public HubAllocationDto getHubByPincodeAndMake(HubAllocationDto hubAllocationRequestDto) throws BusinessServiceException {

        List<ErrorInfoDto> errorInfoList = hubAllocationRequestValidator.doValidateHubAllocationRequest(hubAllocationRequestDto);

        if (null != errorInfoList && errorInfoList.size() > 0) {

            throw new BusinessServiceException(GenericResponseCodes.VALIDATION_FAIL.getErrorCode(), errorInfoList, new InputValidationException());
        }

        PincodeMasterDto pincodeMasterDto = pinCodeMasterCache.get(hubAllocationRequestDto.getPincode());
        hubAllocationRequestDto.setStateCode(pincodeMasterDto.getStateCode());

        RuleEngine.execute(RuleName.HUB_ALLOCATION_RULE, hubAllocationRequestDto);

        // if hubid is not set by the drool rule, then execute the existing fallback logic
        if (hubAllocationRequestDto.getHubId() == null) {
            PincodeServiceEntity pincodeServiceEntity = pincodeServiceRepository.findPincodeServiceByPincodeAndStatus(hubAllocationRequestDto.getPincode(), Constants.ACTIVE);

            if (pincodeServiceEntity != null) {
                hubAllocationRequestDto.setHubId(pincodeServiceEntity.getHubId() != null ? pincodeServiceEntity.getHubId().intValue() : null);
                hubAllocationRequestDto.setPincodeCategory(pincodeServiceEntity.getPincodeCategory());
            }
        }

        return hubAllocationRequestDto;
    }

    @Override
    public void editHubAllocation(HubAllocationRequestDto hubAllocationRequest) {
        List<ErrorInfoDto> errorInfoList = hubAllocationRequestValidator.doValidateHubAllocationRequest(hubAllocationRequest);
        if (!CollectionUtils.isEmpty(errorInfoList)) {
            throw new BusinessServiceException(GenericResponseCodes.VALIDATION_FAIL.getErrorCode(), errorInfoList, new InputValidationException());
        }
        List<PincodeServiceEntity> pincodeServiceEntities = new ArrayList<PincodeServiceEntity>();
        for (HubAllocationDto hubAllocation : hubAllocationRequest.getHubAllocations()) {
            PincodeServiceEntity pincodeServiceEntity = pincodeServiceRepository.findPincodeServiceByPincodeAndStatus(hubAllocation.getPincode(), Constants.ACTIVE);

            if (pincodeServiceEntity != null && pincodeServiceEntity.getHubId().longValue() != Long.valueOf(hubAllocation.getHubId()).longValue()) {
                pincodeServiceEntity.setHubId(Long.valueOf(hubAllocation.getHubId()));
                pincodeServiceEntity.setModifiedBy(hubAllocationRequest.getModifiedBy());
                pincodeServiceEntity.setModifiedOn(new Date());
                pincodeServiceEntities.add(pincodeServiceEntity);
            }
        }
        pincodeServiceRepository.save(pincodeServiceEntities);
    }

    @SuppressWarnings("unchecked")
    @Override
    public PageResponseDto<List<HubAllocationSearchReasultDto>> getHubAllocation(HubAllocationSearchRequestDto hubAllocationSearchRequestDto, PageRequest pageRequest) {
        PageResponseDto<List<HubAllocationSearchReasultDto>> searchResult = new PageResponseDto<List<HubAllocationSearchReasultDto>>();
        List<PincodeServiceEntity> pincodeServiceEntities = null;
        List<String> pincodes = serviceRequestHelper.filterPincodes(hubAllocationSearchRequestDto.getPincodes(), hubAllocationSearchRequestDto.getStates(), hubAllocationSearchRequestDto.getCities());
        hubAllocationSearchRequestDto.setPincodes(pincodes);
        Page<PincodeServiceEntity> page = pincodeServiceRepository.findAll(ServiceHubMasterSpecifications.filterServiceHubMaster(hubAllocationSearchRequestDto), pageRequest);
        if (page != null) {
            pincodeServiceEntities = page.getContent();
            searchResult.setTotalElements(page.getTotalElements());
            searchResult.setTotalPages(page.getTotalPages());
            if (!CollectionUtils.isEmpty(pincodeServiceEntities)) {
                searchResult.setData(populateAllocationSearchResult(pincodeServiceEntities));
            } else {
                searchResult.setData(new ArrayList<HubAllocationSearchReasultDto>());
            }
        }
        return searchResult;
    }

    private List<HubAllocationSearchReasultDto> populateAllocationSearchResult(List<PincodeServiceEntity> pincodeServiceEntities) {
        List<HubAllocationSearchReasultDto> searchResult = new ArrayList<HubAllocationSearchReasultDto>();
        for (PincodeServiceEntity pincodeServiceEntity : pincodeServiceEntities) {
            HubAllocationSearchReasultDto searchResultDto = new HubAllocationSearchReasultDto();
            PincodeMasterDto pincodeMaster = pinCodeMasterCache.get(pincodeServiceEntity.getPincode());
            if (pincodeMaster != null) {
                searchResultDto.setCity(pincodeMaster.getCityName());
                searchResultDto.setHubId(pincodeServiceEntity.getHubId());
                searchResultDto.setModifiedOn(pincodeServiceEntity.getModifiedOn());
                searchResultDto.setPincode(Long.valueOf(pincodeServiceEntity.getPincode()));
                searchResultDto.setState(pincodeMaster.getStateName());
                searchResultDto.setHubName(hubMasterCache.get(String.valueOf(pincodeServiceEntity.getHubId())).getOchmHubName());
                searchResult.add(searchResultDto);
            }
        }
        return searchResult;
    }

}
