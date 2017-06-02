import { TestBed, inject } from '@angular/core/testing';
import { LoginComponent } from './LoginComponent';
import { HttpModule, BaseRequestOptions, Http, Response, ResponseOptions } from '@angular/http';
import { MockBackend, MockConnection } from '@angular/http/testing';
import { FormsModule } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { AuthService } from './AuthService';

let loginComponent: LoginComponent;
let fixture: any;

describe('LoginComponent', () => {
    beforeEach(() => {
        TestBed.configureTestingModule({
            declarations: [LoginComponent],
            providers: [
                {
                  provide : Http,
                  useFactory : (backend: MockBackend, defaultOptions: BaseRequestOptions) => {
                      return new Http(backend, defaultOptions);
                  },
                  deps: [MockBackend, BaseRequestOptions]
                },
                AuthService,
                MockBackend,
                BaseRequestOptions,
                { provide: Router, useValue: {} }
            ],
            imports: [
                HttpModule,
                FormsModule,
                RouterModule
            ]
        });
        fixture = TestBed.createComponent(LoginComponent);
        loginComponent = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should instanciate the component', () => {
        expect(fixture.componentInstance).toBeTruthy();
    });

    it('should authenticate', inject([MockBackend], (mockBackend: MockBackend) => {
        mockBackend.connections.subscribe((conn: MockConnection) => {
            conn.mockRespond(new Response(new ResponseOptions({
                status: 200,
                body: JSON.stringify({ token: null })
            })));
        });
        loginComponent.authenticate();
    }));
});
