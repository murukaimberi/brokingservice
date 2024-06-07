import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';

export interface IContractHistory {
  id: number;
  contractCreatedDate?: dayjs.Dayjs | null;
  contractActiveDate?: dayjs.Dayjs | null;
  contractInActiveDate?: dayjs.Dayjs | null;
  contractLastModifiedDate?: dayjs.Dayjs | null;
  changeDescription?: string | null;
  updated?: Pick<IUser, 'id' | 'login'> | null;
  approved?: Pick<IUser, 'id' | 'login'> | null;
}

export type NewContractHistory = Omit<IContractHistory, 'id'> & { id: null };
