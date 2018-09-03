package com.rothandrew.familygiftlist.gateway.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A GiftList.
 */
@Entity
@Table(name = "gift_list")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class GiftList implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "giftList")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Gift> gifts = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("")
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Gift> getGifts() {
        return gifts;
    }

    public GiftList gifts(Set<Gift> gifts) {
        this.gifts = gifts;
        return this;
    }

    public GiftList addGifts(Gift gift) {
        this.gifts.add(gift);
        gift.setGiftList(this);
        return this;
    }

    public GiftList removeGifts(Gift gift) {
        this.gifts.remove(gift);
        gift.setGiftList(null);
        return this;
    }

    public void setGifts(Set<Gift> gifts) {
        this.gifts = gifts;
    }

    public User getUser() {
        return user;
    }

    public GiftList user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
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
        GiftList giftList = (GiftList) o;
        if (giftList.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), giftList.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "GiftList{" +
            "id=" + getId() +
            "}";
    }
}
