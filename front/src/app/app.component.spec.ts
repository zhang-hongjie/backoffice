import { async, TestBed } from '@angular/core/testing';
import { HttpModule } from '@angular/http';
import { Router } from '@angular/router';
import { AppComponent } from './app.component';

describe('AppComponent', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AppComponent],
      providers: [
        { provide : Router, useValue: {}}
      ],
      imports: [
        HttpModule
      ]
    });
  });

  it('should create the component', async(() => {
    TestBed.overrideComponent(AppComponent, { set: {
      template: `<span>test<span>`,
      styles: [``]
    }}).compileComponents().then(() => {
      let fixture = TestBed.createComponent(AppComponent);
      fixture.detectChanges();
      expect(fixture.componentInstance).toBeTruthy();
    });
  }));

});
