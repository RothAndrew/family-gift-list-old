package com.rothandrew.familygiftlist.gateway.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.rothandrew.familygiftlist.gateway.service.GiftService;
import com.rothandrew.familygiftlist.gateway.web.rest.errors.BadRequestAlertException;
import com.rothandrew.familygiftlist.gateway.web.rest.util.HeaderUtil;
import com.rothandrew.familygiftlist.gateway.web.rest.util.PaginationUtil;
import com.rothandrew.familygiftlist.gateway.service.dto.GiftDTO;
import com.rothandrew.familygiftlist.gateway.service.dto.GiftCriteria;
import com.rothandrew.familygiftlist.gateway.service.GiftQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Gift.
 */
@RestController
@RequestMapping("/api")
public class GiftResource {

    private final Logger log = LoggerFactory.getLogger(GiftResource.class);

    private static final String ENTITY_NAME = "gift";

    private final GiftService giftService;

    private final GiftQueryService giftQueryService;

    public GiftResource(GiftService giftService, GiftQueryService giftQueryService) {
        this.giftService = giftService;
        this.giftQueryService = giftQueryService;
    }

    /**
     * POST  /gifts : Create a new gift.
     *
     * @param giftDTO the giftDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new giftDTO, or with status 400 (Bad Request) if the gift has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/gifts")
    @Timed
    public ResponseEntity<GiftDTO> createGift(@Valid @RequestBody GiftDTO giftDTO) throws URISyntaxException {
        log.debug("REST request to save Gift : {}", giftDTO);
        if (giftDTO.getId() != null) {
            throw new BadRequestAlertException("A new gift cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GiftDTO result = giftService.save(giftDTO);
        return ResponseEntity.created(new URI("/api/gifts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /gifts : Updates an existing gift.
     *
     * @param giftDTO the giftDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated giftDTO,
     * or with status 400 (Bad Request) if the giftDTO is not valid,
     * or with status 500 (Internal Server Error) if the giftDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/gifts")
    @Timed
    public ResponseEntity<GiftDTO> updateGift(@Valid @RequestBody GiftDTO giftDTO) throws URISyntaxException {
        log.debug("REST request to update Gift : {}", giftDTO);
        if (giftDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        GiftDTO result = giftService.save(giftDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, giftDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /gifts : get all the gifts.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of gifts in body
     */
    @GetMapping("/gifts")
    @Timed
    public ResponseEntity<List<GiftDTO>> getAllGifts(GiftCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Gifts by criteria: {}", criteria);
        Page<GiftDTO> page = giftQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/gifts");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /gifts/:id : get the "id" gift.
     *
     * @param id the id of the giftDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the giftDTO, or with status 404 (Not Found)
     */
    @GetMapping("/gifts/{id}")
    @Timed
    public ResponseEntity<GiftDTO> getGift(@PathVariable Long id) {
        log.debug("REST request to get Gift : {}", id);
        Optional<GiftDTO> giftDTO = giftService.findOne(id);
        return ResponseUtil.wrapOrNotFound(giftDTO);
    }

    /**
     * DELETE  /gifts/:id : delete the "id" gift.
     *
     * @param id the id of the giftDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/gifts/{id}")
    @Timed
    public ResponseEntity<Void> deleteGift(@PathVariable Long id) {
        log.debug("REST request to delete Gift : {}", id);
        giftService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
