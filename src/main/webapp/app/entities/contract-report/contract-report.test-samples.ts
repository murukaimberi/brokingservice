import dayjs from 'dayjs/esm';

import { IContractReport, NewContractReport } from './contract-report.model';

export const sampleWithRequiredData: IContractReport = {
  id: 30574,
  contractDocument: '../fake-data/blob/hipster.png',
  contractDocumentContentType: 'unknown',
  createdDate: dayjs('2024-06-06T21:27'),
};

export const sampleWithPartialData: IContractReport = {
  id: 23314,
  contractDocument: '../fake-data/blob/hipster.png',
  contractDocumentContentType: 'unknown',
  createdDate: dayjs('2024-06-07T05:06'),
};

export const sampleWithFullData: IContractReport = {
  id: 749,
  contractDocument: '../fake-data/blob/hipster.png',
  contractDocumentContentType: 'unknown',
  createdDate: dayjs('2024-06-07T04:23'),
};

export const sampleWithNewData: NewContractReport = {
  contractDocument: '../fake-data/blob/hipster.png',
  contractDocumentContentType: 'unknown',
  createdDate: dayjs('2024-06-06T12:14'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
