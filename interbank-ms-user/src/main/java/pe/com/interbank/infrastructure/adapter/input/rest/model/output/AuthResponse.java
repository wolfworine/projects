package pe.com.interbank.infrastructure.adapter.input.rest.model.output;

import pe.com.interbank.domain.model.Account;
import pe.com.interbank.domain.model.User;

import java.util.List;

public record AuthResponse (String token, User user, List<Account> account) {
}