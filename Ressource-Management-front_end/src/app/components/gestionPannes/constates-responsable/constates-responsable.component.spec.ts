import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConstatesResponsableComponent } from './constates-responsable.component';

describe('ConstatesResponsableComponent', () => {
  let component: ConstatesResponsableComponent;
  let fixture: ComponentFixture<ConstatesResponsableComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ConstatesResponsableComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ConstatesResponsableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
