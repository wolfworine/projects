package pe.com.interbank.infrastructure.adapter.output.persistence;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import pe.com.interbank.application.port.output.UserPersistencePort;
import pe.com.interbank.domain.model.User;
import pe.com.interbank.infrastructure.adapter.output.persistence.mapper.UserPersistenceMapper;
import pe.com.interbank.infrastructure.adapter.output.persistence.repository.UserRepository;
import pe.com.interbank.utils.Constants;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.beans.FeatureDescriptor;
import java.time.LocalDateTime;
import java.util.Arrays;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserRestAdapter implements UserPersistencePort {

    private final UserRepository userRepository;
    private final UserPersistenceMapper mapper;

    @Override
    public Mono<User> findById(String id) {
        return userRepository.findById(id)
                .map(mapper::toUser);
    }

    @Override
    public Flux<User> findAll() {
        return userRepository.findAll()
                .map(mapper::toUser);
    }
/*
    @Override
    public Mono<User> update(String id, User user) {
        user.setUpdatedDate(Constants.convertToLocalTimeZone(LocalDateTime.now()));
        user.setIsNewEntry(false);
        return userRepository.findById(id)
                .flatMap(savedUser -> {
                    BeanUtils.copyProperties(user, savedUser, getNullPropertyNames(user));
                    return userRepository.save(savedUser).map(mapper::toUser);
                })
                .doOnError(error -> log.error(Constants.ERROR_SAVING, "user", error.getMessage(), error));
    }


    private String[] getNullPropertyNames(Object source) {
        return Arrays.stream(BeanUtils.getPropertyDescriptors(source.getClass()))
                .map(FeatureDescriptor::getName)
                .filter(propertyName -> {
                    try {
                        return BeanUtils.getPropertyDescriptor(source.getClass(), propertyName)
                                .getReadMethod()
                                .invoke(source) == null;
                    } catch (Exception e) {
                        return false;
                    }
                })
                .toArray(String[]::new);
    }*/

    @Override
    public Mono<User> update(String id, User user) {
        return userRepository.findById(id)
                .flatMap(savedUser -> {
                    if (user.getFirstname() != null) savedUser.setFirstname(user.getFirstname());
                    if (user.getLastname() != null) savedUser.setLastname(user.getLastname());
                    if (user.getAddress() != null) savedUser.setAddress(user.getAddress());
                    if (user.getEmail() != null) savedUser.setEmail(user.getEmail());
                    savedUser.setUpdatedDate(Constants.convertToLocalTimeZone(LocalDateTime.now()));
                    savedUser.setNewEntry(false);
                    return userRepository.save(savedUser).map(mapper::toUser);
                })
                .doOnError(error -> log.error(Constants.ERROR_SAVING, "user",error.getMessage() ,error));
    }

    @Override
    public Mono<User> save(User user) {
        log.info("save {}" , user);
        user.setCreatedDate(Constants.convertToLocalTimeZone(LocalDateTime.now()));
        user.setIsNewEntry(true);
        return userRepository.save(mapper.toUserEntity(user))
                .map(mapper::toUser)
                .doOnError(error -> log.error(Constants.ERROR_SAVING, "user", error.getMessage(), error));
    }


/*
    @Override
    public Mono<User> save(User user) {
        log.info("save {}" , user);
        user.setCreatedDate(Constants.convertToLocalTimeZone(LocalDateTime.now()));
        user.setIsNewEntry(true);
        return userRepository.findById(user.getDocument())
                .map(Optional::of)
                .switchIfEmpty(Mono.just(Optional.empty()))
                .flatMap(existingUser -> existingUser.isPresent()
                        ? Mono.error(new DuplicateUserException(USER_DUPLICATE.getTitle()))
                        :   userRepository.save(mapper.toUserEntity(user))
                .map(mapper::toUser)
                .doOnError(error -> log.error(Constants.ERROR_SAVING, "user",error.getMessage() ,error)));
    }
*/
/*
    @Override
    public Mono<User> save(User user) {
        log.info("save {}", user);
        user.setCreatedDate(Constants.convertToLocalTimeZone(LocalDateTime.now()));
        user.setIsNewEntry(true);
        return userRepository.saveIfNotExists(mapper.toUserEntity(user))
                .switchIfEmpty(Mono.error(new DuplicateUserException(USER_DUPLICATE.getTitle())))
                .map(mapper::toUser);
    }*/

    @Override
    public Mono<Void> delete(String id) {
        return userRepository.findById(id)
                .flatMap(savedUser -> {
                    savedUser.setEnabled(false);
                    savedUser.setNewEntry(false);
                    return userRepository.save(savedUser).then();
                })
                .doOnError(error -> log.error(Constants.ERROR_SAVING, "user",error.getMessage() ,error));
    }

}