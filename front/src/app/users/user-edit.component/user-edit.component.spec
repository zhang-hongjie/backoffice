import { Router, ActivatedRoute } from '@angular/router';
import { TestComponentBuilder, ComponentFixture, inject, addProviders } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { UserEditComponent } from './user-edit.component';
import { GetUserService } from './get-user.service';
import { CreateUserService } from './create-user.service';
import { UpdateUserService } from './update-user.service';
import { User } from '../shared';
import { LocationStrategy, HashLocationStrategy, PlatformLocation } from '@angular/common';
import { BrowserPlatformLocation } from '@angular/platform-browser';

class FakeRouter {
    url: string;

    navigateByUrl(url: string) {
        this.url = url;
    }
}

class FakeActivatedRoute {
    params: Observable<any>;

    constructor(private value: number) {
        this.params = Observable.of({
            'id': value
        });
    }

}

class GetUserServiceMock {
    byId(id: number): Observable<User> {
        return Observable.of(new User(id, id + '@acme.com'));
    }
}

class CreateUserServiceMock {
    email: string;

    execute(email: string): Observable<void> {
        this.email = email;
        return Observable.of(null);
    }
}

class UpdateUserServiceMock {
    user: User;

    execute(user: User): Observable<void> {
        this.user = user;
        return Observable.of(null);
    }
}

describe('UserEditComponent - create new user', () => {

    let createUserServiceMock: any;
    let updateUserServiceMock: any;

    beforeEach(() => {
        createUserServiceMock = new CreateUserServiceMock();
        updateUserServiceMock = new UpdateUserServiceMock();
        addProviders([
            {provide: LocationStrategy, useClass: HashLocationStrategy},
            {provide: PlatformLocation, useClass: BrowserPlatformLocation},
            {provide: Router, useClass: FakeRouter},
            {provide: ActivatedRoute, useValue: new FakeActivatedRoute(null)}
        ]);
    });

    it('renders an empty form', inject([TestComponentBuilder], (tcb: TestComponentBuilder) => {
        tcb
            .overrideProviders(UserEditComponent, [
                {provide: GetUserService, useClass: GetUserServiceMock},
                {provide: CreateUserService, useValue: createUserServiceMock},
                {provide: UpdateUserService, useValue: updateUserServiceMock},
            ])
            .createAsync(UserEditComponent)
            .then((fixture: ComponentFixture<UserEditComponent>) => {
                let elt = fixture.debugElement.nativeElement;

                fixture.detectChanges();

                expect(elt.querySelector('#email').value).toEqual('');
            });
    }));

    it('creates an user', inject([TestComponentBuilder], (tcb: TestComponentBuilder) => {
        tcb
            .overrideProviders(UserEditComponent, [
                {provide: GetUserService, useClass: GetUserServiceMock},
                {provide: CreateUserService, useValue: createUserServiceMock},
                {provide: UpdateUserService, useValue: updateUserServiceMock}
            ])
            .createAsync(UserEditComponent)
            .then((fixture: ComponentFixture<UserEditComponent>) => {
                let elt = fixture.debugElement.nativeElement;

                fixture.detectChanges();
                elt.querySelector('#email').value = 'new@new.com';
                elt.querySelector('#email').dispatchEvent(new Event('input'));
                elt.querySelector('form').dispatchEvent(new Event('submit'));
                fixture.detectChanges();

                expect(createUserServiceMock.email).toEqual('new@new.com');
            });
    }));

    it('creates an user', inject([TestComponentBuilder], (tcb: TestComponentBuilder) => {
        tcb
            .overrideProviders(UserEditComponent, [
                {provide: GetUserService, useClass: GetUserServiceMock},
                {provide: CreateUserService, useValue: createUserServiceMock},
                {provide: UpdateUserService, useValue: updateUserServiceMock}
            ])
            .createAsync(UserEditComponent)
            .then((fixture: ComponentFixture<UserEditComponent>) => {
                let elt = fixture.debugElement.nativeElement;

                fixture.detectChanges();
                elt.querySelector('#email').value = 'new@new.com';
                elt.querySelector('#email').dispatchEvent(new Event('input'));
                elt.querySelector('form').dispatchEvent(new Event('submit'));
                fixture.detectChanges();

                expect(createUserServiceMock.email).toEqual('new@new.com');
            });
    }));

    it('validates the presence of the email', inject([TestComponentBuilder], (tcb: TestComponentBuilder) => {
        tcb
            .overrideProviders(UserEditComponent, [
                {provide: GetUserService, useClass: GetUserServiceMock},
                {provide: CreateUserService, useValue: createUserServiceMock},
                {provide: UpdateUserService, useValue: updateUserServiceMock}
            ])
            .createAsync(UserEditComponent)
            .then((fixture: ComponentFixture<UserEditComponent>) => {
                let elt = fixture.debugElement.nativeElement;

                fixture.detectChanges();
                elt.querySelector('#email').value = '';
                elt.querySelector('#email').dispatchEvent(new Event('input'));
                elt.querySelector('form').dispatchEvent(new Event('submit'));
                fixture.detectChanges();

                expect(elt.querySelector('#emailHelp').textContent).toContain('requise');
            });
    }));

    it('validates the email format', inject([TestComponentBuilder], (tcb: TestComponentBuilder) => {
        tcb
            .overrideProviders(UserEditComponent, [
                {provide: GetUserService, useClass: GetUserServiceMock},
                {provide: CreateUserService, useValue: createUserServiceMock},
                {provide: UpdateUserService, useValue: updateUserServiceMock}
            ])
            .createAsync(UserEditComponent)
            .then((fixture: ComponentFixture<UserEditComponent>) => {
                let elt = fixture.debugElement.nativeElement;

                fixture.detectChanges();
                elt.querySelector('#email').value = 'invalid';
                elt.querySelector('#email').dispatchEvent(new Event('input'));
                elt.querySelector('form').dispatchEvent(new Event('submit'));
                fixture.detectChanges();

                expect(elt.querySelector('#emailHelp').textContent).toContain('invalid');
            });
    }));
});

