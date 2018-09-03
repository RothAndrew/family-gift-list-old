package com.rothandrew.familygiftlist.gateway.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;






/**
 * Criteria class for the Gift entity. This class is used in GiftResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /gifts?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class GiftCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter name;

    private StringFilter url;

    private LongFilter giftListId;

    public GiftCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getUrl() {
        return url;
    }

    public void setUrl(StringFilter url) {
        this.url = url;
    }

    public LongFilter getGiftListId() {
        return giftListId;
    }

    public void setGiftListId(LongFilter giftListId) {
        this.giftListId = giftListId;
    }

    @Override
    public String toString() {
        return "GiftCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (url != null ? "url=" + url + ", " : "") +
                (giftListId != null ? "giftListId=" + giftListId + ", " : "") +
            "}";
    }

}
