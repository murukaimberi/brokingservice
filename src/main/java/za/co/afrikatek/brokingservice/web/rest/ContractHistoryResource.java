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
import za.co.afrikatek.brokingservice.domain.ContractHistory;
import za.co.afrikatek.brokingservice.repository.ContractHistoryRepository;
import za.co.afrikatek.brokingservice.service.ContractHistoryService;
import za.co.afrikatek.brokingservice.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link za.co.afrikatek.brokingservice.domain.ContractHistory}.
 */
@RestController
@RequestMapping("/api/contract-histories")
public class ContractHistoryResource {

    private final Logger log = LoggerFactory.getLogger(ContractHistoryResource.class);

    private static final String ENTITY_NAME = "contractHistory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContractHistoryService contractHistoryService;

    private final ContractHistoryRepository contractHistoryRepository;

    public ContractHistoryResource(ContractHistoryService contractHistoryService, ContractHistoryRepository contractHistoryRepository) {
        this.contractHistoryService = contractHistoryService;
        this.contractHistoryRepository = contractHistoryRepository;
    }

    /**
     * {@code POST  /contract-histories} : Create a new contractHistory.
     *
     * @param contractHistory the contractHistory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contractHistory, or with status {@code 400 (Bad Request)} if the contractHistory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ContractHistory> createContractHistory(@Valid @RequestBody ContractHistory contractHistory)
        throws URISyntaxException {
        log.debug("REST request to save ContractHistory : {}", contractHistory);
        if (contractHistory.getId() != null) {
            throw new BadRequestAlertException("A new contractHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        contractHistory = contractHistoryService.save(contractHistory);
        return ResponseEntity.created(new URI("/api/contract-histories/" + contractHistory.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, contractHistory.getId().toString()))
            .body(contractHistory);
    }

    /**
     * {@code PUT  /contract-histories/:id} : Updates an existing contractHistory.
     *
     * @param id the id of the contractHistory to save.
     * @param contractHistory the contractHistory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contractHistory,
     * or with status {@code 400 (Bad Request)} if the contractHistory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contractHistory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ContractHistory> updateContractHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ContractHistory contractHistory
    ) throws URISyntaxException {
        log.debug("REST request to update ContractHistory : {}, {}", id, contractHistory);
        if (contractHistory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contractHistory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contractHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        contractHistory = contractHistoryService.update(contractHistory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contractHistory.getId().toString()))
            .body(contractHistory);
    }

    /**
     * {@code PATCH  /contract-histories/:id} : Partial updates given fields of an existing contractHistory, field will ignore if it is null
     *
     * @param id the id of the contractHistory to save.
     * @param contractHistory the contractHistory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contractHistory,
     * or with status {@code 400 (Bad Request)} if the contractHistory is not valid,
     * or with status {@code 404 (Not Found)} if the contractHistory is not found,
     * or with status {@code 500 (Internal Server Error)} if the contractHistory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ContractHistory> partialUpdateContractHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ContractHistory contractHistory
    ) throws URISyntaxException {
        log.debug("REST request to partial update ContractHistory partially : {}, {}", id, contractHistory);
        if (contractHistory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contractHistory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contractHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ContractHistory> result = contractHistoryService.partialUpdate(contractHistory);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contractHistory.getId().toString())
        );
    }

    /**
     * {@code GET  /contract-histories} : get all the contractHistories.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contractHistories in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ContractHistory>> getAllContractHistories(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of ContractHistories");
        Page<ContractHistory> page;
        if (eagerload) {
            page = contractHistoryService.findAllWithEagerRelationships(pageable);
        } else {
            page = contractHistoryService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /contract-histories/:id} : get the "id" contractHistory.
     *
     * @param id the id of the contractHistory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contractHistory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ContractHistory> getContractHistory(@PathVariable("id") Long id) {
        log.debug("REST request to get ContractHistory : {}", id);
        Optional<ContractHistory> contractHistory = contractHistoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(contractHistory);
    }

    /**
     * {@code DELETE  /contract-histories/:id} : delete the "id" contractHistory.
     *
     * @param id the id of the contractHistory to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContractHistory(@PathVariable("id") Long id) {
        log.debug("REST request to delete ContractHistory : {}", id);
        contractHistoryService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
