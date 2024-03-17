package com.qred.payment.util;

import com.qred.payment.dto.PaymentRecord;
import com.qred.payment.dto.PaymentType;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.MathContext;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

@Component
public class CsvHelper {

  CSVFormat format = CSVFormat.DEFAULT.withFirstRecordAsHeader().withDelimiter(',');
  MathContext mc = new MathContext(2);

  public Flux<PaymentRecord> parseCsvFile(FilePart file) {
    // Convert the FilePart content to a single String
    Mono<String> contentMono = file.content()
            .reduce(new StringBuilder(), (sb, dataBuffer) -> sb.append(StandardCharsets.UTF_8.decode(dataBuffer.toByteBuffer())))
            .map(StringBuilder::toString);

    // Parse the CSV content and emit PaymentRecord objects
    return contentMono.flatMapMany(content -> {
      try {
        CSVParser parser = CSVParser.parse(content, format);
        return Flux.fromIterable(parser.getRecords()).map(this::toPaymentRecord);
      } catch (Exception e) {
        return Flux.error(e);
      }
    });
  }

  private PaymentRecord toPaymentRecord(CSVRecord record) {
    PaymentRecord paymentRecord = new PaymentRecord();
    paymentRecord.setPaymentDate(LocalDate.parse(record.get("payment_date")));
    paymentRecord.setAmount(new BigDecimal(record.get("amount")).abs(mc));
    paymentRecord.setPaymentType(PaymentType.fromString(record.get("type")));
    paymentRecord.setContractNumber(record.get("contract_number"));
    return paymentRecord;
  }

}
