package gongback.weeda.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@Component
public class TransactionStepVerifier {

    @Autowired
    ReactiveTransactionManager reactiveTransactionManager;

    public <T> StepVerifier.FirstStep<T> create(Mono<T> publisher) {
        return StepVerifier.create(TransactionalOperator.create(reactiveTransactionManager)
                .execute(status -> {
                    status.setRollbackOnly();
                    return publisher;
                })
        );
    }
}
