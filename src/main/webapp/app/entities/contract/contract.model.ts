import dayjs from 'dayjs/esm';
import { IBusinessPartner } from 'app/entities/business-partner/business-partner.model';
import { IClassOfBusiness } from 'app/entities/class-of-business/class-of-business.model';
import { ISubClassOfBusiness } from 'app/entities/sub-class-of-business/sub-class-of-business.model';
import { ICountry } from 'app/entities/country/country.model';
import { ContractType } from 'app/entities/enumerations/contract-type.model';
import { ContractSubType } from 'app/entities/enumerations/contract-sub-type.model';
import { ContractStatus } from 'app/entities/enumerations/contract-status.model';

export interface IContract {
  id: number;
  type?: keyof typeof ContractType | null;
  subType?: keyof typeof ContractSubType | null;
  inception?: dayjs.Dayjs | null;
  expiry?: dayjs.Dayjs | null;
  currency?: string | null;
  totalSumInsured?: number | null;
  limitOfLiability?: number | null;
  uuid?: string | null;
  status?: keyof typeof ContractStatus | null;
  active?: boolean | null;
  insured?: IBusinessPartner | null;
  insurer?: IBusinessPartner | null;
  broker?: IBusinessPartner | null;
  reinsurers?: IBusinessPartner[] | null;
  classOfBusiness?: IClassOfBusiness | null;
  subClassOfBusiness?: ISubClassOfBusiness | null;
  country?: ICountry | null;
}

export type NewContract = Omit<IContract, 'id'> & { id: null };
