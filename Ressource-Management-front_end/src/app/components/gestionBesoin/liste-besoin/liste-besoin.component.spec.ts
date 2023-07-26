import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListeBesoinComponent } from './liste-besoin.component';

describe('ListeBesoinComponent', () => {
  let component: ListeBesoinComponent;
  let fixture: ComponentFixture<ListeBesoinComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ListeBesoinComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ListeBesoinComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
