import { inject, TestBed } from '@angular/core/testing';
import { ResponseOptions, BaseRequestOptions, Response, Http } from '@angular/http';
import { MockBackend, MockConnection } from '@angular/http/testing';
import { GetUserService } from './get-user.service';
import { User } from '../shared';

describe('GetUserService', () => {

    beforeEach(() => {
        TestBed.configureTestingModule({
            providers : [
                GetUserService,
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

    it('returns an existing user', inject([GetUserService, MockBackend],
        (getUserService: GetUserService, mockBackend: MockBackend) => {
            mockBackend.connections.subscribe((conn: MockConnection) => {
                conn.mockRespond(new Response(new ResponseOptions({
                    status: 200,
                    body: JSON.stringify({
                        id: 1
                    })
                })));
            });

            getUserService.byId(1).subscribe((user: User) => {
                expect(user.id).toEqual(1);
            });
        })
    );

    it('returns an error given user not found', inject([GetUserService, MockBackend],
        (getUserService: GetUserService, mockBackend: MockBackend) => {
            mockBackend.connections.subscribe((conn: MockConnection) => {
                conn.mockError(new Error('Bad response status: 404'));
            });

            getUserService.byId(1).subscribe(
                (users: any) => fail(),
                (error: Error) => expect(error).toBeTruthy());
        })
    );

});
