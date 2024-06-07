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
import za.co.afrikatek.brokingservice.domain.BusinessPartner;
import za.co.afrikatek.brokingservice.repository.BusinessPartnerRepository;
import za.co.afrikatek.brokingservice.service.BusinessPartnerService;
import za.co.afrikatek.brokingservice.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link za.co.afrikatek.brokingservice.domain.BusinessPartner}.
 */
@RestController
@RequestMapping("/api/business-partners")
public class BusinessPartnerResource {

    private final Logger log = LoggerFactory.getLogger(BusinessPartnerResource.class);

    private static final String ENTITY_NAME = "businessPartner";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BusinessPartnerService businessPartnerService;

    private final BusinessPartnerRepository businessPartnerRepository;

    public BusinessPartnerResource(BusinessPartnerService businessPartnerService, BusinessPartnerRepository businessPartnerRepository) {
        this.businessPartnerService = businessPartnerService;
        this.businessPartnerRepository = businessPartnerRepository;
    }

    /**
     * {@code POST  /business-partners} : Create a new businessPartner.
     *
     * @param businessPartner the businessPartner to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new businessPartner, or with status {@code 400 (Bad Request)} if the businessPartner has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<BusinessPartner> createBusinessPartner(@Valid @RequestBody BusinessPartner businessPartner)
        throws URISyntaxException {
        log.debug("REST request to save BusinessPartner : {}", businessPartner);
        if (businessPartner.getId() != null) {
            throw new BadRequestAlertException("A new businessPartner cannot already have an ID", ENTITY_NAME, "idexists");
        }
        businessPartner = businessPartnerService.save(businessPartner);
        return ResponseEntity.created(new URI("/api/business-partners/" + businessPartner.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, businessPartner.getId().toString()))
            .body(businessPartner);
    }

    /**
     * {@code PUT  /business-partners/:id} : Updates an existing businessPartner.
     *
     * @param id the id of the businessPartner to save.
     * @param businessPartner the businessPartner to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated businessPartner,
     * or with status {@code 400 (Bad Request)} if the businessPartner is not valid,
     * or with status {@code 500 (Internal Server Error)} if the businessPartner couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BusinessPartner> updateBusinessPartner(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BusinessPartner businessPartner
    ) throws URISyntaxException {
        log.debug("REST request to update BusinessPartner : {}, {}", id, businessPartner);
        if (businessPartner.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, businessPartner.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!businessPartnerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        businessPartner = businessPartnerService.update(businessPartner);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, businessPartner.getId().toString()))
            .body(businessPartner);
    }

    /**
     * {@code PATCH  /business-partners/:id} : Partial updates given fields of an existing businessPartner, field will ignore if it is null
     *
     * @param id the id of the businessPartner to save.
     * @param businessPartner the businessPartner to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated businessPartner,
     * or with status {@code 400 (Bad Request)} if the businessPartner is not valid,
     * or with status {@code 404 (Not Found)} if the businessPartner is not found,
     * or with status {@code 500 (Internal Server Error)} if the businessPartner couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BusinessPartner> partialUpdateBusinessPartner(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BusinessPartner businessPartner
    ) throws URISyntaxException {
        log.debug("REST request to partial update BusinessPartner partially : {}, {}", id, businessPartner);
        if (businessPartner.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, businessPartner.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!businessPartnerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BusinessPartner> result = businessPartnerService.partialUpdate(businessPartner);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, businessPartner.getId().toString())
        );
    }

    /**
     * {@code GET  /business-partners} : get all the businessPartners.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of businessPartners in body.
     */
    @GetMapping("")
    public ResponseEntity<List<BusinessPartner>> getAllBusinessPartners(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of BusinessPartners");
        Page<BusinessPartner> page = businessPartnerService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /business-partners/:id} : get the "id" businessPartner.
     *
     * @param id the id of the businessPartner to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the businessPartner, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BusinessPartner> getBusinessPartner(@PathVariable("id") Long id) {
        log.debug("REST request to get BusinessPartner : {}", id);
        Optional<BusinessPartner> businessPartner = businessPartnerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(businessPartner);
    }

    /**
     * {@code DELETE  /business-partners/:id} : delete the "id" businessPartner.
     *
     * @param id the id of the businessPartner to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBusinessPartner(@PathVariable("id") Long id) {
        log.debug("REST request to delete BusinessPartner : {}", id);
        businessPartnerService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
