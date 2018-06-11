package com.oneassist.serviceplatform.commons.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oneassist.serviceplatform.commons.entities.GenericKeySetEntity;

public interface GenericKeySetRepository extends JpaRepository <GenericKeySetEntity, Long>{
	
	//@Query(value = "select MST.KEYSET_ID ,MST.KEYSET_DESCRIPTION,DTL.KEY,DTL.VALUE from OA_GENERIC_KEYSET_MST MST inner join OA_GENERIC_KEYSET_VALUE_DTL DTL on MST.KEYSET_ID=DTL.KEYSET_ID where MST.KEYSET_NAME =:keySetName AND MST.STATUS=:status",nativeQuery=true)
	//List<Object[]> getKetSetDetailsByKeySetName(@Param("keySetName")String keySetName,@Param("status")String status);
	List<GenericKeySetEntity> findByKeySetNameAndStatus(String keySetName,String status);

	List<GenericKeySetEntity> findByStatus(String active);
}
