import { ISubClassOfBusiness, NewSubClassOfBusiness } from './sub-class-of-business.model';

export const sampleWithRequiredData: ISubClassOfBusiness = {
  id: 16945,
};

export const sampleWithPartialData: ISubClassOfBusiness = {
  id: 15423,
  name: 'lawn',
};

export const sampleWithFullData: ISubClassOfBusiness = {
  id: 990,
  name: 'duh times diligently',
};

export const sampleWithNewData: NewSubClassOfBusiness = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
