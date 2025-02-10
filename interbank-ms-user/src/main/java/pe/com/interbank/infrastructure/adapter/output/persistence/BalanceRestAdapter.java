package pe.com.interbank.infrastructure.adapter.output.persistence;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pe.com.interbank.application.port.output.BalancePersistencePort;
import pe.com.interbank.domain.model.Balance;
import pe.com.interbank.infrastructure.adapter.output.persistence.mapper.BalancePersistenceMapper;
import pe.com.interbank.infrastructure.adapter.output.persistence.repository.BalanceRepository;
import pe.com.interbank.utils.Constants;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Component
@Slf4j
@RequiredArgsConstructor
public class BalanceRestAdapter implements BalancePersistencePort {

    private final BalanceRepository balanceRepository;
    private final BalancePersistenceMapper mapper;

    @Override
    public Mono<Balance> findById(String id) {
        return balanceRepository.findById(id)
                .map(mapper::toBalance);
    }

    @Override
    public Flux<Balance> findAll() {
        return balanceRepository.findAll()
                .map(mapper::toBalance);
    }

    @Override
    public Mono<Balance> save(Balance balance) {
        balance.setLastUpdate(Constants.convertToLocalTimeZone(LocalDateTime.now()));
        return balanceRepository.save(mapper.toBalanceEntity(balance))
                .doOnError(error -> log.error(Constants.ERROR_SAVING, "balance",error.getMessage() ,error))
                .map(mapper::toBalance);
    }

    @Override
    public Mono<Balance> update(String id, Balance balance) {
        balance.setLastUpdate(Constants.convertToLocalTimeZone(LocalDateTime.now()));
        return balanceRepository.findById(id)
                .flatMap(savedBalance -> {
                    if (balance.getOriginAccount() != null) savedBalance.setOriginAccount(balance.getOriginAccount());
                    if (balance.getCurrentBalance() != null) savedBalance.setBalance(balance.getCurrentBalance());
                    return balanceRepository.save(savedBalance).map(mapper::toBalance);
                })
                .doOnError(error -> log.error(Constants.ERROR_SAVING, "balance",error.getMessage() ,error));
    }

}
