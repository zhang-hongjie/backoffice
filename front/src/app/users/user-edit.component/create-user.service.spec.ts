import { inject, TestBed } from '@angular/core/testing';
import { ResponseOptions, BaseRequestOptions, Response, Http } from '@angular/http';
import { MockBackend, MockConnection } from '@angular/http/testing';
import { CreateUserService } from './create-user.service';

describe('CreateUserService', () => {

    beforeEach(() => {
        TestBed.configureTestingModule({
            providers : [
                CreateUserService,
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

    it('creates a user', inject([CreateUserService, MockBackend],
        (createUserService: CreateUserService, mockBackend: MockBackend) => {
            mockBackend.connections.subscribe((conn: MockConnection) => {
                conn.mockRespond(new Response(new ResponseOptions({
                    status: 200
                })));
            });

            createUserService.execute('foo@foo.com').subscribe(null, () => fail());
        })
    );

    it('handles the returned error', inject([CreateUserService, MockBackend],
        (createUserService: CreateUserService, mockBackend: MockBackend) => {
            mockBackend.connections.subscribe((conn: MockConnection) => {
                conn.mockError(new Error('Bad response status: 500'));
            });

            createUserService.execute('foo@foo.com').subscribe(
                () => fail(),
                (error: Error) => expect(error).toBeTruthy());
        })
    );
});
