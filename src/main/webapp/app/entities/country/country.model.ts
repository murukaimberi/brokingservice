export interface ICountry {
  id: number;
  name?: string | null;
  currency?: string | null;
  currencyCode?: string | null;
}

export type NewCountry = Omit<ICountry, 'id'> & { id: null };
