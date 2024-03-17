package com.qred.payment.service;

import com.qred.payment.dto.PaymentRecord;
import com.qred.payment.dto.PaymentType;
import com.qred.payment.model.Payment;
import com.qred.payment.repository.PaymentRepository;
import com.qred.payment.util.CsvHelper;
import com.qred.payment.util.XmlHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.web.reactive.function.UnsupportedMediaTypeException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FileImportServiceTest {

  @Mock
  private PaymentRepository paymentRepository;

  @Mock
  private CsvHelper csvHelper;

  @Mock
  private XmlHelper xmlHelper;

  @Mock
  private TransactionalOperator transactionalOperator;

  @Mock
  private FilePart csvFilePart;

  @Mock
  private FilePart xmlFilePart;

  @InjectMocks
  private FileImportService fileImportService;

  @Captor
  private ArgumentCaptor<Payment> paymentCaptor;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    // Simulate the transactional behavior by just passing through the publisher
    when(transactionalOperator.transactional(any(Flux.class))).thenAnswer(invocation -> invocation.getArgument(0));
    when(transactionalOperator.transactional(any(Mono.class))).thenAnswer(invocation -> invocation.getArgument(0));
  }

  @Test
  void testProcessCsvFile() {
    // Setup
    String filename = "test-valid.csv";
    when(csvFilePart.filename()).thenReturn(filename);
    PaymentRecord csvRecord = new PaymentRecord(LocalDate.parse("2024-01-30"), new BigDecimal("1000.00"), PaymentType.INCOMING, "12345");
    when(csvHelper.parseCsvFile(any(FilePart.class))).thenReturn(Flux.just(csvRecord));
    when(paymentRepository.save(any(Payment.class))).thenReturn(Mono.just(new Payment(1L, csvRecord.getPaymentDate(), csvRecord.getAmount(), csvRecord.getPaymentType(), csvRecord.getContractNumber())));

    // Execute
    Mono<Void> result = fileImportService.processFile(csvFilePart);

    // Verify
    StepVerifier.create(result)
            .verifyComplete();

    verify(paymentRepository).save(paymentCaptor.capture());
    Payment savedPayment = paymentCaptor.getValue();
    assertEquals(csvRecord.getPaymentDate(), savedPayment.getPaymentDate());
    assertEquals(0, csvRecord.getAmount().compareTo(savedPayment.getAmount()));
    assertEquals(csvRecord.getPaymentType(), savedPayment.getPaymentType());
    assertEquals(csvRecord.getContractNumber(), savedPayment.getContractNumber());
  }

  @Test
  void testProcessXmlFile() {
    when(xmlFilePart.filename()).thenReturn("test-invalid.xml");
    PaymentRecord xmlRecord = new PaymentRecord(LocalDate.parse("2024-02-15"), new BigDecimal("500.00"), PaymentType.OUTGOING, "67890");
    when(xmlHelper.parseXmlFile(any(FilePart.class))).thenReturn(Flux.just(xmlRecord));
    when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

    Mono<Void> result = fileImportService.processFile(xmlFilePart);

    StepVerifier.create(result).verifyComplete();

    // Verify save was called and capture the argument
    verify(paymentRepository, times(1)).save(paymentCaptor.capture());

    // Extract the captured Payment and assert its properties
    Payment savedPayment = paymentCaptor.getValue();

    assertEquals(LocalDate.parse("2024-02-15"), savedPayment.getPaymentDate());
    assertEquals(0, new BigDecimal("500.00").compareTo(savedPayment.getAmount()));
    assertEquals(PaymentType.OUTGOING, savedPayment.getPaymentType());
    assertEquals("67890", savedPayment.getContractNumber());
  }

  @Test
  void testProcessUnsupportedFileType() {
    FilePart unsupportedFilePart = org.mockito.Mockito.mock(FilePart.class);
    when(unsupportedFilePart.filename()).thenReturn("unsupported.txt");

    Mono<Void> result = fileImportService.processFile(unsupportedFilePart);

    StepVerifier.create(result)
            .expectError(UnsupportedMediaTypeException.class)
            .verify();
  }

}
