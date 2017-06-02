import { Injectable } from '@angular/core';
import { CanActivate } from '@angular/router';
import { AuthService } from './AuthService';
import { Router, RouterStateSnapshot, ActivatedRouteSnapshot } from '@angular/router';

@Injectable()
export class LoggedInGuard implements CanActivate {
    constructor(
        private authService: AuthService,
        private router: Router
    ) {}

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
        let url: string = null;
        if (state) {
            url = state.url;
        }
        return this.authService.checkLogin(url);
    }
}
