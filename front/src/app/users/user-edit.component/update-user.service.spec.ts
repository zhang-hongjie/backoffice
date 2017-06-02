import { inject, TestBed } from '@angular/core/testing';
import { ResponseOptions, BaseRequestOptions, Response, Http } from '@angular/http';
import { MockBackend, MockConnection } from '@angular/http/testing';
import { UpdateUserService } from './update-user.service';
import { User } from '../shared';

describe('UpdateUserService', () => {

    beforeEach(() => {
        TestBed.configureTestingModule({
            providers : [
                UpdateUserService,
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

    it('updates a user', inject([UpdateUserService, MockBackend],
        (updateUserService: UpdateUserService, mockBackend: MockBackend) => {
            mockBackend.connections.subscribe((conn: MockConnection) => {
                conn.mockRespond(new Response(new ResponseOptions({
                    status: 200
                })));
            });

            updateUserService.execute(new User(1, 'foo@foo.com')).subscribe(null, () => fail());
        })
    );

    it('handles the returned error', inject([UpdateUserService, MockBackend],
        (updateUserService: UpdateUserService, mockBackend: MockBackend) => {
            mockBackend.connections.subscribe((conn: MockConnection) => {
                conn.mockError(new Error('Bad response status: 500'));
            });

            updateUserService.execute(new User(1, 'foo@foo.com')).subscribe(
                () => fail(),
                (error: Error) => expect(error).toBeTruthy());
        })
    );
});
