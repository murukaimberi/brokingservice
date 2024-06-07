import { IInsuranceType } from 'app/entities/insurance-type/insurance-type.model';

export interface IClassOfBusiness {
  id: number;
  name?: string | null;
  insuranceType?: IInsuranceType | null;
}

export type NewClassOfBusiness = Omit<IClassOfBusiness, 'id'> & { id: null };
