import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { User } from '../index';
import { GetUserService } from './get-user.service';
import { CreateUserService } from './create-user.service';
import { UpdateUserService } from './update-user.service';

@Component({
    template: require('./user-edit.component.html'),
    providers: [GetUserService, CreateUserService, UpdateUserService]
})
export class UserEditComponent implements OnInit, OnDestroy {
    user: User;
    alert: string;
    editSubscription: Subscription;

    constructor(private router: Router,
                private route: ActivatedRoute,
                private getUserService: GetUserService,
                private createUserService: CreateUserService,
                private updateUserService: UpdateUserService) {
    }

    ngOnInit() {
        this.editSubscription = this.route.params
            .map(params => params['id'])
            .subscribe((id) => {
                if (id) {
                    this.loadUser(id);
                } else {
                    this.user = this.defaultUser();
                }
            });
    }

    goToUsers() {
        this.router.navigateByUrl('/users');
    }

    saveOrUpdateUser() {
        this.user.id ? this.updateUser() : this.createUser();
    }

    private createUser() {
        this.createUserService
            .execute(this.user.email)
            .subscribe(
                () => this.goToUsers(),
                (error) => this.alert = error
            );
    }

    private updateUser() {
        this.updateUserService
            .execute(this.user)
            .subscribe(
                () => this.goToUsers(),
                (error) => this.alert = error
            );
    }

    private loadUser(id: number) {
        this.getUserService
            .byId(id)
            .subscribe(user => this.user = user);
    }

    private defaultUser(): User {
        return new User(null, null);
    }

    ngOnDestroy() {
        this.editSubscription.unsubscribe();
    }
}
