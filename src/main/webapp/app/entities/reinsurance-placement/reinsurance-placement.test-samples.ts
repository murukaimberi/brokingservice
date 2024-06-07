import { IReinsurancePlacement, NewReinsurancePlacement } from './reinsurance-placement.model';

export const sampleWithRequiredData: IReinsurancePlacement = {
  id: 24369,
  riPercentageCommission: 88.05,
  riPercentageShare: 47.98,
  grossPremiumHundred: 13424.53,
};

export const sampleWithPartialData: IReinsurancePlacement = {
  id: 17379,
  riPercentageCommission: 58.07,
  riPercentageShare: 16.31,
  grossPremiumHundred: 32087.4,
  riCommission: 18593.13,
};

export const sampleWithFullData: IReinsurancePlacement = {
  id: 17629,
  riPercentageCommission: 79.43,
  riPercentageShare: 37.79,
  riTotalSumInsured: 7155.48,
  riLimitOfLiability: 14075.92,
  grossPremiumHundred: 28772.88,
  riPremium: 320.81,
  riCommission: 4925.07,
  netDueFromInsurer: 8475.39,
};

export const sampleWithNewData: NewReinsurancePlacement = {
  riPercentageCommission: 52.2,
  riPercentageShare: 20.6,
  grossPremiumHundred: 6046.54,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
