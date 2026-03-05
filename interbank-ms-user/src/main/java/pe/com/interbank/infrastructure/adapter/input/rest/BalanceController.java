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
import pe.com.interbank.application.port.input.BalanceServicePort;
import pe.com.interbank.infrastructure.adapter.input.rest.mapper.BalanceRestMapper;
import pe.com.interbank.infrastructure.adapter.input.rest.model.input.BalanceRequest;
import pe.com.interbank.infrastructure.adapter.input.rest.model.output.BalanceResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/balance/api")
public class BalanceController {

    private final BalanceServicePort servicePort;
    private final BalanceRestMapper restMapper;

    @GetMapping("")
    public Flux<BalanceResponse> findAll() {
        return servicePort.findAll()
                .map(restMapper::toBalanceResponse);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<BalanceResponse>> findById(@PathVariable String id) {
        return servicePort.findById(id)
                .map(balance -> ResponseEntity.ok()
                        .body(restMapper.toBalanceResponse(balance)));
    }

    @PostMapping("")
    public Mono<ResponseEntity<BalanceResponse>> register(@RequestBody BalanceRequest request) {
        return servicePort.save(restMapper.toBalance(request))
                .map(balance -> ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(restMapper.toBalanceResponse(balance)));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<BalanceResponse>> update(@PathVariable String id,@RequestBody BalanceRequest request) {
        return servicePort.update(id, restMapper.toBalance(request))
                .map(updatedBalance -> ResponseEntity.ok()
                        .body(restMapper.toBalanceResponse(updatedBalance)));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable String id) {
        return servicePort.deleteById(id)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
