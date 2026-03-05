import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WaitingVerify } from './waiting-verify';

describe('WaitingVerify', () => {
  let component: WaitingVerify;
  let fixture: ComponentFixture<WaitingVerify>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [WaitingVerify]
    })
    .compileComponents();

    fixture = TestBed.createComponent(WaitingVerify);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
