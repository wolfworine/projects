package pe.com.interbank.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.com.interbank.application.port.input.AuthServicePort;
import pe.com.interbank.application.port.output.AccountPersistencePort;
import pe.com.interbank.application.port.output.UserPersistencePort;
import pe.com.interbank.domain.exception.InvalidCredentialException;
import pe.com.interbank.domain.model.Account;
import pe.com.interbank.domain.model.Login;
import pe.com.interbank.domain.model.User;
import pe.com.interbank.infrastructure.adapter.input.rest.mapper.AccountRestMapper;
import pe.com.interbank.infrastructure.adapter.input.rest.mapper.UserRestMapper;
import pe.com.interbank.infrastructure.adapter.input.rest.model.enums.CurrencyEnum;
import pe.com.interbank.infrastructure.adapter.input.rest.model.enums.RoleEnum;
import pe.com.interbank.infrastructure.adapter.input.rest.model.input.LoginRequest;
import pe.com.interbank.infrastructure.adapter.input.rest.model.input.RegisterRequest;
import pe.com.interbank.infrastructure.adapter.input.rest.model.output.AuthResponse;
import pe.com.interbank.infrastructure.adapter.security.TokenProvider;
import pe.com.interbank.utils.Constants;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

import static pe.com.interbank.utils.ErrorCatalog.INVALID_USER;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService implements AuthServicePort {

    private final UserPersistencePort userPersistencePort;
    private final AccountPersistencePort accountPersistencePort;
    private final AccountRestMapper accountRestMapper;
    private final UserRestMapper userRestMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    @Override
    public Mono<AuthResponse> login(LoginRequest request) {
        return accountPersistencePort.findByUsername(request.username())
                .filter(accountExists -> passwordEncoder.matches(request.password(), accountExists.getPassword()))
                .switchIfEmpty(Mono.error(new InvalidCredentialException(INVALID_USER.getTitle())))
                .flatMap(account -> userPersistencePort.findById(account.getDocument())
                        .map(user -> new AuthResponse(tokenProvider.generateToken(account), user, List.of(account)))
                );
    }

    @Override
    @Transactional
    public Mono<AuthResponse> register(RegisterRequest request) {
        return createUserAndSave(request)
                .flatMap(user -> createAccountAndSave(request)
                        .map(account -> new AuthResponse(tokenProvider.generateToken(account),
                                user, List.of(account)))
                );
    }

    @Override
    public Mono<String> logout(Login login) {
        return null;
    }

    @Override
    public Mono<String> recoverPassword(Login login) {
        return null;
    }

    private Mono<User> createUserAndSave(RegisterRequest request) {
        User newUser = userRestMapper.toUser(request);
        newUser.setEnabled(Constants.ENABLED);
        newUser.setCreatedDate(Constants.convertToLocalTimeZone(LocalDateTime.now()));
        return userPersistencePort.save(newUser);
    }

    private Mono<Account> createAccountAndSave(RegisterRequest request) {
        Account newAccount = accountRestMapper.toAccount(request);
        newAccount.setPassword(passwordEncoder.encode(request.password()));
        newAccount.setRole(RoleEnum.USER);
        newAccount.setCurrency(CurrencyEnum.PEN);
        return accountPersistencePort.save(newAccount);
    }

}
