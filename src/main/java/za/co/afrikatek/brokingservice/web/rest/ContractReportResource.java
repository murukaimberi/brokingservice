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
import za.co.afrikatek.brokingservice.domain.ContractReport;
import za.co.afrikatek.brokingservice.repository.ContractReportRepository;
import za.co.afrikatek.brokingservice.service.ContractReportService;
import za.co.afrikatek.brokingservice.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link za.co.afrikatek.brokingservice.domain.ContractReport}.
 */
@RestController
@RequestMapping("/api/contract-reports")
public class ContractReportResource {

    private final Logger log = LoggerFactory.getLogger(ContractReportResource.class);

    private static final String ENTITY_NAME = "contractReport";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContractReportService contractReportService;

    private final ContractReportRepository contractReportRepository;

    public ContractReportResource(ContractReportService contractReportService, ContractReportRepository contractReportRepository) {
        this.contractReportService = contractReportService;
        this.contractReportRepository = contractReportRepository;
    }

    /**
     * {@code POST  /contract-reports} : Create a new contractReport.
     *
     * @param contractReport the contractReport to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contractReport, or with status {@code 400 (Bad Request)} if the contractReport has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ContractReport> createContractReport(@Valid @RequestBody ContractReport contractReport)
        throws URISyntaxException {
        log.debug("REST request to save ContractReport : {}", contractReport);
        if (contractReport.getId() != null) {
            throw new BadRequestAlertException("A new contractReport cannot already have an ID", ENTITY_NAME, "idexists");
        }
        contractReport = contractReportService.save(contractReport);
        return ResponseEntity.created(new URI("/api/contract-reports/" + contractReport.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, contractReport.getId().toString()))
            .body(contractReport);
    }

    /**
     * {@code PUT  /contract-reports/:id} : Updates an existing contractReport.
     *
     * @param id the id of the contractReport to save.
     * @param contractReport the contractReport to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contractReport,
     * or with status {@code 400 (Bad Request)} if the contractReport is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contractReport couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ContractReport> updateContractReport(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ContractReport contractReport
    ) throws URISyntaxException {
        log.debug("REST request to update ContractReport : {}, {}", id, contractReport);
        if (contractReport.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contractReport.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contractReportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        contractReport = contractReportService.update(contractReport);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contractReport.getId().toString()))
            .body(contractReport);
    }

    /**
     * {@code PATCH  /contract-reports/:id} : Partial updates given fields of an existing contractReport, field will ignore if it is null
     *
     * @param id the id of the contractReport to save.
     * @param contractReport the contractReport to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contractReport,
     * or with status {@code 400 (Bad Request)} if the contractReport is not valid,
     * or with status {@code 404 (Not Found)} if the contractReport is not found,
     * or with status {@code 500 (Internal Server Error)} if the contractReport couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ContractReport> partialUpdateContractReport(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ContractReport contractReport
    ) throws URISyntaxException {
        log.debug("REST request to partial update ContractReport partially : {}, {}", id, contractReport);
        if (contractReport.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contractReport.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contractReportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ContractReport> result = contractReportService.partialUpdate(contractReport);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contractReport.getId().toString())
        );
    }

    /**
     * {@code GET  /contract-reports} : get all the contractReports.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contractReports in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ContractReport>> getAllContractReports(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of ContractReports");
        Page<ContractReport> page = contractReportService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /contract-reports/:id} : get the "id" contractReport.
     *
     * @param id the id of the contractReport to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contractReport, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ContractReport> getContractReport(@PathVariable("id") Long id) {
        log.debug("REST request to get ContractReport : {}", id);
        Optional<ContractReport> contractReport = contractReportService.findOne(id);
        return ResponseUtil.wrapOrNotFound(contractReport);
    }

    /**
     * {@code DELETE  /contract-reports/:id} : delete the "id" contractReport.
     *
     * @param id the id of the contractReport to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContractReport(@PathVariable("id") Long id) {
        log.debug("REST request to delete ContractReport : {}", id);
        contractReportService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
