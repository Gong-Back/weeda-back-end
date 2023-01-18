package gongback.weeda.api.controller.advice;

import gongback.weeda.api.controller.response.ErrorDetailResponse;
import gongback.weeda.api.controller.response.ErrorResponse;
import gongback.weeda.common.exception.ResponseCode;
import gongback.weeda.common.exception.WeedaApplicationException;
import gongback.weeda.common.provider.EntityProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

@Slf4j
@RestControllerAdvice
public class BasicControllerAdvice {
    private ArrayList<ErrorDetailResponse> getErrorDetailResponses(WebExchangeBindException e) {
        WebExchangeBindException bindException = e;

        ArrayList<ErrorDetailResponse> errors = new ArrayList<>();
        for (int i = 0; i < bindException.getErrorCount(); i++) {
            errors.add(ErrorDetailResponse.of(
                    bindException.getFieldErrors().get(i).getField(),
                    bindException.getAllErrors().get(i).getDefaultMessage()
            ));
        }
        return errors;
    }

    @ExceptionHandler(WeedaApplicationException.class)
    public Mono<ResponseEntity> weedaApplicationException(WeedaApplicationException e) {
        log.error("[WeedaApplicationException] {}", e.getMsg(), e);
        return Mono.just(EntityProvider.error(e.getResponseCode(), e.getMsg()));
    }

    @ExceptionHandler(RuntimeException.class)
    public Mono<ResponseEntity> runtimeException(RuntimeException e) {
        log.error("[RuntimeException]", e);

        if (e instanceof WebExchangeBindException) {
            ArrayList<ErrorDetailResponse> errors = getErrorDetailResponses((WebExchangeBindException) e);
            return Mono.just(EntityProvider.response(ResponseCode.REQUEST_VALIDATION_ERROR, ErrorResponse.of(errors)));
        }

        return Mono.just(EntityProvider.response(ResponseCode.INTERNAL_SERVER_ERROR));
    }
}

