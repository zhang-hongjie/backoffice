import { TestBed, fakeAsync, inject } from '@angular/core/testing';
import { AuthService } from './AuthService';
import { RouterModule, Router } from '@angular/router';

let authService: AuthService;

describe('AuthService with token', () => {
    beforeEach(() => {
        sessionStorage.setItem('auth_token', JSON.stringify('A1234'));
        TestBed.configureTestingModule({
            providers: [
                AuthService,
                { provide: Router, useValue: { navigateByUrl: function() {}}}
            ],
            imports: [
                RouterModule
            ]
        });
        authService = TestBed.get(AuthService);
    });

    it('should be logged', () => {
        expect(authService.isLoggedIn()).toBeTruthy();
    });

    it('should return a token', () => {
        expect(authService.get()).toBe('A1234');
    });

    it('should not redirect during authentication checking', () => {
        expect(authService.checkLogin(null)).toBeTruthy();
    });
});

describe('AuthService without token', () => {
    beforeEach(() => {
        sessionStorage.setItem('auth_token', null);
        TestBed.configureTestingModule({
            providers: [
                AuthService,
                { provide: Router, useValue: { navigateByUrl: function() {}}},
            ],
            imports: [
                RouterModule
            ]
        });
        authService = TestBed.get(AuthService);
    });

    it('should not be logged', () => {
        expect(authService.isLoggedIn()).toBeFalsy();
    });

    it('should not return a token', () => {
        expect(authService.get()).toBeFalsy();
    });

    it('should redirect during authentication checking',
        inject([Router], fakeAsync((router: Router) => {
            spyOn(router, 'navigateByUrl');

            let isAuth = authService.checkLogin(null);

            expect(isAuth).toBeFalsy();
            expect(router.navigateByUrl).toHaveBeenCalled();
    })));

    it('should redirect during authentication checking',
        inject([Router], fakeAsync((router: Router) => {
            spyOn(router, 'navigateByUrl');

            let isAuth = authService.checkLogin('/users');

            expect(isAuth).toBeFalsy();
            expect(sessionStorage.getItem('last_url')).toBe('/users');
            expect(router.navigateByUrl).toHaveBeenCalled();
    })));
});
