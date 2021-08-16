package com.example.demo.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

@EnableWs
@Configuration
public class SoapWSConfig2 {
    public static final String CASE_NAMESPACE = "http://sri4/cases";

    @Bean(name = "cases")
    public DefaultWsdl11Definition defaultWsdl11Definition(@Qualifier("casesSchema") XsdSchema caseSchema){
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("CasesPort");
        wsdl11Definition.setLocationUri("/ws/cases");
        wsdl11Definition.setTargetNamespace(CASE_NAMESPACE);
        wsdl11Definition.setSchema(caseSchema);
        return wsdl11Definition;
    }

    @Bean
    public XsdSchema casesSchema(){
        return new SimpleXsdSchema(new ClassPathResource("cases.xsd"));
    }
}
