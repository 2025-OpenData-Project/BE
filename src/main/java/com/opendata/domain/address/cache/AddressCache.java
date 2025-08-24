package com.opendata.domain.address.cache;

import com.opendata.domain.address.entity.Address;
import com.opendata.domain.address.entity.Area;
import com.opendata.domain.address.repository.AddressRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AddressCache {

    private final AddressRepository addressRepository;

    private Map<String, Address> byKorName;

    private Map<String, List<Address>> byAreaId;

    @PostConstruct
    public void init() {
        // 1) 전체 Address 로드
        List<Address> all = addressRepository.findAll();

        // 2) 한글명 캐시
        byKorName = all.stream()
                .filter(a -> a.getAddressKorNm() != null)
                .collect(Collectors.toConcurrentMap(
                        Address::getAddressKorNm,
                        Function.identity(),
                        (a, b) -> a
                ));

        // 3) area_id 캐시 (area_id → List<Address>)
        byAreaId = all.stream()
                .filter(a -> a.getArea() != null)
                .collect(Collectors.groupingByConcurrent(
                        a -> extractAreaId(a.getArea())
                ));
    }

    private String extractAreaId(Area area) {
        return area.getAreaCodeId().toString();
    }

    public Address getByKorName(String name) {
        return byKorName.get(name);
    }
    public List<Address> getByAreaId(String areaId) {
        return byAreaId.getOrDefault(areaId, Collections.emptyList());
    }

    public List<Address> getAll() {
        return new ArrayList<>(byKorName.values());
    }
}