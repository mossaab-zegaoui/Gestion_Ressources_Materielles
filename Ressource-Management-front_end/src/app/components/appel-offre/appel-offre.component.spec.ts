import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AppelOffreComponent } from './appel-offre.component';

describe('AppelOffreComponent', () => {
  let component: AppelOffreComponent;
  let fixture: ComponentFixture<AppelOffreComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AppelOffreComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AppelOffreComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
