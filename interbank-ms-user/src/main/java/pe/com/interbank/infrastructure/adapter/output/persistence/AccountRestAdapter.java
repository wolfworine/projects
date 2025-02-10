package pe.com.interbank.infrastructure.adapter.output.persistence;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pe.com.interbank.application.port.output.AccountPersistencePort;
import pe.com.interbank.domain.model.Account;
import pe.com.interbank.infrastructure.adapter.output.persistence.mapper.AccountPersistenceMapper;
import pe.com.interbank.infrastructure.adapter.output.persistence.repository.AccountRepository;
import pe.com.interbank.utils.Constants;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Component
@Slf4j
@RequiredArgsConstructor
public class AccountRestAdapter implements AccountPersistencePort {

    private final AccountRepository accountRepository;
    private final AccountPersistenceMapper mapper;

    @Override
    public Flux<Account> findAllByDocument(String document) {
        return accountRepository.findAllByDocument(document)
                .map(mapper::toAccount);
    }

    @Override
    public Mono<Account> findById(String id) {
        return accountRepository.findById(id)
                .map(mapper::toAccount);
    }

    @Override
    public Mono<Account> findByUsername(String username) {
        return accountRepository.findByUsername(username)
                .map(mapper::toAccount);
    }

    @Override
    public Mono<Account> save(Account account) {
        account.setCreatedDate(Constants.convertToLocalTimeZone(LocalDateTime.now()));
        account.setIsNewEntry(true);
        return accountRepository.save(mapper.toAccountEntity(account))
                .map(mapper::toAccount)
                .doOnError(error -> log.error(Constants.ERROR_SAVING, Constants.ACCOUNT,error.getMessage() ,error));
    }

    @Override
    public Mono<Account> update(String id, Account account) {
        return accountRepository.findById(id)
                .flatMap(savedAccount -> {
                    if (account.getAccountNumber() != null) savedAccount.setAccountNumber(account.getAccountNumber());
                    if (account.getBankingEntity() != null) savedAccount.setBankingEntity(account.getBankingEntity());
                    if (account.getDeviceSerial() != null) savedAccount.setDeviceSerial(account.getDeviceSerial());
                    if (account.getDailyLimit() != null) savedAccount.setDailyLimit(account.getDailyLimit());
                    if (account.getOperationLimit() != null) savedAccount.setOperationLimit(account.getOperationLimit());
                    if (account.getPassword() != null) savedAccount.setPassword(account.getPassword());
                    if (account.getCurrency() != null) savedAccount.setCurrency(account.getCurrency());
                    savedAccount.setUpdatedDate(Constants.convertToLocalTimeZone(LocalDateTime.now()));
                    savedAccount.setNewEntry(false);
                    return accountRepository.save(savedAccount).map(mapper::toAccount);
                })
                .doOnError(error -> log.error(Constants.ERROR_SAVING, Constants.ACCOUNT,error.getMessage() ,error));
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return  accountRepository.deleteById(id)
                .doOnError(error -> log.error(Constants.ERROR_DELETED, Constants.ACCOUNT,error.getMessage() ,error));
    }

}
