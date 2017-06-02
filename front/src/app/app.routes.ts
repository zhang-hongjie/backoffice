import { Routes } from '@angular/router';
import { UsersComponent } from './users';
import { UsersListComponent } from './users/users-list.component';
import { UserEditComponent } from './users/user-edit.component';
import { LoggedInGuard } from './shared/LoggedInGuard';
import { LoginComponent } from './shared/LoginComponent';

export const AppRoutes: Routes = [
    {
        path: 'login',
        component: LoginComponent,
    },
    {
        path: 'users',
        component: UsersComponent,
        children: [
            {
                path: '',
                component: UsersListComponent,
                canActivate: [LoggedInGuard]
            },
            {
                path: 'new',
                component: UserEditComponent,
                canActivate: [LoggedInGuard]
            },
            {
                path: ':id/edit',
                component: UserEditComponent,
                canActivate: [LoggedInGuard]
            }
        ]
    },
    {
        path: '**',
        redirectTo: '/users'
    }
];
