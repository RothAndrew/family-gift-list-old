package com.rothandrew.familygiftlist.gateway.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.rothandrew.familygiftlist.gateway.service.GiftListService;
import com.rothandrew.familygiftlist.gateway.web.rest.errors.BadRequestAlertException;
import com.rothandrew.familygiftlist.gateway.web.rest.util.HeaderUtil;
import com.rothandrew.familygiftlist.gateway.web.rest.util.PaginationUtil;
import com.rothandrew.familygiftlist.gateway.service.dto.GiftListDTO;
import com.rothandrew.familygiftlist.gateway.service.dto.GiftListCriteria;
import com.rothandrew.familygiftlist.gateway.service.GiftListQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing GiftList.
 */
@RestController
@RequestMapping("/api")
public class GiftListResource {

    private final Logger log = LoggerFactory.getLogger(GiftListResource.class);

    private static final String ENTITY_NAME = "giftList";

    private final GiftListService giftListService;

    private final GiftListQueryService giftListQueryService;

    public GiftListResource(GiftListService giftListService, GiftListQueryService giftListQueryService) {
        this.giftListService = giftListService;
        this.giftListQueryService = giftListQueryService;
    }

    /**
     * POST  /gift-lists : Create a new giftList.
     *
     * @param giftListDTO the giftListDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new giftListDTO, or with status 400 (Bad Request) if the giftList has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/gift-lists")
    @Timed
    public ResponseEntity<GiftListDTO> createGiftList(@RequestBody GiftListDTO giftListDTO) throws URISyntaxException {
        log.debug("REST request to save GiftList : {}", giftListDTO);
        if (giftListDTO.getId() != null) {
            throw new BadRequestAlertException("A new giftList cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GiftListDTO result = giftListService.save(giftListDTO);
        return ResponseEntity.created(new URI("/api/gift-lists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /gift-lists : Updates an existing giftList.
     *
     * @param giftListDTO the giftListDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated giftListDTO,
     * or with status 400 (Bad Request) if the giftListDTO is not valid,
     * or with status 500 (Internal Server Error) if the giftListDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/gift-lists")
    @Timed
    public ResponseEntity<GiftListDTO> updateGiftList(@RequestBody GiftListDTO giftListDTO) throws URISyntaxException {
        log.debug("REST request to update GiftList : {}", giftListDTO);
        if (giftListDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        GiftListDTO result = giftListService.save(giftListDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, giftListDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /gift-lists : get all the giftLists.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of giftLists in body
     */
    @GetMapping("/gift-lists")
    @Timed
    public ResponseEntity<List<GiftListDTO>> getAllGiftLists(GiftListCriteria criteria, Pageable pageable) {
        log.debug("REST request to get GiftLists by criteria: {}", criteria);
        Page<GiftListDTO> page = giftListQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/gift-lists");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /gift-lists/:id : get the "id" giftList.
     *
     * @param id the id of the giftListDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the giftListDTO, or with status 404 (Not Found)
     */
    @GetMapping("/gift-lists/{id}")
    @Timed
    public ResponseEntity<GiftListDTO> getGiftList(@PathVariable Long id) {
        log.debug("REST request to get GiftList : {}", id);
        Optional<GiftListDTO> giftListDTO = giftListService.findOne(id);
        return ResponseUtil.wrapOrNotFound(giftListDTO);
    }

    /**
     * DELETE  /gift-lists/:id : delete the "id" giftList.
     *
     * @param id the id of the giftListDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/gift-lists/{id}")
    @Timed
    public ResponseEntity<Void> deleteGiftList(@PathVariable Long id) {
        log.debug("REST request to delete GiftList : {}", id);
        giftListService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
