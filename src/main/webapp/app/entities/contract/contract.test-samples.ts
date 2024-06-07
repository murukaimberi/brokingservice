import dayjs from 'dayjs/esm';

import { IContract, NewContract } from './contract.model';

export const sampleWithRequiredData: IContract = {
  id: 25864,
  type: 'TREATY',
  subType: 'PROPORTIONAL',
  inception: dayjs('2024-06-06T12:14'),
  expiry: dayjs('2024-06-07T00:34'),
  currency: 'sheepishly photodiode',
  totalSumInsured: 31580.31,
  limitOfLiability: 14741.43,
  status: 'UPDATE_APPROVED',
};

export const sampleWithPartialData: IContract = {
  id: 3554,
  type: 'FACULTATIVE',
  subType: 'PROPORTIONAL',
  inception: dayjs('2024-06-07T03:37'),
  expiry: dayjs('2024-06-06T19:09'),
  currency: 'clear bah',
  totalSumInsured: 4265.47,
  limitOfLiability: 2703.66,
  status: 'CREATED_SHARE',
};

export const sampleWithFullData: IContract = {
  id: 32390,
  type: 'FACULTATIVE',
  subType: 'PROPORTIONAL',
  inception: dayjs('2024-06-06T21:26'),
  expiry: dayjs('2024-06-07T02:43'),
  currency: 'honored',
  totalSumInsured: 4167.06,
  limitOfLiability: 6241.28,
  uuid: '382fa43c-cde2-431b-bdd3-08a43ccec77e',
  status: 'CREATED_APPROVED',
  active: true,
};

export const sampleWithNewData: NewContract = {
  type: 'FACULTATIVE',
  subType: 'PROPORTIONAL',
  inception: dayjs('2024-06-06T18:59'),
  expiry: dayjs('2024-06-06T16:49'),
  currency: 'ugly alliance',
  totalSumInsured: 11168.27,
  limitOfLiability: 9974.7,
  status: 'CREATED_SHARE',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
