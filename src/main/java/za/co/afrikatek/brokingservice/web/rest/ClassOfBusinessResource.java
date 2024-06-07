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
import za.co.afrikatek.brokingservice.domain.ClassOfBusiness;
import za.co.afrikatek.brokingservice.repository.ClassOfBusinessRepository;
import za.co.afrikatek.brokingservice.service.ClassOfBusinessService;
import za.co.afrikatek.brokingservice.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link za.co.afrikatek.brokingservice.domain.ClassOfBusiness}.
 */
@RestController
@RequestMapping("/api/class-of-businesses")
public class ClassOfBusinessResource {

    private final Logger log = LoggerFactory.getLogger(ClassOfBusinessResource.class);

    private static final String ENTITY_NAME = "classOfBusiness";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ClassOfBusinessService classOfBusinessService;

    private final ClassOfBusinessRepository classOfBusinessRepository;

    public ClassOfBusinessResource(ClassOfBusinessService classOfBusinessService, ClassOfBusinessRepository classOfBusinessRepository) {
        this.classOfBusinessService = classOfBusinessService;
        this.classOfBusinessRepository = classOfBusinessRepository;
    }

    /**
     * {@code POST  /class-of-businesses} : Create a new classOfBusiness.
     *
     * @param classOfBusiness the classOfBusiness to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new classOfBusiness, or with status {@code 400 (Bad Request)} if the classOfBusiness has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ClassOfBusiness> createClassOfBusiness(@Valid @RequestBody ClassOfBusiness classOfBusiness)
        throws URISyntaxException {
        log.debug("REST request to save ClassOfBusiness : {}", classOfBusiness);
        if (classOfBusiness.getId() != null) {
            throw new BadRequestAlertException("A new classOfBusiness cannot already have an ID", ENTITY_NAME, "idexists");
        }
        classOfBusiness = classOfBusinessService.save(classOfBusiness);
        return ResponseEntity.created(new URI("/api/class-of-businesses/" + classOfBusiness.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, classOfBusiness.getId().toString()))
            .body(classOfBusiness);
    }

    /**
     * {@code PUT  /class-of-businesses/:id} : Updates an existing classOfBusiness.
     *
     * @param id the id of the classOfBusiness to save.
     * @param classOfBusiness the classOfBusiness to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated classOfBusiness,
     * or with status {@code 400 (Bad Request)} if the classOfBusiness is not valid,
     * or with status {@code 500 (Internal Server Error)} if the classOfBusiness couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ClassOfBusiness> updateClassOfBusiness(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ClassOfBusiness classOfBusiness
    ) throws URISyntaxException {
        log.debug("REST request to update ClassOfBusiness : {}, {}", id, classOfBusiness);
        if (classOfBusiness.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, classOfBusiness.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!classOfBusinessRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        classOfBusiness = classOfBusinessService.update(classOfBusiness);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, classOfBusiness.getId().toString()))
            .body(classOfBusiness);
    }

    /**
     * {@code PATCH  /class-of-businesses/:id} : Partial updates given fields of an existing classOfBusiness, field will ignore if it is null
     *
     * @param id the id of the classOfBusiness to save.
     * @param classOfBusiness the classOfBusiness to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated classOfBusiness,
     * or with status {@code 400 (Bad Request)} if the classOfBusiness is not valid,
     * or with status {@code 404 (Not Found)} if the classOfBusiness is not found,
     * or with status {@code 500 (Internal Server Error)} if the classOfBusiness couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ClassOfBusiness> partialUpdateClassOfBusiness(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ClassOfBusiness classOfBusiness
    ) throws URISyntaxException {
        log.debug("REST request to partial update ClassOfBusiness partially : {}, {}", id, classOfBusiness);
        if (classOfBusiness.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, classOfBusiness.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!classOfBusinessRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ClassOfBusiness> result = classOfBusinessService.partialUpdate(classOfBusiness);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, classOfBusiness.getId().toString())
        );
    }

    /**
     * {@code GET  /class-of-businesses} : get all the classOfBusinesses.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of classOfBusinesses in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ClassOfBusiness>> getAllClassOfBusinesses(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of ClassOfBusinesses");
        Page<ClassOfBusiness> page;
        if (eagerload) {
            page = classOfBusinessService.findAllWithEagerRelationships(pageable);
        } else {
            page = classOfBusinessService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /class-of-businesses/:id} : get the "id" classOfBusiness.
     *
     * @param id the id of the classOfBusiness to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the classOfBusiness, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ClassOfBusiness> getClassOfBusiness(@PathVariable("id") Long id) {
        log.debug("REST request to get ClassOfBusiness : {}", id);
        Optional<ClassOfBusiness> classOfBusiness = classOfBusinessService.findOne(id);
        return ResponseUtil.wrapOrNotFound(classOfBusiness);
    }

    /**
     * {@code DELETE  /class-of-businesses/:id} : delete the "id" classOfBusiness.
     *
     * @param id the id of the classOfBusiness to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClassOfBusiness(@PathVariable("id") Long id) {
        log.debug("REST request to delete ClassOfBusiness : {}", id);
        classOfBusinessService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
