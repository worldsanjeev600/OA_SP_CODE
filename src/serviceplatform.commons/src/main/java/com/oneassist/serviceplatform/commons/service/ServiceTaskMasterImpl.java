package com.oneassist.serviceplatform.commons.service;

import java.util.List;
import com.google.common.base.Strings;
import com.oneassist.serviceplatform.commons.constants.Constants;
import com.oneassist.serviceplatform.commons.entities.ServiceTaskEntity;
import com.oneassist.serviceplatform.commons.repositories.ServiceTaskRepository;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ServiceTaskDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceTaskMasterImpl implements IServiceTaskMasterService {

    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private ServiceTaskRepository serviceTaskRepository;

    @Override
    public List<ServiceTaskDto> getAllServiceTaskConfig() {

        List<ServiceTaskEntity> serviceTaskList = serviceTaskRepository.findByStatus(Constants.ACTIVE);

        List<ServiceTaskDto> serviceTaskDtoList = modelMapper.map(serviceTaskList, new TypeToken<List<ServiceTaskDto>>() {
        }.getType());

        return serviceTaskDtoList;
    }

    @Override
    public ServiceTaskDto getServiceTaskByTaskId(String serviceTaskId) {

        ServiceTaskEntity serviceTaskEntity = null;
        ServiceTaskDto serviceTaskDto = null;

        if (!Strings.isNullOrEmpty(serviceTaskId)) {
            serviceTaskEntity = serviceTaskRepository.findServiceTaskEntityByServiceTaskId(Long.valueOf(serviceTaskId));
        }

        if (serviceTaskEntity != null) {
            serviceTaskDto = modelMapper.map(serviceTaskEntity, ServiceTaskDto.class);
        }

        return serviceTaskDto;
    }

    @Override
    public List<ServiceTaskDto> getServiceTaskByTaskType(String serviceTaskType) {

        List<ServiceTaskEntity> serviceTaskEntityList = null;
        List<ServiceTaskDto> serviceTaskDtos = null;

        if (!Strings.isNullOrEmpty(serviceTaskType)) {
            serviceTaskEntityList = serviceTaskRepository.findByTaskTypeAndStatus(serviceTaskType, Constants.ACTIVE);
        }

        if (serviceTaskEntityList != null) {
            serviceTaskDtos = modelMapper.map(serviceTaskEntityList, new TypeToken<List<ServiceTaskDto>>() {
            }.getType());
        }

        return serviceTaskDtos;
    }
}
