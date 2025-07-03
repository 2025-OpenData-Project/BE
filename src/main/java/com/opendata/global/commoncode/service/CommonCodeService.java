package com.opendata.global.commoncode.service;

import com.opendata.global.commoncode.entity.CommonCode;
import com.opendata.global.commoncode.repository.CommonCodeRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommonCodeService {

    private final CommonCodeRepository codeRepository;

    private Map<Long, CommonCode> codeMapById;
    private Map<String, CommonCode> codeMapByName;

    @PostConstruct
    public void init() {
        List<CommonCode> codes = codeRepository.findAll();

        this.codeMapById = codes.stream()
                .collect(Collectors.toMap(CommonCode::getId, Function.identity()));

        this.codeMapByName = codes.stream()
                .collect(Collectors.toMap(CommonCode::getKorName, Function.identity()));
    }

    public CommonCode getById(Long id) {
        return codeMapById.get(id);
    }

    public CommonCode getByCodeNm(String codeNm) {
        return codeMapByName.get(codeNm);
    }

    public Long getIdByCodeNm(String codeNm) {
        CommonCode code = codeMapByName.get(codeNm);
        return (code != null) ? code.getId() : null;
    }
}