import { IShare, NewShare } from './share.model';

export const sampleWithRequiredData: IShare = {
  id: 29282,
  sharePercentage: 58.7,
  brokerage: 72.67,
};

export const sampleWithPartialData: IShare = {
  id: 27577,
  sharePercentage: 81.04,
  totalSumInsured: 22597.71,
  grossPremium: 9637.64,
  brokerage: 51.86,
};

export const sampleWithFullData: IShare = {
  id: 4767,
  sharePercentage: 94.74,
  totalSumInsured: 24470.24,
  limitOfLiability: 505.58,
  grossPremium: 25275.65,
  riCommission: 12212.17,
  netPremium: 1338.22,
  brokerage: 31.25,
  brokerageAmount: 30952.02,
  netPayable: 29694.8,
};

export const sampleWithNewData: NewShare = {
  sharePercentage: 70.02,
  brokerage: 58.72,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
