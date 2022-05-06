package com.harrydrummond.projecthjd.user.roles;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "role")
public class UserRole {

    @Id
    private int id;

    @Column(name = "role")
    private Role role;

    public UserRole(Role role) {
        this.role = role;
    }
}