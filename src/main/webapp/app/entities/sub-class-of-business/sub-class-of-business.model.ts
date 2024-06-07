import { IClassOfBusiness } from 'app/entities/class-of-business/class-of-business.model';

export interface ISubClassOfBusiness {
  id: number;
  name?: string | null;
  classOfBusiness?: IClassOfBusiness | null;
}

export type NewSubClassOfBusiness = Omit<ISubClassOfBusiness, 'id'> & { id: null };
