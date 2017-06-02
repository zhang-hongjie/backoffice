import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { GetUsersService } from './get-users.service';
import { User } from '../shared';
import { DeleteUserService } from './delete-user.service';

@Component({
    template: require('./users-list.component.html'),
    styles: [require('./users-list.component.scss')],
    providers: [
        GetUsersService,
        DeleteUserService
    ]
})
export class UsersListComponent implements OnInit {
    public users: User[];

    constructor(private router: Router,
                private getUsersService: GetUsersService,
                private deleteUserService: DeleteUserService) {
    }

    ngOnInit() {
        this.fetchUsers();
    }

    deleteUser(user: User) {
        this.deleteUserService.byId(user.id)
            .subscribe(() => this.fetchUsers());
    }

    goToEditUser(user: User) {
        this.router.navigateByUrl('/users/' + user.id + '/edit');
    }

    private fetchUsers() {
        this.getUsersService.all().subscribe(users => {
            this.users = users;
        });
    }
}
