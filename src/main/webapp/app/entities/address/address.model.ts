import { IBusinessPartner } from 'app/entities/business-partner/business-partner.model';

export interface IAddress {
  id: number;
  streetAddress?: string | null;
  city?: string | null;
  province?: string | null;
  state?: string | null;
  zipCode?: string | null;
  country?: string | null;
  cedent?: IBusinessPartner | null;
}

export type NewAddress = Omit<IAddress, 'id'> & { id: null };
