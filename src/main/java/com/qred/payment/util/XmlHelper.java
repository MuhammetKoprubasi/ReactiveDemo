package com.qred.payment.util;

import com.qred.payment.dto.PaymentRecord;
import com.qred.payment.dto.PaymentType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.MathContext;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

@Component
public class XmlHelper {

  MathContext mc = new MathContext(2);

  public Flux<PaymentRecord> parseXmlFile(FilePart file) {
    Mono<String> contentMono = file.content()
            .reduce(new StringBuilder(), (sb, dataBuffer) -> sb.append(StandardCharsets.UTF_8.decode(dataBuffer.asByteBuffer())))
            .map(StringBuilder::toString);

    return contentMono.flatMapMany(content -> {
      try {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader reader = factory.createXMLStreamReader(new StringReader(content));

        return Flux.create(sink -> {
          try {
            String currentElement = null; // Track the current element
            PaymentRecord paymentRecord = null;
            while (reader.hasNext()) {
              int event = reader.next();
              switch (event) {
                case XMLStreamConstants.START_ELEMENT:
                  currentElement = reader.getLocalName();
                  if ("payment".equals(currentElement)) {
                    paymentRecord = new PaymentRecord();
                  }
                  break;
                case XMLStreamConstants.CHARACTERS:
                  if (!reader.isWhiteSpace() && currentElement != null && paymentRecord != null) {
                    String text = reader.getText();
                    switch (currentElement) {
                      case "payment_date":
                        paymentRecord.setPaymentDate(LocalDate.parse(text));
                        break;
                      case "amount":
                        paymentRecord.setAmount(new BigDecimal(text).abs(mc));
                        break;
                      case "type":
                        paymentRecord.setPaymentType(PaymentType.fromString(text));
                        break;
                      case "contract_number":
                        paymentRecord.setContractNumber(text);
                        break;
                    }
                  }
                  break;
                case XMLStreamConstants.END_ELEMENT:
                  if ("payment".equals(reader.getLocalName())) {
                    sink.next(paymentRecord);
                    currentElement = null; // Reset after processing a payment
                  }
                  break;
              }
            }
            sink.complete();
          } catch (Exception e) {
            sink.error(e);
          }
        });
      } catch (Exception e) {
        return Flux.error(e);
      }
    });
  }
}
