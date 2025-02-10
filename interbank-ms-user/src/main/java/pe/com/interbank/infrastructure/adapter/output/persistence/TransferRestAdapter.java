package pe.com.interbank.infrastructure.adapter.output.persistence;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pe.com.interbank.application.port.output.TransferPersistencePort;
import pe.com.interbank.domain.model.Transfer;
import pe.com.interbank.infrastructure.adapter.output.persistence.mapper.TransferPersistenceMapper;
import pe.com.interbank.infrastructure.adapter.output.persistence.repository.TransferRepository;
import pe.com.interbank.utils.Constants;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Component
@Slf4j
@RequiredArgsConstructor
public class TransferRestAdapter implements TransferPersistencePort {

    private final TransferRepository transferRepository;
    private final TransferPersistenceMapper mapper;

    @Override
    public Mono<Transfer> findById(String id) {
        return transferRepository.findById(id)
                .map(mapper::toTransfer);
    }

    @Override
    public Flux<Transfer> findAll() {
        return transferRepository.findAll()
                .map(mapper::toTransfer);
    }

    @Override
    public Mono<Transfer> save(Transfer transfer) {
        transfer.setCreatedDate(Constants.convertToLocalTimeZone(LocalDateTime.now()));
        return transferRepository.save(mapper.toTransferEntity(transfer))
                .doOnError(error -> log.error(Constants.ERROR_SAVING, "transfer",error.getMessage() ,error))
                .map(mapper::toTransfer);
    }

    @Override
    public Mono<Transfer> update(String id, Transfer transfer) {
        transfer.setUpdatedDate(Constants.convertToLocalTimeZone(LocalDateTime.now()));
        return transferRepository.findById(id)
                .flatMap(savedAccount -> {
                    if (transfer.getOriginAccount() != null) savedAccount.setOriginAccount(transfer.getOriginAccount());
                    if (transfer.getTargetNumber() != null) savedAccount.setTargetNumber(transfer.getTargetNumber());
                    if (transfer.getTargetAccount() != null) savedAccount.setTargetAccount(transfer.getTargetAccount());
                    if (transfer.getAmount() != null) savedAccount.setAmount(transfer.getAmount());
                    if (transfer.getTransferType() != null) savedAccount.setTransferType(transfer.getTransferType());
                    if (transfer.getTransferStatus() != null) savedAccount.setTransferStatus(transfer.getTransferStatus());
                    return transferRepository.save(savedAccount).map(mapper::toTransfer);
                })
                .doOnError(error -> log.error(Constants.ERROR_SAVING, "transfer",error.getMessage() , error));
    }
}
