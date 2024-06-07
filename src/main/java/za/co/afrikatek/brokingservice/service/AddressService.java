package za.co.afrikatek.brokingservice.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.co.afrikatek.brokingservice.domain.Address;
import za.co.afrikatek.brokingservice.repository.AddressRepository;

/**
 * Service Implementation for managing {@link za.co.afrikatek.brokingservice.domain.Address}.
 */
@Service
@Transactional
public class AddressService {

    private final Logger log = LoggerFactory.getLogger(AddressService.class);

    private final AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    /**
     * Save a address.
     *
     * @param address the entity to save.
     * @return the persisted entity.
     */
    public Address save(Address address) {
        log.debug("Request to save Address : {}", address);
        return addressRepository.save(address);
    }

    /**
     * Update a address.
     *
     * @param address the entity to save.
     * @return the persisted entity.
     */
    public Address update(Address address) {
        log.debug("Request to update Address : {}", address);
        return addressRepository.save(address);
    }

    /**
     * Partially update a address.
     *
     * @param address the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Address> partialUpdate(Address address) {
        log.debug("Request to partially update Address : {}", address);

        return addressRepository
            .findById(address.getId())
            .map(existingAddress -> {
                if (address.getStreetAddress() != null) {
                    existingAddress.setStreetAddress(address.getStreetAddress());
                }
                if (address.getCity() != null) {
                    existingAddress.setCity(address.getCity());
                }
                if (address.getProvince() != null) {
                    existingAddress.setProvince(address.getProvince());
                }
                if (address.getState() != null) {
                    existingAddress.setState(address.getState());
                }
                if (address.getZipCode() != null) {
                    existingAddress.setZipCode(address.getZipCode());
                }
                if (address.getCountry() != null) {
                    existingAddress.setCountry(address.getCountry());
                }

                return existingAddress;
            })
            .map(addressRepository::save);
    }

    /**
     * Get all the addresses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Address> findAll(Pageable pageable) {
        log.debug("Request to get all Addresses");
        return addressRepository.findAll(pageable);
    }

    /**
     * Get all the addresses with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Address> findAllWithEagerRelationships(Pageable pageable) {
        return addressRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one address by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Address> findOne(Long id) {
        log.debug("Request to get Address : {}", id);
        return addressRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the address by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Address : {}", id);
        addressRepository.deleteById(id);
    }
}
