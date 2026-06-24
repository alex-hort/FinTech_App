package com.FinTechApp.com.FinTechApp.role.services;

import com.FinTechApp.com.FinTechApp.role.entity.Role;
import com.FinTechApp.com.FinTechApp.res.Response;
import java.util.List;

public interface RoleService {
    Response<Role> createRole(Role roleRequest);
    Response<Role> updateRole(Role roleRequest);
    Response<List<Role>> getAllRoles();
    Response<?> deleteRole(Long id);
}