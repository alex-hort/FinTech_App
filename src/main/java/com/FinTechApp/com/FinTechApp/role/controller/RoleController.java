package com.FinTechApp.com.FinTechApp.role.controller;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.FinTechApp.com.FinTechApp.role.services.RoleService;
import com.FinTechApp.com.FinTechApp.role.entity.Role;
import com.FinTechApp.com.FinTechApp.res.Response;
import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
// @PreAuthorize("hasRole('ADMIN')")
public class RoleController {

    private final RoleService roleService;

    @PostMapping
    public ResponseEntity<Response<Role>> createRole(@RequestBody Role roleRequest) {
       return ResponseEntity.ok(roleService.createRole(roleRequest));
    }

    @PutMapping
    public ResponseEntity<Response<Role>> updateRole(@RequestBody Role roleRequest) {
        return ResponseEntity.ok(roleService.updateRole(roleRequest));
    }

    @GetMapping
    public ResponseEntity<Response<List<Role>>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<?>> deleteRole(@PathVariable Long id) {
        return ResponseEntity.ok(roleService.deleteRole(id));
    }

}