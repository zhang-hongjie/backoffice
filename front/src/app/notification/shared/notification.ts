import { NotificationType } from './notification-type';

export class Notification {
    constructor(public type: NotificationType,
                public message = '') {
    }
}
