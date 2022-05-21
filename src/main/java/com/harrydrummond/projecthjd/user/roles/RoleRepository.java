package com.harrydrummond.projecthjd.user.roles;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<UserRole, Long> {

    UserRole getByRole(Role role);
}