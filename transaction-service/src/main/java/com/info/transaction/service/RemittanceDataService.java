package com.info.transaction.service;

import com.info.dto.remittance.RemittanceDataDTO;
import com.info.transaction.exception.InvalidRequestException;
import com.info.transaction.feignClient.InstantCashServiceFeignClient;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor
@Component("remittanceDataService")
public class RemittanceDataService implements JavaDelegate {

    private static final Logger logger = LoggerFactory.getLogger(RemittanceDataService.class.getName());
    private final InstantCashServiceFeignClient instantCashService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String reference = (String) execution.getVariable("reference");
        logger.info("RemittanceDataService for reference number: {}", reference);
        RemittanceDataDTO remittanceDataDTO = instantCashService.findByReference(reference);
        logger.info("Transaction RemittanceDataDTO: {}", remittanceDataDTO);
        if (remittanceDataDTO == null) {
            throw new BpmnError("PAYMENT_FAILED", "Transaction Posting failed!");
        }
    }
}