import { Component } from '@angular/core';
import { AuthService } from './AuthService';
import { Http, Response } from '@angular/http';

@Component({
    template: require('./LoginComponent.html'),
})

export class LoginComponent {

    public username: string;
    public password: string;

    constructor(
        private authService: AuthService,
        private http: Http
    ) {}

    public authenticate(): void {
        this
            .http
            .get('/backoffice/login?username=' + this.username + '&password=' + btoa(this.password))
            .map((res: Response) => res.json())
            .subscribe((response: any) => {
                if (response.token) {
                    this.authService.set(response.token);
                    let last_url = sessionStorage.getItem('last_url');
                    if (last_url) {
                        window.location.href = '/backoffice/front' + last_url;
                        sessionStorage.setItem('last_url', '');
                    } else {
                        window.location.href = '/backoffice';
                    }
                }
            });
    }
}
