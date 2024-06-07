import { IInsuranceType, NewInsuranceType } from './insurance-type.model';

export const sampleWithRequiredData: IInsuranceType = {
  id: 32310,
  name: 'unscrew instead whispering',
};

export const sampleWithPartialData: IInsuranceType = {
  id: 1997,
  name: 'deracinate unless longingly',
};

export const sampleWithFullData: IInsuranceType = {
  id: 22304,
  name: 'catalogue',
};

export const sampleWithNewData: NewInsuranceType = {
  name: 'ew onto',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
