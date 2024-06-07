package za.co.afrikatek.brokingservice.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;
import za.co.afrikatek.brokingservice.domain.ReinsurancePlacement;
import za.co.afrikatek.brokingservice.repository.ReinsurancePlacementRepository;
import za.co.afrikatek.brokingservice.service.ReinsurancePlacementService;
import za.co.afrikatek.brokingservice.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link za.co.afrikatek.brokingservice.domain.ReinsurancePlacement}.
 */
@RestController
@RequestMapping("/api/reinsurance-placements")
public class ReinsurancePlacementResource {

    private final Logger log = LoggerFactory.getLogger(ReinsurancePlacementResource.class);

    private static final String ENTITY_NAME = "reinsurancePlacement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReinsurancePlacementService reinsurancePlacementService;

    private final ReinsurancePlacementRepository reinsurancePlacementRepository;

    public ReinsurancePlacementResource(
        ReinsurancePlacementService reinsurancePlacementService,
        ReinsurancePlacementRepository reinsurancePlacementRepository
    ) {
        this.reinsurancePlacementService = reinsurancePlacementService;
        this.reinsurancePlacementRepository = reinsurancePlacementRepository;
    }

    /**
     * {@code POST  /reinsurance-placements} : Create a new reinsurancePlacement.
     *
     * @param reinsurancePlacement the reinsurancePlacement to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reinsurancePlacement, or with status {@code 400 (Bad Request)} if the reinsurancePlacement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ReinsurancePlacement> createReinsurancePlacement(@Valid @RequestBody ReinsurancePlacement reinsurancePlacement)
        throws URISyntaxException {
        log.debug("REST request to save ReinsurancePlacement : {}", reinsurancePlacement);
        if (reinsurancePlacement.getId() != null) {
            throw new BadRequestAlertException("A new reinsurancePlacement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        reinsurancePlacement = reinsurancePlacementService.save(reinsurancePlacement);
        return ResponseEntity.created(new URI("/api/reinsurance-placements/" + reinsurancePlacement.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, reinsurancePlacement.getId().toString()))
            .body(reinsurancePlacement);
    }

    /**
     * {@code PUT  /reinsurance-placements/:id} : Updates an existing reinsurancePlacement.
     *
     * @param id the id of the reinsurancePlacement to save.
     * @param reinsurancePlacement the reinsurancePlacement to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reinsurancePlacement,
     * or with status {@code 400 (Bad Request)} if the reinsurancePlacement is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reinsurancePlacement couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ReinsurancePlacement> updateReinsurancePlacement(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ReinsurancePlacement reinsurancePlacement
    ) throws URISyntaxException {
        log.debug("REST request to update ReinsurancePlacement : {}, {}", id, reinsurancePlacement);
        if (reinsurancePlacement.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reinsurancePlacement.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reinsurancePlacementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        reinsurancePlacement = reinsurancePlacementService.update(reinsurancePlacement);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reinsurancePlacement.getId().toString()))
            .body(reinsurancePlacement);
    }

    /**
     * {@code PATCH  /reinsurance-placements/:id} : Partial updates given fields of an existing reinsurancePlacement, field will ignore if it is null
     *
     * @param id the id of the reinsurancePlacement to save.
     * @param reinsurancePlacement the reinsurancePlacement to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reinsurancePlacement,
     * or with status {@code 400 (Bad Request)} if the reinsurancePlacement is not valid,
     * or with status {@code 404 (Not Found)} if the reinsurancePlacement is not found,
     * or with status {@code 500 (Internal Server Error)} if the reinsurancePlacement couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ReinsurancePlacement> partialUpdateReinsurancePlacement(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ReinsurancePlacement reinsurancePlacement
    ) throws URISyntaxException {
        log.debug("REST request to partial update ReinsurancePlacement partially : {}, {}", id, reinsurancePlacement);
        if (reinsurancePlacement.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reinsurancePlacement.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reinsurancePlacementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReinsurancePlacement> result = reinsurancePlacementService.partialUpdate(reinsurancePlacement);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reinsurancePlacement.getId().toString())
        );
    }

    /**
     * {@code GET  /reinsurance-placements} : get all the reinsurancePlacements.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reinsurancePlacements in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ReinsurancePlacement>> getAllReinsurancePlacements(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of ReinsurancePlacements");
        Page<ReinsurancePlacement> page = reinsurancePlacementService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /reinsurance-placements/:id} : get the "id" reinsurancePlacement.
     *
     * @param id the id of the reinsurancePlacement to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reinsurancePlacement, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReinsurancePlacement> getReinsurancePlacement(@PathVariable("id") Long id) {
        log.debug("REST request to get ReinsurancePlacement : {}", id);
        Optional<ReinsurancePlacement> reinsurancePlacement = reinsurancePlacementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reinsurancePlacement);
    }

    /**
     * {@code DELETE  /reinsurance-placements/:id} : delete the "id" reinsurancePlacement.
     *
     * @param id the id of the reinsurancePlacement to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReinsurancePlacement(@PathVariable("id") Long id) {
        log.debug("REST request to delete ReinsurancePlacement : {}", id);
        reinsurancePlacementService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
