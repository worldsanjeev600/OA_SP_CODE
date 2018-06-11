package com.oneassist.serviceplatform.services.generickeyset;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.oneassist.serviceplatform.commons.constants.Constants;
import com.oneassist.serviceplatform.commons.entities.GenericKeySetEntity;
import com.oneassist.serviceplatform.commons.entities.GenericKeySetValueEntity;
import com.oneassist.serviceplatform.commons.enums.GenericResponseCodes;
import com.oneassist.serviceplatform.commons.exception.BusinessServiceException;
import com.oneassist.serviceplatform.commons.exception.InputValidationException;
import com.oneassist.serviceplatform.commons.mastercache.GenericKeySetCache;
import com.oneassist.serviceplatform.commons.validators.GenericKeySetRequestValidator;
import com.oneassist.serviceplatform.contracts.dtos.ErrorInfoDto;
import com.oneassist.serviceplatform.contracts.dtos.generickeyset.GenericKeySetDetailDto;
import com.oneassist.serviceplatform.contracts.dtos.generickeyset.GenericKeySetRequestDto;

public class GenericKeySetServiceImpl implements IGenericKeySetService {

	@Autowired
	private GenericKeySetRequestValidator genericKeySetRequestValidator;

	@Autowired
	private GenericKeySetCache genericKeySetCache;

	@Override
	public List<GenericKeySetDetailDto> getGenericKeySetDetail(GenericKeySetRequestDto genericKeySetRequestDto) throws BusinessServiceException {

		List<GenericKeySetDetailDto> listData = null;
		List<ErrorInfoDto> errorInfoList = genericKeySetRequestValidator.doValidateGenericKeySetRequest(genericKeySetRequestDto);

		if (null != errorInfoList 
				&& errorInfoList.size() > 0) {

			throw new BusinessServiceException(GenericResponseCodes.VALIDATION_FAIL.getErrorCode(), errorInfoList, new InputValidationException());
		} else {

			listData = new ArrayList<GenericKeySetDetailDto>();
			String [] keySetNames = genericKeySetRequestDto.getKeySetName().split(",");
			for (String keySetName : keySetNames) {
				GenericKeySetEntity genericKeySetEntity = genericKeySetCache.get(keySetName);
				if(genericKeySetEntity != null){
					List<GenericKeySetValueEntity> valueEntities = genericKeySetEntity.getGenericKeySetValueDetails();

					if (!CollectionUtils.isEmpty(valueEntities)) {

						for (GenericKeySetValueEntity valueEntity : valueEntities) {

							if (Constants.ACTIVE.equalsIgnoreCase(valueEntity.getStatus())) {
								GenericKeySetDetailDto genericKeySetDetailDto = new GenericKeySetDetailDto();
								genericKeySetDetailDto.setKeySetDescription(genericKeySetEntity.getKeySetDescription());
								genericKeySetDetailDto.setKeySetId(genericKeySetEntity.getKeySetId());
								genericKeySetDetailDto.setKeySetName(genericKeySetEntity.getKeySetName());
								genericKeySetDetailDto.setCreatedOn(genericKeySetEntity.getCreatedOn());
								genericKeySetDetailDto.setModifiedOn(genericKeySetEntity.getModifiedOn());
								genericKeySetDetailDto.setCreatedBy(genericKeySetEntity.getCreatedBy());
								genericKeySetDetailDto.setModifiedBy(genericKeySetEntity.getModifiedBy());
								genericKeySetDetailDto.setStatus(genericKeySetEntity.getStatus());
								genericKeySetDetailDto.setValueId(valueEntity.getValueId());
								genericKeySetDetailDto.setValue(valueEntity.getValue());
								genericKeySetDetailDto.setKey(valueEntity.getKey());

								listData.add(genericKeySetDetailDto);
							}
						}
					}
				}
			}

		}

		return listData;
	}
}