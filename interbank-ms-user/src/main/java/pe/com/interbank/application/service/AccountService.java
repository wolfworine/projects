package pe.com.interbank.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pe.com.interbank.application.port.input.AccountServicePort;
import pe.com.interbank.application.port.output.AccountPersistencePort;
import pe.com.interbank.domain.model.Account;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService implements AccountServicePort {

    private final AccountPersistencePort accountPersistencePort;
    private final BCryptPasswordEncoder passwordEncoder;


    @Override
    public Mono<Account> findById(String id) {
        return this.accountPersistencePort.findById(id);
    }

    @Override
    public Mono<Account> findByUsername(String username) {
        return this.accountPersistencePort.findByUsername(username);
    }

    @Override
    public Flux<Account> findAllByDocument(String document) {
        return this.accountPersistencePort.findAllByDocument(document);
    }

    @Override
    public Mono<Account> save(Account account) {
        account.setPassword(this.passwordEncoder.encode(account.getPassword()));
        return  this.accountPersistencePort.save(account);
    }

    @Override
    public Mono<Account> update(String id, Account account) {
        if (account.getPassword() != null) account.setPassword(this.passwordEncoder.encode(account.getPassword()));
        return accountPersistencePort.update(id,account);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return accountPersistencePort.deleteById(id);
    }
}
