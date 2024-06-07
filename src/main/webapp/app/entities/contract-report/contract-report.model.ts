import dayjs from 'dayjs/esm';

export interface IContractReport {
  id: number;
  contractDocument?: string | null;
  contractDocumentContentType?: string | null;
  createdDate?: dayjs.Dayjs | null;
}

export type NewContractReport = Omit<IContractReport, 'id'> & { id: null };
