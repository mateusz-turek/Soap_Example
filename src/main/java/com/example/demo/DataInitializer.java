package com.example.demo;

import com.example.demo.model.Case;
import com.example.demo.model.Employee;
import com.example.demo.repository.CaseRepository;
import com.example.demo.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(DataInitializer.class);
    private final EmployeeRepository employeeRepository;
    private final CaseRepository caseRepository;

    public void initData(){
        Case c = Case.builder()
                .message("broken phone")
                .status("new")
                .assignedTo("tommy")
                .createdAt(LocalDate.of(1990,01,01))
                .build();
        caseRepository.save(c);
        Employee e1 = Employee.builder()
                .firstName("sampleName")
                .lastName("sampleLastname")
                .birthDate(LocalDate.of(1990,01,01))
                .job("sampleJob")
                .build();
        Employee e2 = Employee.builder()
                .firstName("sampleName")
                .lastName("sampleLastname")
                .birthDate(LocalDate.of(1990,01,01))
                .job("sampleJob")
                .build();
        Employee e3 = Employee.builder()
                .firstName("sampleName")
                .lastName("sampleLastname")
                .birthDate(LocalDate.of(1990,01,01))
                .job("sampleJob")
                .build();
        employeeRepository.saveAll(Arrays.asList(e1,e2,e3));
        LOG.info("Data initialized");
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        initData();
    }
}
