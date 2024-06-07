import { IAddress, NewAddress } from './address.model';

export const sampleWithRequiredData: IAddress = {
  id: 28219,
  streetAddress: '../fake-data/blob/hipster.txt',
  city: 'Boehmstad',
  province: 'yum',
  zipCode: '52996-2272',
  country: 'Bermuda',
};

export const sampleWithPartialData: IAddress = {
  id: 7647,
  streetAddress: '../fake-data/blob/hipster.txt',
  city: 'Agustinfort',
  province: 'demerge conscious',
  state: 'rightfully',
  zipCode: '21813',
  country: 'El Salvador',
};

export const sampleWithFullData: IAddress = {
  id: 16153,
  streetAddress: '../fake-data/blob/hipster.txt',
  city: 'Abernathybury',
  province: 'gap considerate heavily',
  state: 'although modernity',
  zipCode: '32226-1932',
  country: 'Azerbaijan',
};

export const sampleWithNewData: NewAddress = {
  streetAddress: '../fake-data/blob/hipster.txt',
  city: 'Blacksburg',
  province: 'dismantle tomorrow',
  zipCode: '47548',
  country: 'Gabon',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
