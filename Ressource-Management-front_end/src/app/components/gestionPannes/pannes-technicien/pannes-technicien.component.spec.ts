import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PannesTechnicienComponent } from './pannes-technicien.component';

describe('PannesTechnicienComponent', () => {
  let component: PannesTechnicienComponent;
  let fixture: ComponentFixture<PannesTechnicienComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PannesTechnicienComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PannesTechnicienComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
