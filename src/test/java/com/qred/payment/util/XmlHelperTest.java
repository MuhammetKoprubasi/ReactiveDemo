package com.qred.payment.util;

import com.qred.payment.dto.PaymentRecord;
import com.qred.payment.dto.PaymentType;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

class XmlHelperTest {

  @Test
  void parseXmlFileTest() {
    XmlHelper xmlHelper = new XmlHelper();

    String xmlContent = """
            <payments>
                <payment>
                    <payment_date>2024-01-30</payment_date>
                    <amount>1000.00</amount>
                    <type>incoming</type>
                    <contract_number>12345</contract_number>
                </payment>
                <payment>
                    <payment_date>2024-01-31</payment_date>
                    <amount>-500.00</amount>
                    <type>outgoing</type>
                    <contract_number>54321</contract_number>
                </payment>
            </payments>
            """;

    // Mock FilePart and its content using DataBuffer
    DataBuffer dataBuffer = new DefaultDataBufferFactory().wrap(xmlContent.getBytes(StandardCharsets.UTF_8));
    FilePart filePartMock = Mockito.mock(FilePart.class);
    Mockito.when(filePartMock.content()).thenReturn(Flux.just(dataBuffer));

    // Test parseXmlFile method
    Flux<PaymentRecord> result = xmlHelper.parseXmlFile(filePartMock);

    // Verify results
    StepVerifier.create(result)
            .expectNextMatches(record -> record.getPaymentDate().equals(LocalDate.parse("2024-01-30"))
                    && record.getAmount().compareTo(new BigDecimal("1000.00")) == 0
                    && record.getPaymentType() == PaymentType.INCOMING
                    && record.getContractNumber().equals("12345"))
            .expectNextMatches(record -> record.getPaymentDate().equals(LocalDate.parse("2024-01-31"))
                    && record.getAmount().compareTo(new BigDecimal("500.00")) == 0  // Assuming your implementation uses .abs() for the amount
                    && record.getPaymentType() == PaymentType.OUTGOING
                    && record.getContractNumber().equals("54321"))
            .verifyComplete();
  }
}
