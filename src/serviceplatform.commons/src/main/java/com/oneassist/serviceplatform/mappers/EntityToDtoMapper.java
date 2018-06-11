package com.oneassist.serviceplatform.mappers;

import org.springframework.stereotype.Service;

import com.oneassist.serviceplatform.contracts.dtos.servicerequest.AssetDetailDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.asset.ServiceRequestAssetResponseDto;

@Service
public class EntityToDtoMapper {

	public void mapAssetDetailDtoToServiceRequestAssetResponseDto(
			ServiceRequestAssetResponseDto srAssetResponseDto, AssetDetailDto assetDetail) {
		
		srAssetResponseDto.setProductCode(assetDetail.getProdCode());
		srAssetResponseDto.setMake(assetDetail.getBrand());
		srAssetResponseDto.setModelNo(assetDetail.getModel());
		srAssetResponseDto.setSerialNo(assetDetail.getSerialNo());

	}
}
