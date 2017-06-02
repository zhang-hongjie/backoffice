import { async, TestBed } from '@angular/core/testing';
import { HttpModule } from '@angular/http';
import { FormsModule } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { LoaderComponent } from './loader.component';

let fixture: any;
let loaderComponent: any;

describe('BudgetsSearchComponent', () => {
    beforeEach(() => {
        TestBed.configureTestingModule({
            declarations: [LoaderComponent],
            providers: [
                { provide: Router, useValue: {} }
            ],
            imports: [
                HttpModule,
                RouterModule,
                FormsModule
            ]
        });
        TestBed.overrideComponent(LoaderComponent, { set: {
            template: ``,
            styles: [``]
        }});
        fixture = TestBed.createComponent(LoaderComponent);
        loaderComponent = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create the component', async(() => {
        expect(loaderComponent).toBeTruthy();
    }));
});
