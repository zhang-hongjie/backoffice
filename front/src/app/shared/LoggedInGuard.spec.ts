import { TestBed } from '@angular/core/testing';
import { LoggedInGuard } from './LoggedInGuard';
import { AuthService } from './AuthService';
import { Router } from '@angular/router';

let loggedInGuard: LoggedInGuard;
describe('LoggedInGuard with token', () => {
    beforeEach(() => {
        window.sessionStorage.setItem('auth_token', JSON.stringify('testoken'));
        TestBed.configureTestingModule({
            providers: [
                LoggedInGuard,
                AuthService,
                { provide : Router, useValue: {}}
            ]
        });
        loggedInGuard = TestBed.get(LoggedInGuard);
    });

    it('should activate the route', () => {
        expect(loggedInGuard.canActivate(null,  null)).toBeTruthy();
    });
});
