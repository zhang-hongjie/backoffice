import { Injectable } from '@angular/core';
import { Router } from '@angular/router';

@Injectable()
export class AuthService {

    constructor(
        private router: Router
    ) {
    }

    isLoggedIn(): boolean {
        return this.get().length > 0;
    }

    checkLogin(url: string): boolean {
        if (!this.isLoggedIn()) {
            if (url && url !== '/' && url !== '/login') {
                sessionStorage.setItem('last_url', url);
            }
            this.router.navigateByUrl('/login');
            return false;
        }
        return true;
    }

    get(): string {
        return JSON.parse(sessionStorage.getItem('auth_token')) || '';
    }

    set(token: string): void {
        sessionStorage.setItem('auth_token', JSON.stringify(token));
    }
}
