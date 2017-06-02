import { Component } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { NgbAccordionConfig, NgbTooltipConfig, NgbPaginationConfig, NgbTabsetConfig } from '@ng-bootstrap/ng-bootstrap';
import { AuthService } from './shared/AuthService';
import { CustomHttp } from './shared/CustomHttp';
import { NotificationService } from './notification';
import { EventSubject } from './shared/EventSubject';

@Component({
    selector: 'backoffice-app',
    template: require('./app.component.html'),
    styles: [require('./app.component.scss')],
    providers: [
        Title,
        AuthService,
        NotificationService,
        NgbAccordionConfig,
        NgbTooltipConfig,
        NgbPaginationConfig,
        NgbTabsetConfig,
        CustomHttp,
        EventSubject,
        CustomHttp
    ]
})

export class AppComponent {

    constructor(
        public authService: AuthService,
        ) {
    }

    public logout(): void {
        this.authService.set(null);
        window.location.href = '/';
    }

    public getMenuLinks(): Array<any> {
        return [
        ];
    }
}
