package gongback.weeda.api.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor(staticName = "of")
public class ErrorResponse {
    List<ErrorDetailResponse> errors;
}
