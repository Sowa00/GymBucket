import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WorkoutPlansComponent } from './workout-plans.component';

describe('WorkoutPlansComponent', () => {
  let component: WorkoutPlansComponent;
  let fixture: ComponentFixture<WorkoutPlansComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [WorkoutPlansComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(WorkoutPlansComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
