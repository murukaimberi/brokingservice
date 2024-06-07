import { ICountry, NewCountry } from './country.model';

export const sampleWithRequiredData: ICountry = {
  id: 3253,
  name: 'whereas',
  currency: 'cleverly yum',
  currencyCode: 'KGS',
};

export const sampleWithPartialData: ICountry = {
  id: 18970,
  name: 'honestly who',
  currency: 'lest during anti',
  currencyCode: 'USD',
};

export const sampleWithFullData: ICountry = {
  id: 27044,
  name: 'longingly gain',
  currency: 'modulo inwardly ah',
  currencyCode: 'NAD',
};

export const sampleWithNewData: NewCountry = {
  name: 'mecca weighty sans',
  currency: 'black including acidly',
  currencyCode: 'SAR',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
