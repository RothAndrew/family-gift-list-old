package com.rothandrew.familygiftlist.gateway.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Family.
 */
@Entity
@Table(name = "family")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Family implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "family_members",
               joinColumns = @JoinColumn(name = "families_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "members_id", referencedColumnName = "id"))
    private Set<User> members = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "family_owners",
               joinColumns = @JoinColumn(name = "families_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "owners_id", referencedColumnName = "id"))
    private Set<User> owners = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "family_admins",
               joinColumns = @JoinColumn(name = "families_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "admins_id", referencedColumnName = "id"))
    private Set<User> admins = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Family name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<User> getMembers() {
        return members;
    }

    public Family members(Set<User> users) {
        this.members = users;
        return this;
    }

    public Family addMembers(User user) {
        this.members.add(user);
        return this;
    }

    public Family removeMembers(User user) {
        this.members.remove(user);
        return this;
    }

    public void setMembers(Set<User> users) {
        this.members = users;
    }

    public Set<User> getOwners() {
        return owners;
    }

    public Family owners(Set<User> users) {
        this.owners = users;
        return this;
    }

    public Family addOwners(User user) {
        this.owners.add(user);
        return this;
    }

    public Family removeOwners(User user) {
        this.owners.remove(user);
        return this;
    }

    public void setOwners(Set<User> users) {
        this.owners = users;
    }

    public Set<User> getAdmins() {
        return admins;
    }

    public Family admins(Set<User> users) {
        this.admins = users;
        return this;
    }

    public Family addAdmins(User user) {
        this.admins.add(user);
        return this;
    }

    public Family removeAdmins(User user) {
        this.admins.remove(user);
        return this;
    }

    public void setAdmins(Set<User> users) {
        this.admins = users;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Family family = (Family) o;
        if (family.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), family.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Family{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
