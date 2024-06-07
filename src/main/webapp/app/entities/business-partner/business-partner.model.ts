import { IContract } from 'app/entities/contract/contract.model';
import { InsuranceAgentType } from 'app/entities/enumerations/insurance-agent-type.model';

export interface IBusinessPartner {
  id: number;
  name?: string | null;
  description?: string | null;
  representativeName?: string | null;
  email?: string | null;
  phoneNumber?: string | null;
  agentType?: keyof typeof InsuranceAgentType | null;
  reInsurerContracts?: IContract[] | null;
}

export type NewBusinessPartner = Omit<IBusinessPartner, 'id'> & { id: null };
