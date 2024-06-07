import dayjs from 'dayjs/esm';

import { INotification, NewNotification } from './notification.model';

export const sampleWithRequiredData: INotification = {
  id: 14468,
  message: 'stereotype patty ouch',
  notificationDate: dayjs('2024-06-07T03:09'),
  dueDate: dayjs('2024-06-06T16:45'),
  notificationStatus: 'IN_PROGRESS',
};

export const sampleWithPartialData: INotification = {
  id: 16525,
  message: 'lest',
  notificationDate: dayjs('2024-06-06T18:55'),
  dueDate: dayjs('2024-06-06T23:45'),
  notificationStatus: 'TODO',
};

export const sampleWithFullData: INotification = {
  id: 27174,
  message: 'oh while',
  notificationDate: dayjs('2024-06-06T20:03'),
  dueDate: dayjs('2024-06-07T01:44'),
  notificationStatus: 'IN_PROGRESS',
};

export const sampleWithNewData: NewNotification = {
  message: 'scripture',
  notificationDate: dayjs('2024-06-07T09:38'),
  dueDate: dayjs('2024-06-06T17:23'),
  notificationStatus: 'TODO',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
