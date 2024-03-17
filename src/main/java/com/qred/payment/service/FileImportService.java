package com.qred.payment.service;

import com.qred.payment.dto.PaymentRecord;
import com.qred.payment.model.Payment;
import com.qred.payment.repository.PaymentRepository;
import com.qred.payment.util.CsvHelper;
import com.qred.payment.util.XmlHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.web.reactive.function.UnsupportedMediaTypeException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class FileImportService {

  private final PaymentRepository paymentRepository;
  private final TransactionalOperator transactionalOperator;
  private final CsvHelper csvHelper;
  private final XmlHelper xmlHelper;

  @Autowired
  public FileImportService(PaymentRepository paymentRepository, TransactionalOperator transactionalOperator, CsvHelper csvHelper, XmlHelper xmlHelper) {
    this.paymentRepository = paymentRepository;
    this.transactionalOperator = transactionalOperator;
    this.csvHelper = csvHelper;
    this.xmlHelper = xmlHelper;
  }

  public Mono<Void> processFile(FilePart file) {
    String filename = file.filename();
    Flux<PaymentRecord> paymentRecords;

    if (filename.endsWith(".csv")) {
      paymentRecords = csvHelper.parseCsvFile(file);
    } else if (filename.endsWith(".xml")) {
      paymentRecords = xmlHelper.parseXmlFile(file);
    } else {
      return Mono.error(new UnsupportedMediaTypeException("Unsupported file type"));
    }

    return processPayments(paymentRecords);
  }

  @Transactional
  public Mono<Void> processPayments(Flux<PaymentRecord> paymentRecords) {
    return paymentRecords.concatMap(this::createAndSavePayment)
            .as(transactionalOperator::transactional) // Apply transactional operator
            .then();
  }

  private Mono<Payment> createAndSavePayment(PaymentRecord record) {
    Payment payment = Payment.builder()
            .paymentDate(record.getPaymentDate())
            .amount(record.getAmount())
            .paymentType(record.getPaymentType())
            .contractNumber(record.getContractNumber())
            .build();
    return paymentRepository.save(payment);
  }

}
