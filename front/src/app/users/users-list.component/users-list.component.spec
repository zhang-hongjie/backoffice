import { Router, ActivatedRoute } from '@angular/router';
import { ComponentFixture, TestComponentBuilder, inject, addProviders } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { UsersListComponent } from './users-list.component';
import { DeleteUserService } from './delete-user.service';
import { GetUsersService } from './get-users.service';
import { User } from '../shared';
import { LocationStrategy, HashLocationStrategy, PlatformLocation } from '@angular/common';
import { BrowserPlatformLocation } from '@angular/platform-browser';

let users: User[] = [
    new User(1, 'foo@foo.com'),
    new User(2, 'bar@bar.com')
];

class DeleteUserServiceMock {
    byId(id: number): Observable<void> {
        users = users.filter(user => user.id !== 1);
        return Observable.of(null);
    }
}

class GetUsersServiceMock {
    all(): Observable<User[]> {
        return Observable.of(users);
    }
}

class FakeRouter {
    url: string;

    navigateByUrl(url: string) {
        this.url = url;
    }

    createUrlTree(commands: any[]) {
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

describe('UsersListComponent', () => {
    let deleteUserServiceMock: any;

    beforeEach(() => {
        deleteUserServiceMock = new DeleteUserServiceMock();
        addProviders([
            {provide: LocationStrategy, useClass: HashLocationStrategy},
            {provide: PlatformLocation, useClass: BrowserPlatformLocation},
            {provide: Router, useClass: FakeRouter},
            {provide: ActivatedRoute, useValue: new FakeActivatedRoute(null)}
        ]);
    });

    it('renders the users list', inject([TestComponentBuilder], (tcb: TestComponentBuilder) => {
        tcb
            .overrideProviders(UsersListComponent, [
                {provide: GetUsersService, useClass: GetUsersServiceMock},
                {provide: DeleteUserService, useValue: deleteUserServiceMock}
            ])
            .createAsync(UsersListComponent)
            .then((fixture: ComponentFixture<UsersListComponent>) => {
                let elt = fixture.debugElement.nativeElement;

                fixture.detectChanges();

                expect(elt.querySelectorAll('tbody tr').length).toEqual(2);
            });
    }));

    it('delete the first user', inject([TestComponentBuilder], (tcb: TestComponentBuilder) => {
        tcb
            .overrideProviders(UsersListComponent, [
                {provide: GetUsersService, useClass: GetUsersServiceMock},
                {provide: DeleteUserService, useValue: deleteUserServiceMock}
            ])
            .createAsync(UsersListComponent)
            .then((fixture: ComponentFixture<UsersListComponent>) => {
                let elt = fixture.debugElement.nativeElement;

                fixture.detectChanges();
                elt.querySelector('#delete-1').click();
                fixture.detectChanges();

                expect(elt.querySelector('#row-user-1')).toBeNull();
                expect(elt.querySelector('#row-user-2')).not.toBeNull();
            });
    }));
});
