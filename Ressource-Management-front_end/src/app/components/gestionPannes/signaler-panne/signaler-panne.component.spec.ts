import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SignalerPanneComponent } from './signaler-panne.component';

describe('SignalerPanneComponent', () => {
  let component: SignalerPanneComponent;
  let fixture: ComponentFixture<SignalerPanneComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SignalerPanneComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SignalerPanneComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
