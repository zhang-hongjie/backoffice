import { Component } from '@angular/core';
import { Notification } from './shared';
import { NotificationService } from './notification.service';

@Component({
    selector: 'backoffice-notification',
    styles: [require('./notification.scss')],
    template: require('./notification.component.html'),
})

export class NotificationComponent {
    private notes: Notification[] = new Array<Notification>();

    constructor(private notificationService: NotificationService) {
        notificationService.noteAdded.subscribe(note => {
            this.notes.push(note);
            setTimeout(() => {
                this.close(note);
            }, 5000);
        });
    }

    private close (note: Notification) {
        let index = this.notes.indexOf(note);
        if (index >= 0) {
            this.notes.splice(index, 1);
        }
    }
}
