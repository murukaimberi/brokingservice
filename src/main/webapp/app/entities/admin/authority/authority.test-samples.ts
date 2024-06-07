import { IAuthority, NewAuthority } from './authority.model';

export const sampleWithRequiredData: IAuthority = {
  name: '3d51fbe3-cb72-4fcc-8936-49902af77d2a',
};

export const sampleWithPartialData: IAuthority = {
  name: '193b5dab-dbc9-48ce-8622-25e2df1c66b9',
};

export const sampleWithFullData: IAuthority = {
  name: 'e7d432f3-b0c9-43b3-a394-f32f88cf215e',
};

export const sampleWithNewData: NewAuthority = {
  name: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
