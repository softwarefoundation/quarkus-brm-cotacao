package com.softwarefoundation.message;

import com.softwarefoundation.dto.QuotationDTO;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Slf4j
@ApplicationScoped
public class KafkaEvents {

    private final Logger LOG = LoggerFactory.getLogger(KafkaEvents.class);

    @Channel("quotation-channel")
    Emitter<QuotationDTO> quotationResquestEmmiter;

    public void sendNewKafkaEvent(QuotationDTO quotationDTO) {
        LOG.info("Enviando cotação para o topico Kafka");
        this.quotationResquestEmmiter.send(quotationDTO).toCompletableFuture();
    }

}
