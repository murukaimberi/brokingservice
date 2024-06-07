import { IBusinessPartner } from 'app/entities/business-partner/business-partner.model';

export interface IShare {
  id: number;
  sharePercentage?: number | null;
  totalSumInsured?: number | null;
  limitOfLiability?: number | null;
  grossPremium?: number | null;
  riCommission?: number | null;
  netPremium?: number | null;
  brokerage?: number | null;
  brokerageAmount?: number | null;
  netPayable?: number | null;
  reInsurer?: IBusinessPartner | null;
}

export type NewShare = Omit<IShare, 'id'> & { id: null };
