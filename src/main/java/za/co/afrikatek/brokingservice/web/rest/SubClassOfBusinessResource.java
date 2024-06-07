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
import za.co.afrikatek.brokingservice.domain.SubClassOfBusiness;
import za.co.afrikatek.brokingservice.repository.SubClassOfBusinessRepository;
import za.co.afrikatek.brokingservice.service.SubClassOfBusinessService;
import za.co.afrikatek.brokingservice.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link za.co.afrikatek.brokingservice.domain.SubClassOfBusiness}.
 */
@RestController
@RequestMapping("/api/sub-class-of-businesses")
public class SubClassOfBusinessResource {

    private final Logger log = LoggerFactory.getLogger(SubClassOfBusinessResource.class);

    private static final String ENTITY_NAME = "subClassOfBusiness";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SubClassOfBusinessService subClassOfBusinessService;

    private final SubClassOfBusinessRepository subClassOfBusinessRepository;

    public SubClassOfBusinessResource(
        SubClassOfBusinessService subClassOfBusinessService,
        SubClassOfBusinessRepository subClassOfBusinessRepository
    ) {
        this.subClassOfBusinessService = subClassOfBusinessService;
        this.subClassOfBusinessRepository = subClassOfBusinessRepository;
    }

    /**
     * {@code POST  /sub-class-of-businesses} : Create a new subClassOfBusiness.
     *
     * @param subClassOfBusiness the subClassOfBusiness to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new subClassOfBusiness, or with status {@code 400 (Bad Request)} if the subClassOfBusiness has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SubClassOfBusiness> createSubClassOfBusiness(@Valid @RequestBody SubClassOfBusiness subClassOfBusiness)
        throws URISyntaxException {
        log.debug("REST request to save SubClassOfBusiness : {}", subClassOfBusiness);
        if (subClassOfBusiness.getId() != null) {
            throw new BadRequestAlertException("A new subClassOfBusiness cannot already have an ID", ENTITY_NAME, "idexists");
        }
        subClassOfBusiness = subClassOfBusinessService.save(subClassOfBusiness);
        return ResponseEntity.created(new URI("/api/sub-class-of-businesses/" + subClassOfBusiness.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, subClassOfBusiness.getId().toString()))
            .body(subClassOfBusiness);
    }

    /**
     * {@code PUT  /sub-class-of-businesses/:id} : Updates an existing subClassOfBusiness.
     *
     * @param id the id of the subClassOfBusiness to save.
     * @param subClassOfBusiness the subClassOfBusiness to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subClassOfBusiness,
     * or with status {@code 400 (Bad Request)} if the subClassOfBusiness is not valid,
     * or with status {@code 500 (Internal Server Error)} if the subClassOfBusiness couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SubClassOfBusiness> updateSubClassOfBusiness(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SubClassOfBusiness subClassOfBusiness
    ) throws URISyntaxException {
        log.debug("REST request to update SubClassOfBusiness : {}, {}", id, subClassOfBusiness);
        if (subClassOfBusiness.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subClassOfBusiness.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!subClassOfBusinessRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        subClassOfBusiness = subClassOfBusinessService.update(subClassOfBusiness);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, subClassOfBusiness.getId().toString()))
            .body(subClassOfBusiness);
    }

    /**
     * {@code PATCH  /sub-class-of-businesses/:id} : Partial updates given fields of an existing subClassOfBusiness, field will ignore if it is null
     *
     * @param id the id of the subClassOfBusiness to save.
     * @param subClassOfBusiness the subClassOfBusiness to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subClassOfBusiness,
     * or with status {@code 400 (Bad Request)} if the subClassOfBusiness is not valid,
     * or with status {@code 404 (Not Found)} if the subClassOfBusiness is not found,
     * or with status {@code 500 (Internal Server Error)} if the subClassOfBusiness couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SubClassOfBusiness> partialUpdateSubClassOfBusiness(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SubClassOfBusiness subClassOfBusiness
    ) throws URISyntaxException {
        log.debug("REST request to partial update SubClassOfBusiness partially : {}, {}", id, subClassOfBusiness);
        if (subClassOfBusiness.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subClassOfBusiness.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!subClassOfBusinessRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SubClassOfBusiness> result = subClassOfBusinessService.partialUpdate(subClassOfBusiness);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, subClassOfBusiness.getId().toString())
        );
    }

    /**
     * {@code GET  /sub-class-of-businesses} : get all the subClassOfBusinesses.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of subClassOfBusinesses in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SubClassOfBusiness>> getAllSubClassOfBusinesses(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of SubClassOfBusinesses");
        Page<SubClassOfBusiness> page;
        if (eagerload) {
            page = subClassOfBusinessService.findAllWithEagerRelationships(pageable);
        } else {
            page = subClassOfBusinessService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /sub-class-of-businesses/:id} : get the "id" subClassOfBusiness.
     *
     * @param id the id of the subClassOfBusiness to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the subClassOfBusiness, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SubClassOfBusiness> getSubClassOfBusiness(@PathVariable("id") Long id) {
        log.debug("REST request to get SubClassOfBusiness : {}", id);
        Optional<SubClassOfBusiness> subClassOfBusiness = subClassOfBusinessService.findOne(id);
        return ResponseUtil.wrapOrNotFound(subClassOfBusiness);
    }

    /**
     * {@code DELETE  /sub-class-of-businesses/:id} : delete the "id" subClassOfBusiness.
     *
     * @param id the id of the subClassOfBusiness to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubClassOfBusiness(@PathVariable("id") Long id) {
        log.debug("REST request to delete SubClassOfBusiness : {}", id);
        subClassOfBusinessService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
