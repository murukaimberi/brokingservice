import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IBusinessPartner } from 'app/entities/business-partner/business-partner.model';
import { BusinessPartnerService } from 'app/entities/business-partner/service/business-partner.service';
import { IClassOfBusiness } from 'app/entities/class-of-business/class-of-business.model';
import { ClassOfBusinessService } from 'app/entities/class-of-business/service/class-of-business.service';
import { ISubClassOfBusiness } from 'app/entities/sub-class-of-business/sub-class-of-business.model';
import { SubClassOfBusinessService } from 'app/entities/sub-class-of-business/service/sub-class-of-business.service';
import { ICountry } from 'app/entities/country/country.model';
import { CountryService } from 'app/entities/country/service/country.service';
import { ContractType } from 'app/entities/enumerations/contract-type.model';
import { ContractSubType } from 'app/entities/enumerations/contract-sub-type.model';
import { ContractStatus } from 'app/entities/enumerations/contract-status.model';
import { ContractService } from '../service/contract.service';
import { IContract } from '../contract.model';
import { ContractFormService, ContractFormGroup } from './contract-form.service';

@Component({
  standalone: true,
  selector: 'jhi-contract-update',
  templateUrl: './contract-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ContractUpdateComponent implements OnInit {
  isSaving = false;
  contract: IContract | null = null;
  contractTypeValues = Object.keys(ContractType);
  contractSubTypeValues = Object.keys(ContractSubType);
  contractStatusValues = Object.keys(ContractStatus);

  businessPartnersSharedCollection: IBusinessPartner[] = [];
  classOfBusinessesSharedCollection: IClassOfBusiness[] = [];
  subClassOfBusinessesSharedCollection: ISubClassOfBusiness[] = [];
  countriesSharedCollection: ICountry[] = [];

  protected contractService = inject(ContractService);
  protected contractFormService = inject(ContractFormService);
  protected businessPartnerService = inject(BusinessPartnerService);
  protected classOfBusinessService = inject(ClassOfBusinessService);
  protected subClassOfBusinessService = inject(SubClassOfBusinessService);
  protected countryService = inject(CountryService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ContractFormGroup = this.contractFormService.createContractFormGroup();

  compareBusinessPartner = (o1: IBusinessPartner | null, o2: IBusinessPartner | null): boolean =>
    this.businessPartnerService.compareBusinessPartner(o1, o2);

  compareClassOfBusiness = (o1: IClassOfBusiness | null, o2: IClassOfBusiness | null): boolean =>
    this.classOfBusinessService.compareClassOfBusiness(o1, o2);

  compareSubClassOfBusiness = (o1: ISubClassOfBusiness | null, o2: ISubClassOfBusiness | null): boolean =>
    this.subClassOfBusinessService.compareSubClassOfBusiness(o1, o2);

  compareCountry = (o1: ICountry | null, o2: ICountry | null): boolean => this.countryService.compareCountry(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contract }) => {
      this.contract = contract;
      if (contract) {
        this.updateForm(contract);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const contract = this.contractFormService.getContract(this.editForm);
    if (contract.id !== null) {
      this.subscribeToSaveResponse(this.contractService.update(contract));
    } else {
      this.subscribeToSaveResponse(this.contractService.create(contract));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IContract>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(contract: IContract): void {
    this.contract = contract;
    this.contractFormService.resetForm(this.editForm, contract);

    this.businessPartnersSharedCollection = this.businessPartnerService.addBusinessPartnerToCollectionIfMissing<IBusinessPartner>(
      this.businessPartnersSharedCollection,
      contract.insured,
      contract.insurer,
      contract.broker,
      ...(contract.reinsurers ?? []),
    );
    this.classOfBusinessesSharedCollection = this.classOfBusinessService.addClassOfBusinessToCollectionIfMissing<IClassOfBusiness>(
      this.classOfBusinessesSharedCollection,
      contract.classOfBusiness,
    );
    this.subClassOfBusinessesSharedCollection =
      this.subClassOfBusinessService.addSubClassOfBusinessToCollectionIfMissing<ISubClassOfBusiness>(
        this.subClassOfBusinessesSharedCollection,
        contract.subClassOfBusiness,
      );
    this.countriesSharedCollection = this.countryService.addCountryToCollectionIfMissing<ICountry>(
      this.countriesSharedCollection,
      contract.country,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.businessPartnerService
      .query()
      .pipe(map((res: HttpResponse<IBusinessPartner[]>) => res.body ?? []))
      .pipe(
        map((businessPartners: IBusinessPartner[]) =>
          this.businessPartnerService.addBusinessPartnerToCollectionIfMissing<IBusinessPartner>(
            businessPartners,
            this.contract?.insured,
            this.contract?.insurer,
            this.contract?.broker,
            ...(this.contract?.reinsurers ?? []),
          ),
        ),
      )
      .subscribe((businessPartners: IBusinessPartner[]) => (this.businessPartnersSharedCollection = businessPartners));

    this.classOfBusinessService
      .query()
      .pipe(map((res: HttpResponse<IClassOfBusiness[]>) => res.body ?? []))
      .pipe(
        map((classOfBusinesses: IClassOfBusiness[]) =>
          this.classOfBusinessService.addClassOfBusinessToCollectionIfMissing<IClassOfBusiness>(
            classOfBusinesses,
            this.contract?.classOfBusiness,
          ),
        ),
      )
      .subscribe((classOfBusinesses: IClassOfBusiness[]) => (this.classOfBusinessesSharedCollection = classOfBusinesses));

    this.subClassOfBusinessService
      .query()
      .pipe(map((res: HttpResponse<ISubClassOfBusiness[]>) => res.body ?? []))
      .pipe(
        map((subClassOfBusinesses: ISubClassOfBusiness[]) =>
          this.subClassOfBusinessService.addSubClassOfBusinessToCollectionIfMissing<ISubClassOfBusiness>(
            subClassOfBusinesses,
            this.contract?.subClassOfBusiness,
          ),
        ),
      )
      .subscribe((subClassOfBusinesses: ISubClassOfBusiness[]) => (this.subClassOfBusinessesSharedCollection = subClassOfBusinesses));

    this.countryService
      .query()
      .pipe(map((res: HttpResponse<ICountry[]>) => res.body ?? []))
      .pipe(
        map((countries: ICountry[]) => this.countryService.addCountryToCollectionIfMissing<ICountry>(countries, this.contract?.country)),
      )
      .subscribe((countries: ICountry[]) => (this.countriesSharedCollection = countries));
  }
}
