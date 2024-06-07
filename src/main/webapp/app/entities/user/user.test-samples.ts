import { IUser } from './user.model';

export const sampleWithRequiredData: IUser = {
  id: 17153,
  login: 'UyE',
};

export const sampleWithPartialData: IUser = {
  id: 4216,
  login: 'C@okpVX\\MC97Py\\?F\\*aUtc\\x57dUQ\\W2',
};

export const sampleWithFullData: IUser = {
  id: 17682,
  login: 'oYB5',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
