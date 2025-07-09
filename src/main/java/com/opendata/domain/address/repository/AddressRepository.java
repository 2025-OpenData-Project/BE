package com.opendata.domain.address.repository;

import com.opendata.domain.address.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long>, CustomAddressRepository {
    Address findByAddressKorNm(String addressKorNm);
}
