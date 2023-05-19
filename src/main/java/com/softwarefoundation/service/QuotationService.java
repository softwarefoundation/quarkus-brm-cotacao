package com.softwarefoundation.service;

import com.softwarefoundation.client.CurrencyPriceClient;
import com.softwarefoundation.dto.CurrencyPriceDTO;
import com.softwarefoundation.dto.QuotationDTO;
import com.softwarefoundation.entity.QuotationEntity;
import com.softwarefoundation.enums.CurrencyPairEnum;
import com.softwarefoundation.message.KafkaEvents;
import com.softwarefoundation.repository.QuotationRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;

@ApplicationScoped
public class QuotationService {

    @Inject
    @RestClient
    CurrencyPriceClient currencyPriceClient;
    @Inject
    QuotationRepository quotationRepository;
    @Inject
    KafkaEvents kafkaEvents;

    public void getCurrencyPricce() {
        CurrencyPriceDTO currencyPriceInfo = currencyPriceClient.getPriceByPair(CurrencyPairEnum.USD_BRL.getPair());

        if (updateCurrentInfoPrice(currencyPriceInfo)) {
            kafkaEvents.sendNewKafkaEvent(QuotationDTO.builder().currencyPrice(new BigDecimal(currencyPriceInfo.getUsdbrl().getBid())).date(new Date()).build());
        }
    }

    private boolean updateCurrentInfoPrice(CurrencyPriceDTO currencyPriceInfo) {
        BigDecimal currentPrice = new BigDecimal(currencyPriceInfo.getUsdbrl().getBid());
        AtomicBoolean updatePrice = new AtomicBoolean(false);
        LinkedList<QuotationEntity> quotationList = new LinkedList<>(quotationRepository.findAll().list());
        if (quotationList.isEmpty()) {
            saveQuotation(currencyPriceInfo);
            updatePrice.set(true);
        } else {
            QuotationEntity lastDollarPrice = quotationList.getLast();
            if (currentPrice.floatValue() > lastDollarPrice.getCurrencyPrice().floatValue()) {
                saveQuotation(currencyPriceInfo);
                updatePrice.set(true);
            }
        }
        return updatePrice.get();
    }

    private void saveQuotation(CurrencyPriceDTO currencyPriceInfo) {
        QuotationEntity quotation = new QuotationEntity();
        quotation.setDate(new Date());
        quotation.setCurrencyPrice(new BigDecimal(currencyPriceInfo.getUsdbrl().getBid()));
        quotation.setPctChange(currencyPriceInfo.getUsdbrl().getPctChange());
        quotation.setPair(CurrencyPairEnum.USD_BRL.getPair());
        quotationRepository.persist(quotation);
    }

}
