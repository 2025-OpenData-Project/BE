package com.opendata.domain.address.repository;

import com.opendata.domain.address.entity.Address;
import com.opendata.domain.address.entity.QAddress;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomAddressRepositoryImpl implements CustomAddressRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public Address findByAddressKorNm(String addressKorNm) {
        QAddress qAddress = QAddress.address;
        return queryFactory.selectFrom(qAddress)
                .where(qAddress.addressKorNm.eq(addressKorNm))
                .fetchFirst();
    }
}
