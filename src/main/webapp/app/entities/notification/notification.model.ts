import dayjs from 'dayjs/esm';
import { NotificationStatus } from 'app/entities/enumerations/notification-status.model';

export interface INotification {
  id: number;
  message?: string | null;
  notificationDate?: dayjs.Dayjs | null;
  dueDate?: dayjs.Dayjs | null;
  notificationStatus?: keyof typeof NotificationStatus | null;
}

export type NewNotification = Omit<INotification, 'id'> & { id: null };
