import dayjs from 'dayjs/esm';

import { IContractHistory, NewContractHistory } from './contract-history.model';

export const sampleWithRequiredData: IContractHistory = {
  id: 237,
  contractCreatedDate: dayjs('2024-06-07T02:46'),
  contractActiveDate: dayjs('2024-06-07T04:51'),
  changeDescription: '../fake-data/blob/hipster.txt',
};

export const sampleWithPartialData: IContractHistory = {
  id: 28050,
  contractCreatedDate: dayjs('2024-06-06T14:44'),
  contractActiveDate: dayjs('2024-06-06T22:08'),
  contractLastModifiedDate: dayjs('2024-06-06T18:03'),
  changeDescription: '../fake-data/blob/hipster.txt',
};

export const sampleWithFullData: IContractHistory = {
  id: 16347,
  contractCreatedDate: dayjs('2024-06-07T10:47'),
  contractActiveDate: dayjs('2024-06-06T19:59'),
  contractInActiveDate: dayjs('2024-06-06T14:27'),
  contractLastModifiedDate: dayjs('2024-06-06T22:57'),
  changeDescription: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewContractHistory = {
  contractCreatedDate: dayjs('2024-06-07T08:19'),
  contractActiveDate: dayjs('2024-06-07T09:23'),
  changeDescription: '../fake-data/blob/hipster.txt',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
