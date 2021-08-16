package com.example.demo.endpoint;

import com.example.demo.config.SoapWSConfig;
import com.example.demo.config.SoapWSConfig2;
import com.example.demo.model.Case;
import com.example.demo.model.Employee;
import com.example.demo.repository.CaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import soap.employees.*;
import sri4.cases.*;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Endpoint
@RequiredArgsConstructor
public class CaseEndpoint {

    private final CaseRepository caseRepository;

    @PayloadRoot(namespace = SoapWSConfig2.CASE_NAMESPACE, localPart = "getCasesRequest")
    @ResponsePayload
    public GetCasesResponse getCases(@RequestPayload GetCasesRequest req){
        List<Case> caseList =  caseRepository.findAll();
        List<CaseDto> dtoList = caseList.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        GetCasesResponse res = new GetCasesResponse();
        res.getCases().addAll(dtoList);
        return res;
    }

    @PayloadRoot(namespace = SoapWSConfig2.CASE_NAMESPACE, localPart = "getCaseByIdRequest")
    @ResponsePayload
    public GetCaseByIdResponse getCasesById(@RequestPayload GetCaseByIdRequest req){
        Long caseId = req.getCaseId().longValue();
        Optional<Case> caseOptional = caseRepository.findById(caseId);
        GetCaseByIdResponse res = new GetCaseByIdResponse();
        res.setCase(convertToDto(caseOptional.orElse(null)));
        return res;
    }

    @PayloadRoot(namespace = SoapWSConfig2.CASE_NAMESPACE, localPart = "addCaseRequest")
    @ResponsePayload
    public AddCaseResponse addCase(@RequestPayload AddCaseRequest req){
        CaseDto caseDto = req.getCase();
        Case c = convertToEntity(caseDto);
        System.out.println(c);
        caseRepository.save(c);
        AddCaseResponse response = new AddCaseResponse();
        response.setCaseId(new BigDecimal(c.getId()));
        return response;
    }

    @PayloadRoot(namespace = SoapWSConfig2.CASE_NAMESPACE, localPart = "getCaseByStatusRequest")
    @ResponsePayload
    public GetCaseByStatusResponse getCaseByStatusResponse(@RequestPayload GetCaseByStatusRequest request){
        String caseStatus = request.getCaseStatus();
        List<Case> c = caseRepository.findByStatus(caseStatus);
        List<CaseDto> dtoList = c.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        GetCaseByStatusResponse response = new GetCaseByStatusResponse();
        response.getCases().addAll(dtoList);
        return response;
    }

    @PayloadRoot(namespace = SoapWSConfig2.CASE_NAMESPACE, localPart = "getCaseByAssignedToRequest")
    @ResponsePayload
    public GetCaseByAssignedToResponse getCaseByAssignedToResponse(@RequestPayload GetCaseByAssignedToRequest request){
        String assignedTo = request.getAssignedTo();
        List<Case> c = caseRepository.findByAssignedTo(assignedTo);
        List<CaseDto> dtoList = c.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        GetCaseByAssignedToResponse response = new GetCaseByAssignedToResponse();
        response.getCases().addAll(dtoList);
        return response;
    }

    private CaseDto convertToDto (Case c){
        if(c == null){
            return null;
        }
        try {
            CaseDto dto = new CaseDto();
            dto.setId(new BigDecimal(c.getId()));
            dto.setStatus(c.getStatus());
            dto.setAssignedTo(c.getAssignedTo());
            XMLGregorianCalendar createdAt = DatatypeFactory.newInstance().newXMLGregorianCalendar(c.getCreatedAt().toString());
            System.out.println(createdAt);
            dto.setCreatedAt(createdAt);
            dto.setMessage(c.getMessage());
            return dto;
        } catch (DatatypeConfigurationException datatypeConfigurationException){
            datatypeConfigurationException.printStackTrace();
            return null;
        }
    }

    private Case convertToEntity(CaseDto dto){
        LocalDate localDate = LocalDate.now();
        return Case.builder()
                .id(dto.getId() != null ? dto.getId().longValue() : null)
                .assignedTo(dto.getAssignedTo())
                .createdAt(localDate)
                .status(dto.getStatus())
                .message(dto.getMessage())
                .build();
    }
}
