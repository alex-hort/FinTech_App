package com.FinTechApp.com.FinTechApp.role.services;

import com.FinTechApp.com.FinTechApp.role.entity.Role;
import com.FinTechApp.com.FinTechApp.res.Response;
import com.FinTechApp.com.FinTechApp.role.repo.RoleRepo;
import com.FinTechApp.com.FinTechApp.exceptions.BadRequestException;
import com.FinTechApp.com.FinTechApp.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepo roleRepo;

    @Override
    public Response<Role> createRole(Role roleRequest) {
       if(roleRepo.existsByName(roleRequest.getName())){
            throw new BadRequestException("Role already exists");

        }

        Role savedRole = roleRepo.save(roleRequest);
        return Response.<Role>builder()
                 .statusCode(HttpStatus.OK.value())
                 .message("Role created successfully")
                 .data(savedRole)
                 .build();
    }

    @Override
    public Response<Role> updateRole(Role roleRequest) {
        Role role = roleRepo.findById(roleRequest.getId())
                .orElseThrow(() -> new NotFoundException("Role not found with id: "));

                role.setName(roleRequest.getName());

                Role updatedRole = roleRepo.save(role);

                return Response.<Role>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Role updated successfully")
                        .data(updatedRole)
                        .build();
        
    }

    @Override
    public Response<List<Role>> getAllRoles() {
        List<Role> roles = roleRepo.findAll();
        return Response.<List<Role>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Roles retrieved successfully")
                .data(roles)
                .build();
    }

    @Override
    public Response<?> deleteRole(Long id) {
        if(!roleRepo.existsById(id)) {
            throw new NotFoundException("Role not found with id: " + id);
        }

        roleRepo.deleteById(id);

        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Role deleted successfully")
                .build();

       
    }
}