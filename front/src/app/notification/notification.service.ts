import { Injectable } from '@angular/core';
import { Subject } from 'rxjs/Subject';
import { Notification } from './shared';
import { NotificationType } from './shared/notification-type';

@Injectable()
export class NotificationService {
    public notifications = new Subject<Notification>();

    public noteAdded = this.notifications.asObservable();

    private notify(notification: Notification) {
        this.notifications.next(notification);
    }

    public error(message: string) {
        this.notify(new Notification(NotificationType.ERROR, message));
    }

    public success(message: string) {
        this.notify(new Notification(NotificationType.SUCCESS, message));
    }

    public info(message: string) {
        this.notify(new Notification(NotificationType.INFO, message));
    }

    public warn(message: string) {
        this.notify(new Notification(NotificationType.WARN, message));
    }
}
