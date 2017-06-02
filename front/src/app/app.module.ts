import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { HttpModule, Http, RequestOptions, XHRBackend } from '@angular/http';
import { CustomHttp } from './shared/CustomHttp';
import { CustomRequestOptions } from './shared/CustomRequestOptions';
import { AppComponent } from './app.component';
import { AppRoutes } from './app.routes';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { AuthService } from './shared/AuthService';
import { FocusDirective } from './utils/focus/focus.directive';
import { LoaderComponent } from './loader/loader.component';
import { LoginComponent } from './shared/LoginComponent';
import { NotificationComponent } from './notification';
import { TableSortPipe } from './utils/pipes/table-sort-pipe';
import { UsersComponent } from './users/users.component/users.component';
import { UsersListComponent } from './users/users-list.component/users-list.component';
import { UserEditComponent } from './users/user-edit.component/user-edit.component';
import { LoggedInGuard } from './shared/LoggedInGuard';
import { EnumPipe } from './utils/pipes/enum-pipe';

@NgModule({
    declarations: [
        AppComponent,
        FocusDirective,
        EnumPipe,
        LoaderComponent,
        LoginComponent,
        NotificationComponent,
        TableSortPipe,
        UsersComponent,
        UsersListComponent,
        UserEditComponent,
    ],
    imports: [
        BrowserModule,
        FormsModule,
        HttpModule,
        RouterModule,
        RouterModule.forRoot(AppRoutes),
        NgbModule
    ],
    providers: [
        {
            provide: RequestOptions,
            useClass: CustomRequestOptions
        },
        {
            provide: Http,
            useFactory: (xhrBackend: XHRBackend, requestOptions: RequestOptions, router: Router, authService: AuthService) =>
               new CustomHttp(xhrBackend, requestOptions, router, authService),
            deps: [XHRBackend, RequestOptions, Router, AuthService]
        },
        LoggedInGuard,
        AuthService
    ],
    bootstrap: [AppComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AppModule {
}
