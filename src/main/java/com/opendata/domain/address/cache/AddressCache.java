package com.opendata.domain.address.cache;

import com.opendata.domain.address.entity.Address;
import com.opendata.domain.address.repository.AddressRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AddressCache {
    private final AddressRepository addressRepository;
    private Map<String, Address> cache;

    @PostConstruct
    public void init() {
        cache = new ConcurrentHashMap<>(addressRepository.findAll().stream()
                .collect(Collectors.toMap(Address::getAddressKorNm, Function.identity())));
    }

    public Address getByKorName(String name) {
        return cache.get(name);
    }
}
