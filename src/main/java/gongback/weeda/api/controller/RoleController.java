package gongback.weeda.api.controller;

import gongback.weeda.api.controller.request.RoleRequest;
import gongback.weeda.common.provider.DtoProvider;
import gongback.weeda.common.provider.EntityProvider;
import gongback.weeda.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/roles")
public class RoleController {

    private final RoleService roleService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<ResponseEntity> save(@RequestBody @Valid RoleRequest req) {
        return roleService.save(DtoProvider.fromRoleRequest(req))
                .thenReturn(EntityProvider.created());
    }
}
