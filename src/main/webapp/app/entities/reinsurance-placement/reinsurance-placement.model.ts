import { IContract } from 'app/entities/contract/contract.model';

export interface IReinsurancePlacement {
  id: number;
  riPercentageCommission?: number | null;
  riPercentageShare?: number | null;
  riTotalSumInsured?: number | null;
  riLimitOfLiability?: number | null;
  grossPremiumHundred?: number | null;
  riPremium?: number | null;
  riCommission?: number | null;
  netDueFromInsurer?: number | null;
  contract?: IContract | null;
}

export type NewReinsurancePlacement = Omit<IReinsurancePlacement, 'id'> & { id: null };
