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
import za.co.afrikatek.brokingservice.domain.InsuranceType;
import za.co.afrikatek.brokingservice.repository.InsuranceTypeRepository;
import za.co.afrikatek.brokingservice.service.InsuranceTypeService;
import za.co.afrikatek.brokingservice.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link za.co.afrikatek.brokingservice.domain.InsuranceType}.
 */
@RestController
@RequestMapping("/api/insurance-types")
public class InsuranceTypeResource {

    private final Logger log = LoggerFactory.getLogger(InsuranceTypeResource.class);

    private static final String ENTITY_NAME = "insuranceType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InsuranceTypeService insuranceTypeService;

    private final InsuranceTypeRepository insuranceTypeRepository;

    public InsuranceTypeResource(InsuranceTypeService insuranceTypeService, InsuranceTypeRepository insuranceTypeRepository) {
        this.insuranceTypeService = insuranceTypeService;
        this.insuranceTypeRepository = insuranceTypeRepository;
    }

    /**
     * {@code POST  /insurance-types} : Create a new insuranceType.
     *
     * @param insuranceType the insuranceType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new insuranceType, or with status {@code 400 (Bad Request)} if the insuranceType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<InsuranceType> createInsuranceType(@Valid @RequestBody InsuranceType insuranceType) throws URISyntaxException {
        log.debug("REST request to save InsuranceType : {}", insuranceType);
        if (insuranceType.getId() != null) {
            throw new BadRequestAlertException("A new insuranceType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        insuranceType = insuranceTypeService.save(insuranceType);
        return ResponseEntity.created(new URI("/api/insurance-types/" + insuranceType.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, insuranceType.getId().toString()))
            .body(insuranceType);
    }

    /**
     * {@code PUT  /insurance-types/:id} : Updates an existing insuranceType.
     *
     * @param id the id of the insuranceType to save.
     * @param insuranceType the insuranceType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated insuranceType,
     * or with status {@code 400 (Bad Request)} if the insuranceType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the insuranceType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<InsuranceType> updateInsuranceType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody InsuranceType insuranceType
    ) throws URISyntaxException {
        log.debug("REST request to update InsuranceType : {}, {}", id, insuranceType);
        if (insuranceType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, insuranceType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!insuranceTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        insuranceType = insuranceTypeService.update(insuranceType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, insuranceType.getId().toString()))
            .body(insuranceType);
    }

    /**
     * {@code PATCH  /insurance-types/:id} : Partial updates given fields of an existing insuranceType, field will ignore if it is null
     *
     * @param id the id of the insuranceType to save.
     * @param insuranceType the insuranceType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated insuranceType,
     * or with status {@code 400 (Bad Request)} if the insuranceType is not valid,
     * or with status {@code 404 (Not Found)} if the insuranceType is not found,
     * or with status {@code 500 (Internal Server Error)} if the insuranceType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InsuranceType> partialUpdateInsuranceType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody InsuranceType insuranceType
    ) throws URISyntaxException {
        log.debug("REST request to partial update InsuranceType partially : {}, {}", id, insuranceType);
        if (insuranceType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, insuranceType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!insuranceTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InsuranceType> result = insuranceTypeService.partialUpdate(insuranceType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, insuranceType.getId().toString())
        );
    }

    /**
     * {@code GET  /insurance-types} : get all the insuranceTypes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of insuranceTypes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<InsuranceType>> getAllInsuranceTypes(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of InsuranceTypes");
        Page<InsuranceType> page = insuranceTypeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /insurance-types/:id} : get the "id" insuranceType.
     *
     * @param id the id of the insuranceType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the insuranceType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<InsuranceType> getInsuranceType(@PathVariable("id") Long id) {
        log.debug("REST request to get InsuranceType : {}", id);
        Optional<InsuranceType> insuranceType = insuranceTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(insuranceType);
    }

    /**
     * {@code DELETE  /insurance-types/:id} : delete the "id" insuranceType.
     *
     * @param id the id of the insuranceType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInsuranceType(@PathVariable("id") Long id) {
        log.debug("REST request to delete InsuranceType : {}", id);
        insuranceTypeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
