package pe.com.interbank.infrastructure.adapter.input.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.com.interbank.application.port.input.AccountServicePort;
import pe.com.interbank.infrastructure.adapter.input.rest.mapper.AccountRestMapper;
import pe.com.interbank.infrastructure.adapter.input.rest.model.input.AccountRequest;
import pe.com.interbank.infrastructure.adapter.input.rest.model.input.UpdateRequest;
import pe.com.interbank.infrastructure.adapter.input.rest.model.output.AccountResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/account/api")
public class AccountController {

    private final AccountServicePort servicePort;
    private final AccountRestMapper restMapper;

    @GetMapping("/all/{document}")
    public Flux<AccountResponse> findAllByDocument(@PathVariable String document) {
        return servicePort.findAllByDocument(document)
                .map(restMapper::toAccountResponse);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<AccountResponse>> findById(@PathVariable String id) {
        return servicePort.findById(id)
                .map(user -> ResponseEntity
                        .status(HttpStatus.ACCEPTED)
                        .body(restMapper.toAccountResponse(user)));
    }

    @PostMapping("")
    public Mono<ResponseEntity<AccountResponse>> register(@RequestBody AccountRequest request) {
        return servicePort.save(restMapper.toAccount(request))
                .map(account -> ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(restMapper.toAccountResponse(account)));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<AccountResponse>> update(@PathVariable String id,@RequestBody AccountRequest request) {
        return servicePort.update(id, restMapper.toAccount(request))
                .map(updatedUser -> ResponseEntity
                        .status(HttpStatus.ACCEPTED)
                        .body(restMapper.toAccountResponse(updatedUser)));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteById(@PathVariable String id) {
        return servicePort.deleteById(id)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
