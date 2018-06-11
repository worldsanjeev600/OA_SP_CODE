package com.oneassist.serviceplatform.services.servicerequest;

import java.text.ParseException;
import java.util.Map;

import org.springframework.context.NoSuchMessageException;

import com.oneassist.serviceplatform.commons.exception.BusinessServiceException;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.DashboardDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ServiceRequestSearchDto;
import com.oneassist.serviceplatform.contracts.dtos.shipment.DashBoardInfoDto;
import com.oneassist.serviceplatform.contracts.dtos.shipment.DashBoardRequestDto;
import com.oneassist.serviceplatform.contracts.response.base.ResponseDto;

public interface IServiceRequestReportService {

    public DashboardDto getDashboardCountDetails() throws BusinessServiceException;

    public DashBoardInfoDto getDashBoardInfo(DashBoardRequestDto dashBoardRequestDto) throws ParseException, NoSuchMessageException, BusinessServiceException;
}
