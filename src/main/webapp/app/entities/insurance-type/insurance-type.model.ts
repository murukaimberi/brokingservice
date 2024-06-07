export interface IInsuranceType {
  id: number;
  name?: string | null;
}

export type NewInsuranceType = Omit<IInsuranceType, 'id'> & { id: null };
