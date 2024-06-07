import { IClassOfBusiness, NewClassOfBusiness } from './class-of-business.model';

export const sampleWithRequiredData: IClassOfBusiness = {
  id: 25001,
};

export const sampleWithPartialData: IClassOfBusiness = {
  id: 8077,
};

export const sampleWithFullData: IClassOfBusiness = {
  id: 1705,
  name: 'phooey',
};

export const sampleWithNewData: NewClassOfBusiness = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
