package com.opendata.domain.address.repository;

import com.opendata.domain.address.entity.Address;

public interface CustomAddressRepository {
    Address findByAddressKorNm(String addressKorNm);
}
