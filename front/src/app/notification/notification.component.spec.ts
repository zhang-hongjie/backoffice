import { async, TestBed } from '@angular/core/testing';
import { NotificationComponent, NotificationService } from '../notification';
import { NotificationType } from './shared/notification-type';

let fixture: any;
let notificationComponent: any;
let notificationComponentCompiled: any;

describe('NotificationComponent', () => {
    beforeEach(() => {
        TestBed.configureTestingModule({
            declarations: [NotificationComponent],
            providers: [
                NotificationService
            ]
        });
        fixture = TestBed.createComponent(NotificationComponent);
        notificationComponent = fixture.componentInstance;
        notificationComponentCompiled = fixture.debugElement.nativeElement;
        fixture.detectChanges();
    });

    it('should create the component', async(() => {
        expect(fixture.componentInstance).toBeTruthy();
    }));

    it('should add success notification', () => {
            notificationComponent.notificationService.success('message with success');

            expect(notificationComponent.notes.length).toBe(1);
            expect(notificationComponent.notes[0].message).toBe('message with success');
            expect(notificationComponent.notes[0].type).toBe(NotificationType.SUCCESS);
            expect(notificationComponentCompiled.querySelectorAll('.notifications').length).toBe(1);
        });
});