describe('UserEditComponent - edit existant user', () => {

    let createUserServiceMock: any;
    let updateUserServiceMock: any;

    beforeEach(() => {
        createUserServiceMock = new CreateUserServiceMock();
        updateUserServiceMock = new UpdateUserServiceMock();
        addProviders([
            {provide: Router, useClass: FakeRouter},
            {provide: ActivatedRoute, useValue: new FakeActivatedRoute(1)}
        ]);
    });

    it('renders the user form given a loaded user', inject([TestComponentBuilder], (tcb: TestComponentBuilder) => {
        tcb
            .overrideProviders(UserEditComponent, [
                {provide: GetUserService, useClass: GetUserServiceMock},
                {provide: CreateUserService, useValue: createUserServiceMock},
                {provide: UpdateUserService, useValue: updateUserServiceMock}
            ])
            .createAsync(UserEditComponent)
            .then((fixture: ComponentFixture<UserEditComponent>) => {
                let elt = fixture.debugElement.nativeElement;

                fixture.detectChanges();

                expect(elt.querySelector('#email').value).toEqual('1@acme.com');
            });
    }));

    it('updates an user', inject([TestComponentBuilder], (tcb: TestComponentBuilder) => {
        tcb
            .overrideProviders(UserEditComponent, [
                {provide: GetUserService, useClass: GetUserServiceMock},
                {provide: CreateUserService, useValue: createUserServiceMock},
                {provide: UpdateUserService, useValue: updateUserServiceMock}
            ])
            .createAsync(UserEditComponent)
            .then((fixture: ComponentFixture<UserEditComponent>) => {
                let elt = fixture.debugElement.nativeElement;

                fixture.detectChanges();
                elt.querySelector('#email').value = 'new@new.com';
                elt.querySelector('#email').dispatchEvent(new Event('input'));
                elt.querySelector('form').dispatchEvent(new Event('submit'));
                fixture.detectChanges();

                expect(updateUserServiceMock.user).toEqual(new User(1, 'new@new.com'));
            });
    }));
});

