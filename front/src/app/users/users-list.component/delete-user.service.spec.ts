import { inject, TestBed } from '@angular/core/testing';
import { ResponseOptions, BaseRequestOptions, Response, Http } from '@angular/http';
import { MockBackend, MockConnection } from '@angular/http/testing';
import { DeleteUserService } from './delete-user.service';

describe('DeleteUserService', () => {

    beforeEach(() => {
        TestBed.configureTestingModule({
            providers : [
                DeleteUserService,
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

    it('deletes a user by id', inject([DeleteUserService, MockBackend],
        (deleteUserService: DeleteUserService, mockBackend: MockBackend) => {
            mockBackend.connections.subscribe((conn: MockConnection) => {
                conn.mockRespond(new Response(new ResponseOptions({
                    status: 200
                })));
            });

            deleteUserService.byId(1).subscribe(null, () => fail());
        })
    );
});
