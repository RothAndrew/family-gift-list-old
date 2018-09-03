package com.rothandrew.familygiftlist.gateway.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Family entity.
 */
public class FamilyDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private Set<UserDTO> members = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<UserDTO> getMembers() {
        return members;
    }

    public void setMembers(Set<UserDTO> users) {
        this.members = users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FamilyDTO familyDTO = (FamilyDTO) o;
        if (familyDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), familyDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FamilyDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
