package com.oneassist.serviceplatform.contracts.response;

import com.oneassist.serviceplatform.externalcontracts.DocumentCheckListDto;

public class DocumentChecklistDataDto {

    private String checklistId;
    private DocumentCheckListDto checklistDto;

    public String getChecklistId() {
        return checklistId;
    }

    public void setChecklistId(String checklistId) {
        this.checklistId = checklistId;
    }

    public DocumentCheckListDto getChecklistDto() {
        return checklistDto;
    }

    public void setChecklistDto(DocumentCheckListDto checklistDto) {
        this.checklistDto = checklistDto;
    }

	@Override
	public String toString() {
		return "DocumentChecklistDataDto [checklistId=" + checklistId + ", checklistDto=" + checklistDto + "]";
	}
    
}
