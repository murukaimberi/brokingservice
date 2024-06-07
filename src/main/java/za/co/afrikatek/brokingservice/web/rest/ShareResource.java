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
import za.co.afrikatek.brokingservice.domain.Share;
import za.co.afrikatek.brokingservice.repository.ShareRepository;
import za.co.afrikatek.brokingservice.service.ShareService;
import za.co.afrikatek.brokingservice.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link za.co.afrikatek.brokingservice.domain.Share}.
 */
@RestController
@RequestMapping("/api/shares")
public class ShareResource {

    private final Logger log = LoggerFactory.getLogger(ShareResource.class);

    private static final String ENTITY_NAME = "share";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ShareService shareService;

    private final ShareRepository shareRepository;

    public ShareResource(ShareService shareService, ShareRepository shareRepository) {
        this.shareService = shareService;
        this.shareRepository = shareRepository;
    }

    /**
     * {@code POST  /shares} : Create a new share.
     *
     * @param share the share to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new share, or with status {@code 400 (Bad Request)} if the share has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Share> createShare(@Valid @RequestBody Share share) throws URISyntaxException {
        log.debug("REST request to save Share : {}", share);
        if (share.getId() != null) {
            throw new BadRequestAlertException("A new share cannot already have an ID", ENTITY_NAME, "idexists");
        }
        share = shareService.save(share);
        return ResponseEntity.created(new URI("/api/shares/" + share.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, share.getId().toString()))
            .body(share);
    }

    /**
     * {@code PUT  /shares/:id} : Updates an existing share.
     *
     * @param id the id of the share to save.
     * @param share the share to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated share,
     * or with status {@code 400 (Bad Request)} if the share is not valid,
     * or with status {@code 500 (Internal Server Error)} if the share couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Share> updateShare(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Share share)
        throws URISyntaxException {
        log.debug("REST request to update Share : {}, {}", id, share);
        if (share.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, share.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shareRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        share = shareService.update(share);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, share.getId().toString()))
            .body(share);
    }

    /**
     * {@code PATCH  /shares/:id} : Partial updates given fields of an existing share, field will ignore if it is null
     *
     * @param id the id of the share to save.
     * @param share the share to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated share,
     * or with status {@code 400 (Bad Request)} if the share is not valid,
     * or with status {@code 404 (Not Found)} if the share is not found,
     * or with status {@code 500 (Internal Server Error)} if the share couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Share> partialUpdateShare(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Share share
    ) throws URISyntaxException {
        log.debug("REST request to partial update Share partially : {}, {}", id, share);
        if (share.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, share.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shareRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Share> result = shareService.partialUpdate(share);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, share.getId().toString())
        );
    }

    /**
     * {@code GET  /shares} : get all the shares.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of shares in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Share>> getAllShares(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of Shares");
        Page<Share> page;
        if (eagerload) {
            page = shareService.findAllWithEagerRelationships(pageable);
        } else {
            page = shareService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /shares/:id} : get the "id" share.
     *
     * @param id the id of the share to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the share, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Share> getShare(@PathVariable("id") Long id) {
        log.debug("REST request to get Share : {}", id);
        Optional<Share> share = shareService.findOne(id);
        return ResponseUtil.wrapOrNotFound(share);
    }

    /**
     * {@code DELETE  /shares/:id} : delete the "id" share.
     *
     * @param id the id of the share to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShare(@PathVariable("id") Long id) {
        log.debug("REST request to delete Share : {}", id);
        shareService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
