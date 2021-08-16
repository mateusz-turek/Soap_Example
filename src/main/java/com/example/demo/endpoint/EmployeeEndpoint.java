package com.example.demo.endpoint;

import com.example.demo.config.SoapWSConfig;
import com.example.demo.model.Employee;
import com.example.demo.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import soap.employees.*;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Endpoint
@RequiredArgsConstructor
public class EmployeeEndpoint {

    private final EmployeeRepository employeeRepository;

    @PayloadRoot(namespace = SoapWSConfig.EMPLOYEE_NAMESPACE, localPart = "getEmployeesRequest")
    @ResponsePayload
    public GetEmployeesResponse getEmployees(@RequestPayload GetEmployeesRequest req){
        List<Employee> employeeList =  employeeRepository.findAll();
        List<EmployeeDto> dtoList = employeeList.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        GetEmployeesResponse res = new GetEmployeesResponse();
        res.getEmployees().addAll(dtoList);
        return res;
    }

    @PayloadRoot(namespace = SoapWSConfig.EMPLOYEE_NAMESPACE, localPart = "getEmployeeByIdRequest")
    @ResponsePayload
    public GetEmployeeByIdResponse getEmployeeById(@RequestPayload GetEmployeeByIdRequest req){
        Long employeId = req.getEmployeeId().longValue();
        Optional<Employee> employee = employeeRepository.findById(employeId);
        GetEmployeeByIdResponse res = new GetEmployeeByIdResponse();
        res.setEmployee(convertToDto(employee.orElse(null)));
        return res;
    }

    @PayloadRoot(namespace = SoapWSConfig.EMPLOYEE_NAMESPACE, localPart = "addEmployeeRequest")
    @ResponsePayload
    public AddEmployeeResponse addEmployee(@RequestPayload AddEmployeeRequest req){
        EmployeeDto employeeDto = req.getEmployee();
        Employee employee = convertToEntity(employeeDto);
        employeeRepository.save(employee);
        AddEmployeeResponse response = new AddEmployeeResponse();
        response.setEmployeeId(new BigDecimal(employee.getId()));
        return response;
    }

    private EmployeeDto convertToDto (Employee e){
        if(e == null){
            return null;
        }
        try {
            EmployeeDto dto = new EmployeeDto();
            dto.setId(new BigDecimal(e.getId()));
            dto.setFirstName(e.getFirstName());
            dto.setLastName(e.getLastName());
            XMLGregorianCalendar birthDate = null;
            birthDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(e.getBirthDate().toString());
            dto.setBirthDate(birthDate);
            dto.setJob(e.getJob());
            return dto;
        } catch (DatatypeConfigurationException datatypeConfigurationException){
            datatypeConfigurationException.printStackTrace();
            return null;
        }
    }

    private Employee convertToEntity(EmployeeDto dto){
        return Employee.builder()
                .id(dto.getId() != null ? dto.getId().longValue() : null)
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .job(dto.getJob())
                .build();
    }
}
