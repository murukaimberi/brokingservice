import { IBusinessPartner, NewBusinessPartner } from './business-partner.model';

export const sampleWithRequiredData: IBusinessPartner = {
  id: 17821,
  name: 'some provided deviance',
  description: '../fake-data/blob/hipster.txt',
  representativeName: 'resize than ugh',
  email: 'Reina90@gmail.com',
  phoneNumber: 'vice disseminate',
  agentType: 'INSURER',
};

export const sampleWithPartialData: IBusinessPartner = {
  id: 24575,
  name: 'respite besides innocently',
  description: '../fake-data/blob/hipster.txt',
  representativeName: 'huzzah',
  email: 'Jeffrey.Emard@hotmail.com',
  phoneNumber: 'barring dolor',
  agentType: 'BROKER',
};

export const sampleWithFullData: IBusinessPartner = {
  id: 26690,
  name: 'gregarious ack',
  description: '../fake-data/blob/hipster.txt',
  representativeName: 'supposing patter',
  email: 'Zella.Roberts@hotmail.com',
  phoneNumber: 'sunny concede',
  agentType: 'RE_INSURER',
};

export const sampleWithNewData: NewBusinessPartner = {
  name: 'furrow midst return',
  description: '../fake-data/blob/hipster.txt',
  representativeName: 'socks',
  email: 'Rachel84@yahoo.com',
  phoneNumber: 'outlaw marshal solid',
  agentType: 'RE_INSURER',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
