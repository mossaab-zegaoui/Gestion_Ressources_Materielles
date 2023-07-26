import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PannesMemberDepartementComponent } from './pannes-member-departement.component';

describe('PannesMemberDepartementComponent', () => {
  let component: PannesMemberDepartementComponent;
  let fixture: ComponentFixture<PannesMemberDepartementComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PannesMemberDepartementComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PannesMemberDepartementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
