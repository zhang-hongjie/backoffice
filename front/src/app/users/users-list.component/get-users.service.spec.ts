import { inject, TestBed } from '@angular/core/testing';
import { ResponseOptions, BaseRequestOptions, Response, Http } from '@angular/http';
import { MockBackend, MockConnection } from '@angular/http/testing';
import { GetUsersService } from './get-users.service';
import { User } from '../shared';

describe('GetUsers', () => {

    beforeEach(() => {
        TestBed.configureTestingModule({
            providers : [
                GetUsersService,
                MockBackend,
                BaseRequestOptions,
                {
                  provide : Http,
                  useFactory : (backend: MockBackend, defaultOptions: BaseRequestOptions) => {
                      return new Http(backend, defaultOptions);
                  },
                  deps: [MockBackend, BaseRequestOptions]
                }
            ]
        });
    });

    it('returns a list of users', inject([GetUsersService, MockBackend],
        (getUsersService: GetUsersService, mockBackend: MockBackend) => {
            mockBackend.connections.subscribe((conn: MockConnection) => {
                conn.mockRespond(new Response(new ResponseOptions({
                    status: 200,
                    body: JSON.stringify([
                        {
                            id: 1,
                            email: 'foo@foo.com'
                        },
                        {
                            id: 2,
                            email: 'bar@bar.com'
                        }
                    ])
                })));
            });

            getUsersService.all().subscribe((users: User[]) => {
                expect(users).toContain(new User(1, 'foo@foo.com'));
                expect(users).toContain(new User(2, 'bar@bar.com'));
            });
        })
    );

    it('returns an error given a server error', inject([GetUsersService, MockBackend],
        (getUsersService: GetUsersService, mockBackend: MockBackend) => {
            mockBackend.connections.subscribe((conn: MockConnection) => {
                conn.mockError(new Error('Bad response status: 404'));
            });

            getUsersService.all().subscribe(
                (users: any) => fail(),
                (error: any) => expect(error).toBeTruthy());
        })
    );
});
